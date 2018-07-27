package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import edu.kopp.phd.service.similarity.ConcreteSimilarityMethodFactory;
import edu.kopp.phd.service.similarity.api.SimilarityMethod;
import org.apache.jena.rdf.model.*;

import java.util.*;

public class ValidationService {
    private RDFRepository repository = RDFRepository.getInstance();

    private ControlFlowService controlFlowService;

    public Set<FlowObject> getStartNodesByProcessName(String processName) {
        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        Set<FlowObject> startNodes = new HashSet<>();

        for (Map.Entry<FlowObject, List<Statement>> entry : repository.retrieveProcess(process).entrySet()) {
            boolean isPossibleStartNode = false;

            for (Statement statement : entry.getValue())
                if (statement.getPredicate().equals(repository.getA()) &&
                        (statement.getObject().equals(repository.getEvent()) || statement.getObject().equals(repository.getProcess()))) {
                    isPossibleStartNode = true;
                    break;
                }

            if (isPossibleStartNode) {
                StmtIterator iterator = repository.getModel().listStatements(null,
                        repository.getIsPredecessorOf(), entry.getKey().getResource());

                int countPredecessors = 0;

                while (iterator.hasNext()) {
                    iterator.nextStatement();
                    countPredecessors++;
                }

                if (countPredecessors == 0)
                    startNodes.add(entry.getKey());
            }
        }

        return startNodes;
    }

    public Set<FlowObject> getEndNodesByProcessName(String processName) {
        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        Set<FlowObject> endNodes = new HashSet<>();

        for (Map.Entry<FlowObject, List<Statement>> entry : repository.retrieveProcess(process).entrySet()) {
            boolean isPossibleEndNode = false;

            for (Statement statement : entry.getValue())
                if (statement.getPredicate().equals(repository.getA()) &&
                        (statement.getObject().equals(repository.getEvent()) || statement.getObject().equals(repository.getProcess()))) {
                    isPossibleEndNode = true;
                    break;
                }

            if (isPossibleEndNode) {
                StmtIterator iterator = repository.getModel().listStatements(entry.getKey().getResource(),
                        repository.getIsPredecessorOf(), (RDFNode) null);

                int countSubsequent = 0;

                while (iterator.hasNext()) {
                    iterator.nextStatement();
                    countSubsequent++;
                }

                if (countSubsequent == 0)
                    endNodes.add(entry.getKey());
            }
        }

        return endNodes;
    }

    public Set<FlowObject> validateNodesCoherenceByProcessName(String processName) {
        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        Map<FlowObject, List<Statement>> nodes = repository.retrieveProcess(process);

        FlowObject startNode = (FlowObject) nodes.keySet().toArray()[0];

        Queue<FlowObject> queue = new LinkedList<>();
        Set<FlowObject> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            FlowObject node = queue.poll();

            for (FlowObject flowObject : getCoherentNodes(node, nodes)) {
                if (!visited.contains(flowObject)) {
                    queue.add(flowObject);
                    visited.add(flowObject);
                }
            }
        }

        Set<FlowObject> unreachableNodes = new HashSet<>();
        unreachableNodes.addAll(nodes.keySet());
        unreachableNodes.removeAll(visited);

