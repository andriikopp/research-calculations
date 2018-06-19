package bp.analysis.aris.model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Description of ARIS extended Event-driven Process Chain (eEPC) model.
 *
 * @author Andrii Kopp
 */
public class ARISeEPCBusinessModel {
    public static final String RDF_NS_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final String TYPE = "type";
    public static final String IS_PREDECESSOR_OF = "isPredecessorOf";
    public static final String IS_PERFORMED_BY = "isPerformedBy";
    public static final String IS_SUPPORTED_BY = "isSupportedBy";
    public static final String REQUIRES = "requires";
    public static final String PRODUCES = "produces";
    public static final String IS_COMPOSED_BY = "isComposedBy";

    public static final String EVENT = "event";
    public static final String FUNCTION = "function";
    public static final String PROCESS = "process";
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String XOR = "xor";
    public static final String ORGANIZATIONAL_UNIT = "organizationalUnit";
    public static final String POSITION = "position";
    public static final String APPLICATION_SYSTEM = "applicationSystem";
    public static final String MATERIAL = "material";
    public static final String INFORMATION = "information";

    private Model knowledgeModel;

    private Set<ARISeEPCEvent> events;
    private Set<ARISeEPCFunction> functions;
    private Set<ARISeEPCGateway> gateways;
    private Set<ARISeEPCProcess> processes;

    private Set<ARISeEPCOrganizationalUnit> organizationalUnits;
    private Set<ARISeEPCApplicationSystem> applicationSystems;
    private Set<ARISeEPCBusinessObject> businessObjects;

    private ARISeEPCBusinessModel() {
        this.knowledgeModel = ModelFactory.createDefaultModel();

        this.events = new HashSet<>();
        this.functions = new HashSet<>();
        this.gateways = new HashSet<>();
        this.processes = new HashSet<>();

        this.organizationalUnits = new HashSet<>();
        this.applicationSystems = new HashSet<>();
        this.businessObjects = new HashSet<>();
    }

    public static ARISeEPCBusinessModel createInstance() {
        return new ARISeEPCBusinessModel();
    }

    /* Public functions. */
    public void buildKnowledgeModel() {
        for (ARISeEPCEvent event : events)
            storeEvent(event);

        for (ARISeEPCFunction function : functions)
            storeFunction(function);

        for (ARISeEPCGateway gateway : gateways)
            storeGateway(gateway);

        for (ARISeEPCProcess process : processes)
            storeProcess(process);

        for (ARISeEPCOrganizationalUnit organizationalUnit : organizationalUnits)
            storeOrganizationalUnit(organizationalUnit);

        for (ARISeEPCApplicationSystem applicationSystem : applicationSystems)
            storeApplicationSystem(applicationSystem);

        for (ARISeEPCBusinessObject businessObject : businessObjects)
            storeBusinessObject(businessObject);
    }

    public void createControlFlow(ARISeEPCFlowObject flowObject, ARISeEPCFlowObject... isPredecessorOf) {
        if (flowObject == null) return;

        if (isPredecessorOf != null && isPredecessorOf.length != 0) {
            flowObject.isPredecessorOf(isPredecessorOf);

            for (ARISeEPCFlowObject node : isPredecessorOf)
                addFlowObject(node);
        }

        addFlowObject(flowObject);

        if (flowObject instanceof ARISeEPCFunction) {
            ARISeEPCFunction function = (ARISeEPCFunction) flowObject;

            if (function.getOrganizationalUnits() != null)
                organizationalUnits.addAll(Arrays.asList(function.getOrganizationalUnits()));

            if (function.getApplicationSystems() != null)
                applicationSystems.addAll(Arrays.asList(function.getApplicationSystems()));

            if (function.getInputs() != null)
                businessObjects.addAll(Arrays.asList(function.getInputs()));

            if (function.getOutputs() != null)
                businessObjects.addAll(Arrays.asList(function.getOutputs()));
        }

        if (flowObject instanceof ARISeEPCProcess) {
            ARISeEPCProcess process = (ARISeEPCProcess) flowObject;

            if (process.getFlowObjects() != null)
                for (ARISeEPCFlowObject node : process.getFlowObjects())
                    addFlowObject(node);
        }
    }

