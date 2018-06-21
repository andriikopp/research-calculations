package edu.kopp.phd.service;

import edu.kopp.phd.model.environment.ApplicationSystem;
import edu.kopp.phd.model.environment.BusinessObject;
import edu.kopp.phd.model.environment.OrganizationalUnit;
import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import org.apache.jena.rdf.model.*;

import java.util.*;

public class ControlFlowService {
    private RDFRepository repository = RDFRepository.getInstance();

    public List<Function> getDetailedFunctionsByProcessName(String processName) {
        List<Function> processEnvironmentFunctions = new ArrayList<>();

        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        Map<FlowObject, List<Statement>> flowObjects = repository.retrieveProcess(process);

        for (Map.Entry<FlowObject, List<Statement>> entry : flowObjects.entrySet()) {
            List<Statement> statements = entry.getValue();

            boolean isFunction = false;

            for (Statement statement : statements)
                if (statement.getSubject().equals(entry.getKey().getResource()) &&
                        statement.getPredicate().equals(repository.getA()) &&
                        statement.getObject().equals(repository.getFunction())) {
                    isFunction = true;
                    break;
                }

            if (isFunction) {
                Function function = new Function(entry.getKey().getResource());

                function.setPreceding(getPreceding(entry.getKey(), flowObjects));
                function.setSubsequent(getSubsequent(entry.getKey(), flowObjects));

                function.setOrganizationalUnits(getOrganizationalUnits(function, flowObjects));

                function.setApplicationSystems(getApplicationSystems(function, flowObjects));

                function.setInputs(getInputs(function, flowObjects));
                function.setOutputs(getOutputs(function, flowObjects));

                processEnvironmentFunctions.add(function);
            }
        }

        return processEnvironmentFunctions;
    }

    public List<Event> getDetailedEventsByProcessName(String processName) {
        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        List<Event> events = new ArrayList<>();

        Map<FlowObject, List<Statement>> flowObjects = repository.retrieveProcess(process);

        for (Map.Entry<FlowObject, List<Statement>> entry : flowObjects.entrySet()) {
            for (Statement statement : entry.getValue())
                if (statement.getPredicate().equals(repository.getA()) &&
                        statement.getObject().equals(repository.getEvent())) {
                    Event event = new Event(entry.getKey().getResource());

                    event.setPreceding(getPreceding(entry.getKey(), flowObjects));
                    event.setSubsequent(getSubsequent(entry.getKey(), flowObjects));

                    events.add(event);
                    break;
                }
        }

        return events;
    }

    public List<Process> getDetailedProcessesByProcessName(String processName) {
        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        List<Process> processes = new ArrayList<>();

        Map<FlowObject, List<Statement>> flowObjects = repository.retrieveProcess(process);

        for (Map.Entry<FlowObject, List<Statement>> entry : flowObjects.entrySet()) {
            for (Statement statement : entry.getValue())
                if (statement.getPredicate().equals(repository.getA()) &&
                        statement.getObject().equals(repository.getProcess())) {
                    Process _process = new Process(entry.getKey().getResource());

                    _process.setPreceding(getPreceding(entry.getKey(), flowObjects));
                    _process.setSubsequent(getSubsequent(entry.getKey(), flowObjects));

                    processes.add(_process);
                    break;
                }
        }

        return processes;
    }

    public List<Gateway> getDetailedGatewaysByProcessName(String processName) {
        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        List<Gateway> gateways = new ArrayList<>();

        Map<FlowObject, List<Statement>> flowObjects = repository.retrieveProcess(process);

        for (Map.Entry<FlowObject, List<Statement>> entry : flowObjects.entrySet()) {
            for (Statement statement : entry.getValue())
                if (statement.getPredicate().equals(repository.getA()) &&
                        statement.getObject().equals(repository.getGateway())) {
                    Gateway gateway = new Gateway(entry.getKey().getResource());

                    gateway.setPreceding(getPreceding(entry.getKey(), flowObjects));
                    gateway.setSubsequent(getSubsequent(entry.getKey(), flowObjects));

                    gateways.add(gateway);
                    break;
                }
        }

        return gateways;
    }

