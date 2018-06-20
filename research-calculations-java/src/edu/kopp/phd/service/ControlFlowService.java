package edu.kopp.phd.service;

import edu.kopp.phd.model.environment.ApplicationSystem;
import edu.kopp.phd.model.environment.BusinessObject;
import edu.kopp.phd.model.environment.OrganizationalUnit;
import edu.kopp.phd.model.flow.FlowObject;
import edu.kopp.phd.model.flow.Function;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import edu.kopp.phd.repository.api.Repository;
import edu.kopp.phd.service.api.Service;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.*;

/**
 * Provides functions used to retrieve process flow information.
 */
public class ControlFlowService implements Service {
    private RDFRepository repository = RDFRepository.getInstance();

    @Override
    public Repository getRepository() {
        return repository;
    }

    /**
     * Returns a list of process functions.
     *
     * @param processName - a name of the process.
     * @return the list of process functions.
     */
    public List<Function> getDetailedFunctionsByProcessName(String processName) {
        List<Function> processEnvironmentFunctions = new ArrayList<>();

        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        repository.load();

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