    public Set<ARISeEPCFlowObject> getRelatedNodes(ARISeEPCFlowObject flowObject) {
        Set<ARISeEPCFlowObject> result = new HashSet<>();

        for (ARISeEPCEvent event : events)
            if (event.getSubsequent() != null && Arrays.asList(event.getSubsequent()).contains(flowObject))
                result.add(event);

        for (ARISeEPCFunction function : functions)
            if (function.getSubsequent() != null && Arrays.asList(function.getSubsequent()).contains(flowObject))
                result.add(function);

        for (ARISeEPCGateway gateway : gateways)
            if (gateway.getSubsequent() != null && Arrays.asList(gateway.getSubsequent()).contains(flowObject))
                result.add(gateway);

        for (ARISeEPCProcess process : processes)
            if (process.getSubsequent() != null && Arrays.asList(process.getSubsequent()).contains(flowObject))
                result.add(process);

        if (flowObject.getSubsequent() != null)
            result.addAll(Arrays.asList(flowObject.getSubsequent()));

        return result;
    }

    public int countPreceding(ARISeEPCFlowObject flowObject) {
        int precedingNumber = 0;

        for (ARISeEPCFlowObject node : getFlowObjects())
            if (node.getSubsequent() != null && Arrays.asList(node.getSubsequent()).contains(flowObject))
                precedingNumber++;

        return precedingNumber;
    }

    public int countSubsequent(ARISeEPCFlowObject flowObject) {
        if (flowObject.getSubsequent() == null)
            return 0;

        return flowObject.getSubsequent().length;
    }

    public int countOrganizationalUnits(ARISeEPCFunction function) {
        if (function.getOrganizationalUnits() == null)
            return 0;

        return function.getOrganizationalUnits().length;
    }

    public int countInputs(ARISeEPCFunction function) {
        if (function.getInputs() == null)
            return 0;

        return function.getInputs().length;
    }

    public int countOutputs(ARISeEPCFunction function) {
        if (function.getOutputs() == null)
            return 0;

        return function.getOutputs().length;
    }

    public int countApplicationSystems(ARISeEPCFunction function) {
        if (function.getApplicationSystems() == null)
            return 0;

        return function.getApplicationSystems().length;
    }

    /* Private functions. */
    private void addFlowObject(ARISeEPCFlowObject flowObject) {
        if (flowObject == null) return;

        if (flowObject instanceof ARISeEPCEvent)
            events.add((ARISeEPCEvent) flowObject);

        if (flowObject instanceof ARISeEPCFunction)
            functions.add((ARISeEPCFunction) flowObject);

        if (flowObject instanceof ARISeEPCGateway)
            gateways.add((ARISeEPCGateway) flowObject);

        if (flowObject instanceof ARISeEPCProcess)
            processes.add((ARISeEPCProcess) flowObject);
    }

    private void addStatement(String subjectURI, String predicateURI, String objectURI) {
        Resource subject = knowledgeModel.createResource(RDF_NS_PREFIX + subjectURI
                .toLowerCase().replaceAll("\\s+", "_"));
        Property predicate = knowledgeModel.createProperty(RDF_NS_PREFIX + predicateURI
                .toLowerCase().replaceAll("\\s+", "_"));
        Resource object = knowledgeModel.createResource(RDF_NS_PREFIX + objectURI
                .toLowerCase().replaceAll("\\s+", "_"));

        knowledgeModel.add(knowledgeModel.createStatement(subject, predicate, object));
    }

    private void storeEvent(ARISeEPCEvent event) {
        addStatement(event.getName(), TYPE, EVENT);

        if (event.getSubsequent() != null)
            for (ARISeEPCFlowObject flowObject : event.getSubsequent())
                addStatement(event.getName(), IS_PREDECESSOR_OF, flowObject.getName());
    }

