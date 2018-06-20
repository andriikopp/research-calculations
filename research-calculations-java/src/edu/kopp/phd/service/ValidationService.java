package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidationService {
    private RDFRepository repository = RDFRepository.getInstance();

    private ControlFlowService controlFlowService = new ControlFlowService();

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

    public Set<Function> validateFunctionsByProcessName(String processName) {
        Set<Function> invalidFunctions = new HashSet<>();

        for (Function function : controlFlowService.getDetailedFunctionsByProcessName(processName))
            if (!(function.getPreceding().size() == 1 && function.getSubsequent().size() == 1))
                invalidFunctions.add(function);

        return invalidFunctions;
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
            if (!((gateway.getPreceding().size() == 1 && gateway.getSubsequent().size() >= 1) ||
                    (gateway.getPreceding().size() >= 1 && gateway.getSubsequent().size() == 1)))
                invalidGateways.add(gateway);

        return invalidGateways;
    }
}