    public String getJSONNodesRepresentation() {
        String nodesArray = "nodesArray = [";

        ResIterator resIterator = repository.getModel().listSubjects();

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();

            if (!nodesArray.contains(resource.getURI()))
                nodesArray += "{id: '" + resource.getURI() + "', label: '" + resource.getLocalName() + "'},";
        }

        NodeIterator nodeIterator = repository.getModel().listObjects();

        while (nodeIterator.hasNext()) {
            Resource resource = (Resource) nodeIterator.nextNode();

            if (!nodesArray.contains(resource.getURI()))
                nodesArray += "{id: '" + resource.getURI() + "', label: '" + resource.getLocalName() + "'},";
        }

        return nodesArray + "];";
    }

    public String getJSONEdgesRepresentation() {
        String edgesArray = "edgesArray = [";

        StmtIterator iterator = repository.getModel().listStatements();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            edgesArray += "{from: '" + statement.getSubject().getURI() + "', to: '"
                    + ((Resource) statement.getObject()).getURI() + "', label: '"
                    + statement.getPredicate().getLocalName() + "', arrows: 'to' },";
        }

        return edgesArray + "];";
    }

    private Set<FlowObject> getPreceding(FlowObject flowObject, Map<FlowObject, List<Statement>> flowObjects) {
        Set<FlowObject> preceding = new HashSet<>();

        for (Map.Entry<FlowObject, List<Statement>> entry : flowObjects.entrySet()) {
            List<Statement> statements = entry.getValue();

            for (Statement statement : statements)
                if (statement.getPredicate().equals(repository.getIsPredecessorOf()) &&
                        statement.getObject().equals(flowObject.getResource()))
                    preceding.add(entry.getKey());
        }

        return preceding;
    }

    private Set<FlowObject> getSubsequent(FlowObject flowObject, Map<FlowObject, List<Statement>> flowObjects) {
        Set<FlowObject> subsequent = new HashSet<>();

        for (Statement statement : flowObjects.get(flowObject))
            if (statement.getPredicate().equals(repository.getIsPredecessorOf()))
                subsequent.add(new FlowObject((Resource) statement.getObject()));

        return subsequent;
    }

    private Set<OrganizationalUnit> getOrganizationalUnits(Function function, Map<FlowObject, List<Statement>> flowObjects) {
        Set<OrganizationalUnit> organizationalUnits = new HashSet<>();

        for (Statement statement : flowObjects.get(new FlowObject(function.getResource())))
            if (statement.getPredicate().equals(repository.getIsPerformedBy()))
                organizationalUnits.add(new OrganizationalUnit((Resource) statement.getObject()));

        return organizationalUnits;
    }

    private Set<ApplicationSystem> getApplicationSystems(Function function, Map<FlowObject, List<Statement>> flowObjects) {
        Set<ApplicationSystem> applicationSystems = new HashSet<>();

        for (Statement statement : flowObjects.get(new FlowObject(function.getResource())))
            if (statement.getPredicate().equals(repository.getIsSupportedBy()))
                applicationSystems.add(new ApplicationSystem((Resource) statement.getObject()));

        return applicationSystems;
    }

    private Set<BusinessObject> getInputs(Function function, Map<FlowObject, List<Statement>> flowObjects) {
        Set<BusinessObject> businessObjects = new HashSet<>();

        for (Statement statement : flowObjects.get(new FlowObject(function.getResource())))
            if (statement.getPredicate().equals(repository.getRequires()))
                businessObjects.add(new BusinessObject((Resource) statement.getObject()));

        return businessObjects;
    }

    private Set<BusinessObject> getOutputs(Function function, Map<FlowObject, List<Statement>> flowObjects) {
        Set<BusinessObject> businessObjects = new HashSet<>();

        for (Statement statement : flowObjects.get(new FlowObject(function.getResource())))
            if (statement.getPredicate().equals(repository.getProduces()))
                businessObjects.add(new BusinessObject((Resource) statement.getObject()));

        return businessObjects;
    }
}