    private void storeFunction(ARISeEPCFunction function) {
        addStatement(function.getName(), TYPE, FUNCTION);

        if (function.getSubsequent() != null)
            for (ARISeEPCFlowObject flowObject : function.getSubsequent())
                addStatement(function.getName(), IS_PREDECESSOR_OF, flowObject.getName());

        if (function.getOrganizationalUnits() != null)
            for (ARISeEPCOrganizationalUnit organizationalUnit : function.getOrganizationalUnits())
                addStatement(function.getName(), IS_PERFORMED_BY, organizationalUnit.getName());

        if (function.getApplicationSystems() != null)
            for (ARISeEPCApplicationSystem applicationSystem : function.getApplicationSystems())
                addStatement(function.getName(), IS_SUPPORTED_BY, applicationSystem.getName());

        if (function.getInputs() != null)
            for (ARISeEPCBusinessObject businessObject : function.getInputs())
                addStatement(function.getName(), REQUIRES, businessObject.getName());

        if (function.getOutputs() != null)
            for (ARISeEPCBusinessObject businessObject : function.getOutputs())
                addStatement(function.getName(), PRODUCES, businessObject.getName());
    }

    private void storeGateway(ARISeEPCGateway gateway) {
        if (gateway instanceof ARISeEPCGateway.ARISeEPCAndGateway)
            addStatement(gateway.getName(), TYPE, AND);

        if (gateway instanceof ARISeEPCGateway.ARISeEPCOrGateway)
            addStatement(gateway.getName(), TYPE, OR);

        if (gateway instanceof ARISeEPCGateway.ARISeEPCXorGateway)
            addStatement(gateway.getName(), TYPE, XOR);

        if (gateway.getSubsequent() != null)
            for (ARISeEPCFlowObject flowObject : gateway.getSubsequent())
                addStatement(gateway.getName(), IS_PREDECESSOR_OF, flowObject.getName());
    }

    private void storeProcess(ARISeEPCProcess process) {
        addStatement(process.getName(), TYPE, PROCESS);

        if (process.getFlowObjects() != null)
            for (ARISeEPCFlowObject flowObject : process.getFlowObjects())
                addStatement(process.getName(), IS_COMPOSED_BY, flowObject.getName());
    }

    private void storeOrganizationalUnit(ARISeEPCOrganizationalUnit organizationalUnit) {
        if (organizationalUnit instanceof ARISeEPCOrganizationalUnit.ARISeEPCPosition)
            addStatement(organizationalUnit.getName(), TYPE, POSITION);
        else
            addStatement(organizationalUnit.getName(), TYPE, ORGANIZATIONAL_UNIT);
    }

    private void storeApplicationSystem(ARISeEPCApplicationSystem applicationSystem) {
        addStatement(applicationSystem.getName(), TYPE, APPLICATION_SYSTEM);
    }

    private void storeBusinessObject(ARISeEPCBusinessObject businessObject) {
        if (businessObject instanceof ARISeEPCBusinessObject.ARISeEPCMaterial)
            addStatement(businessObject.getName(), TYPE, MATERIAL);
        else
            addStatement(businessObject.getName(), TYPE, INFORMATION);
    }

    /* Getters. */
    public Model getKnowledgeModel() {
        return knowledgeModel;
    }

    public Set<ARISeEPCFlowObject> getFlowObjects() {
        Set<ARISeEPCFlowObject> flowObjects = new HashSet<>();

        flowObjects.addAll(events);
        flowObjects.addAll(functions);
        flowObjects.addAll(processes);
        flowObjects.addAll(gateways);

        return flowObjects;
    }

    public Set<ARISeEPCEvent> getEvents() {
        return events;
    }

    public Set<ARISeEPCFunction> getFunctions() {
        return functions;
    }

    public Set<ARISeEPCGateway> getGateways() {
        return gateways;
    }

    public Set<ARISeEPCProcess> getProcesses() {
        return processes;
    }

    public Set<ARISeEPCOrganizationalUnit> getOrganizationalUnits() {
        return organizationalUnits;
    }

    public Set<ARISeEPCApplicationSystem> getApplicationSystems() {
        return applicationSystems;
    }

    public Set<ARISeEPCBusinessObject> getBusinessObjects() {
        return businessObjects;
    }
}