        return unreachableNodes;
    }

    public boolean validateStartEndNodesByProcessName(String processName) {
        return getStartNodesByProcessName(processName).size() >= 1 &&
                getEndNodesByProcessName(processName).size() >= 1;
    }

    public Set<Event> validateEventsByProcessName(String processName) {
        Set<Event> invalidEvents = new HashSet<>();

        for (Event event : controlFlowService.getDetailedEventsByProcessName(processName))
            if (!(event.getPreceding().size() <= 1 && event.getSubsequent().size() <= 1))
                invalidEvents.add(event);

        return invalidEvents;
    }

    public Set<Event> getEventsThatArePrecedingForAnotherEventsByProcessName(String processName) {
        Set<Event> invalidEvents = new HashSet<>();

        SimilarityMethod similarityMethod = ConcreteSimilarityMethodFactory.createInstance().getSimilarityMethod();

        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        for (Event event : controlFlowService.getDetailedEventsByProcessName(processName))
            for (FlowObject subsequent : event.getSubsequent()) {
                String label = subsequent.getResource().getLocalName();

                if (similarityMethod.getNodeTypeByLabel(label, process).equals(repository.getEvent()))
                    invalidEvents.add(event);
            }

        return invalidEvents;
    }

    public Set<Function> validateFunctionsByProcessName(String processName) {
        Set<Function> invalidFunctions = new HashSet<>();

        for (Function function : controlFlowService.getDetailedFunctionsByProcessName(processName))
            if (!(function.getPreceding().size() == 1 && function.getSubsequent().size() == 1))
                invalidFunctions.add(function);

        return invalidFunctions;
    }

    public boolean processDoesNotHaveAtLeastOneFunction(String processName) {
        return controlFlowService.getDetailedFunctionsByProcessName(processName).isEmpty();
    }

    public Set<Process> validateProcessesByProcessName(String processName) {
        Set<Process> invalidProcesses = new HashSet<>();

        for (Process process : controlFlowService.getDetailedProcessesByProcessName(processName))
            if (!((process.getPreceding().size() == 1 && process.getSubsequent().size() == 0) ||
                    (process.getPreceding().size() == 0 && process.getSubsequent().size() == 1)))
                invalidProcesses.add(process);

        return invalidProcesses;
    }

    public Set<Gateway> validateGatewaysByProcessName(String processName) {
        Set<Gateway> invalidGateways = new HashSet<>();

        for (Gateway gateway : controlFlowService.getDetailedGatewaysByProcessName(processName))
            if (!((gateway.getPreceding().size() == 1 && gateway.getSubsequent().size() > 1) ||
                    (gateway.getPreceding().size() > 1 && gateway.getSubsequent().size() == 1)))
                invalidGateways.add(gateway);

        return invalidGateways;
    }

    public Set<Event> validateEventsAndGatewaysConnectionsByProcessName(String processName) {
        Set<Event> invalidEvents = new HashSet<>();

        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        for (Event event : controlFlowService.getDetailedEventsByProcessName(processName))
            for (FlowObject flowObject : event.getSubsequent())
                if (flowObject.getResource().getURI().contains(RDFRepository.NS_REPOSITORY +
                            process.getResource().getLocalName() + "/or") ||
                        flowObject.getResource().getURI().contains((RDFRepository.NS_REPOSITORY +
                                process.getResource().getLocalName() + "/xor")))
                    if (countPrecedingByURI(flowObject.getResource().getURI()) == 1 &&
                            countSubsequentByURI(flowObject.getResource().getURI()) > 1)
                        invalidEvents.add(event);

        return invalidEvents;
    }

    private Set<FlowObject> getCoherentNodes(FlowObject flowObject, Map<FlowObject, List<Statement>> flowObjects) {
        Model processModel = ModelFactory.createDefaultModel();

        for (Map.Entry<FlowObject, List<Statement>> entry : flowObjects.entrySet())
            for (Statement statement : entry.getValue())
                processModel.add(statement);

        Set<FlowObject> coherentNodes = new HashSet<>();

        StmtIterator iterator = processModel.listStatements();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            if ((statement.getSubject().equals(flowObject.getResource()) &&
                    statement.getPredicate().equals(repository.getIsPredecessorOf())))
                coherentNodes.add(new FlowObject((Resource) statement.getObject()));


            if ((statement.getObject().equals(flowObject.getResource()) &&
                    statement.getPredicate().equals(repository.getIsPredecessorOf())))
                coherentNodes.add(new FlowObject(statement.getSubject()));
        }

        return coherentNodes;
    }

    private int countPrecedingByURI(String uri) {
        int preceding = 0;

        StmtIterator iterator = repository.getModel().listStatements();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            if (statement.getPredicate().equals(repository.getIsPredecessorOf()) &&
                    ((Resource) statement.getObject()).getURI().equals(uri))
                preceding++;
        }

        return preceding;
    }

    private int countSubsequentByURI(String uri) {
        int subsequent = 0;

        StmtIterator iterator = repository.getModel().listStatements();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            if (statement.getSubject().getURI().equals(uri) &&
                    statement.getPredicate().equals(repository.getIsPredecessorOf()))
                subsequent++;
        }

        return subsequent;
    }

    public void setControlFlowService(ControlFlowService controlFlowService) {
        this.controlFlowService = controlFlowService;
    }
}
