package main.java.edu.kopp.phd.repository.domain.model;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;
import main.java.edu.kopp.phd.repository.domain.process.GenericProcess;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Definition of a business process model.
 * Provides tools to create a business process models with a certain types of objects.
 *
 * @author Andrii Kopp
 */
public abstract class GenericModel {
    private String name;
    private Resource model;
    private Resource process;
    private Granularity granularity;

    // Mapping relations between similar business process models.
    private Map<GenericModel, Double> relations;

    // RDF graph that represents a business process model.
    private Model statements;

    // Namespaces of the corresponding resources, properties, and types.
    public static final String NS = "http://www.bpmodel.edu/";
    public static final String NS_RESOURCE = NS + "resource#";
    public static final String NS_PROPERTY = NS + "relation#";
    public static final String NS_TYPE = NS + "type#";

    // Prepared properties that should be used to form RDF statements.
    protected Property a;
    protected Property isPredecessorOf;
    protected Property isComposedOf;
    protected Property isPerformedBy;
    protected Property isSupportedBy;
    protected Property requires;
    protected Property produces;
    protected Property isRegulatedBy;
    protected Property hasLabel;

    // Properties to serialize models using Apache Jena TDB.
    protected Property nameIs;
    protected Property processIs;
    protected Property granularityIs;
    protected Property notationIs;
    protected Property categoryIs;

    // Counts resources of a certain model.
    private int resourceCounter = 1;

    private boolean isNotationDefined;

    protected GenericModel(String name, GenericProcess process) {
        this.name = name;
        this.relations = new HashMap<>();
        this.statements = ModelFactory.createDefaultModel();
        this.process = statements.createResource(NS_RESOURCE + trimURI(process.getName()));

        initProperties();
        initModel();
        initProcess(process);
    }

    protected GenericModel(String name, GenericProcess process, Granularity granularity) {
        this.name = name;
        this.granularity = granularity;
        this.relations = new HashMap<>();
        this.statements = ModelFactory.createDefaultModel();
        this.process = statements.createResource(NS_RESOURCE + trimURI(process.getName()));

        initProperties();
        initModel();
        initProcess(process);
    }

    // Names of resource types.
    private static final String START_EVENT = "startEvent";
    private static final String END_EVENT = "endEvent";
    private static final String INTERMEDIATE_EVENT = "intermediateEvent";

    private static final String AND_SPLIT = "andSplit";
    private static final String AND_JOIN = "andJoin";
    private static final String OR_SPLIT = "orSplit";
    private static final String OR_JOIN = "orJoin";
    private static final String XOR_SPLIT = "xorSplit";
    private static final String XOR_JOIN = "xorJoin";

    public Resource createStartEvent(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + START_EVENT));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createStartEvent() {
        Resource resource = statements.createResource(getURI(START_EVENT));
        statements.add(statements.createStatement(resource, a, NS_TYPE + START_EVENT));
        statements.add(statements.createStatement(resource, hasLabel, START_EVENT));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createIntermediateEvent(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + INTERMEDIATE_EVENT));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createIntermediateEvent() {
        Resource resource = statements.createResource(getURI(INTERMEDIATE_EVENT));
        statements.add(statements.createStatement(resource, a, NS_TYPE + INTERMEDIATE_EVENT));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createEndEvent(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + END_EVENT));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createEndEvent() {
        Resource resource = statements.createResource(getURI(END_EVENT));
        statements.add(statements.createStatement(resource, a, NS_TYPE + END_EVENT));
        statements.add(statements.createStatement(resource, hasLabel, END_EVENT));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createAndSplitGateway() {
        Resource resource = statements.createResource(getURI(AND_SPLIT));
        statements.add(statements.createStatement(resource, a, NS_TYPE + AND_SPLIT));
        statements.add(statements.createStatement(resource, hasLabel, AND_SPLIT));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createAndJoinGateway() {
        Resource resource = statements.createResource(getURI(AND_JOIN));
        statements.add(statements.createStatement(resource, a, NS_TYPE + AND_JOIN));
        statements.add(statements.createStatement(resource, hasLabel, AND_JOIN));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createOrSplitGateway() {
        Resource resource = statements.createResource(getURI(OR_SPLIT));
        statements.add(statements.createStatement(resource, a, NS_TYPE + OR_SPLIT));
        statements.add(statements.createStatement(resource, hasLabel, OR_SPLIT));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createOrJoinGateway() {
        Resource resource = statements.createResource(getURI(OR_JOIN));
        statements.add(statements.createStatement(resource, a, NS_TYPE + OR_JOIN));
        statements.add(statements.createStatement(resource, hasLabel, OR_JOIN));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createXorSplitGateway() {
        Resource resource = statements.createResource(getURI(XOR_SPLIT));
        statements.add(statements.createStatement(resource, a, NS_TYPE + XOR_SPLIT));
        statements.add(statements.createStatement(resource, hasLabel, XOR_SPLIT));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createXorJoinGateway() {
        Resource resource = statements.createResource(getURI(XOR_JOIN));
        statements.add(statements.createStatement(resource, a, NS_TYPE + XOR_JOIN));
        statements.add(statements.createStatement(resource, hasLabel, XOR_JOIN));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createActivity(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "activity"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createProcess(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "process"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createDataStore(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "dataStore"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createExternalEntity(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "externalEntity"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createDepartment(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "department"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createPosition(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "position"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createApplicationSystem(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "applicationSystem"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createBusinessObject(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "businessObject"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createDataFlow(String label) {
        Resource resource = statements.createResource(getURI(label));
        statements.add(statements.createStatement(resource, a, NS_TYPE + "dataFlow"));
        statements.add(statements.createStatement(resource, hasLabel, label));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    /**
     * Create model using this and only this method.
     * This method defines business process modeling notation
     */
    public abstract GenericModel start();

    /**
     * Used to return the instance of a current model.
     *
     * @return
     */
    public GenericModel finish() {
        if (!isNotationDefined) {
            throw new RuntimeException("Use start() method to define model properly!");
        }

        return this;
    }

    private void initProperties() {
        a = statements.createProperty(NS_PROPERTY + "a");
        isPredecessorOf = statements.createProperty(NS_PROPERTY + "isPredecessorOf");
        isComposedOf = statements.createProperty(NS_PROPERTY + "isComposedOf");
        isPerformedBy = statements.createProperty(NS_PROPERTY + "isPerformedBy");
        isSupportedBy = statements.createProperty(NS_PROPERTY + "isSupportedBy");
        requires = statements.createProperty(NS_PROPERTY + "requires");
        produces = statements.createProperty(NS_PROPERTY + "produces");
        isRegulatedBy = statements.createProperty(NS_PROPERTY + "isRegulatedBy");
        hasLabel = statements.createProperty(NS_PROPERTY + "hasLabel");

        nameIs = statements.createProperty(NS_PROPERTY + "nameIs");
        processIs = statements.createProperty(NS_PROPERTY + "processIs");
        granularityIs = statements.createProperty(NS_PROPERTY + "granularityIs");
        notationIs = statements.createProperty(NS_PROPERTY + "notationIs");
        categoryIs = statements.createProperty(NS_PROPERTY + "categoryIs");
    }

    private void initModel() {
        model = statements.createResource(NS_RESOURCE + "model");
        statements.add(statements.createStatement(model, nameIs, name));
        statements.add(statements.createStatement(model, processIs, process));

        if (granularity != null) {
            statements.add(statements.createStatement(model, granularityIs, granularity.getName()));
        }
    }

    private void initProcess(GenericProcess process) {
        statements.add(statements.createStatement(this.process, a, NS_TYPE + "process"));
        statements.add(statements.createStatement(this.process, categoryIs, process.getCategory().getName()));
    }

    private String getURI() {
        String identifier = NS_RESOURCE + "n" + resourceCounter;
        resourceCounter++;
        return identifier;
    }

    private String getURI(String name) {
        return NS_RESOURCE + trimURI(name);
    }

    private String trimURI(String value) {
        return value.replaceAll("\\s+", "_");
    }

    protected void setupNotation(String notation) {
        statements.add(statements.createStatement(model, notationIs, notation));
        isNotationDefined = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericModel that = (GenericModel) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (granularity != null ? !granularity.equals(that.granularity) : that.granularity != null) return false;
        if (relations != null ? !relations.equals(that.relations) : that.relations != null) return false;
        return statements != null ? statements.equals(that.statements) : that.statements == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (granularity != null ? granularity.hashCode() : 0);
        result = 31 * result + (relations != null ? relations.hashCode() : 0);
        result = 31 * result + (statements != null ? statements.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GenericModel{" +
                "name='" + name + '\'' +
                ", granularity='" + granularity + '\'' +
                ", relations=" + relations +
                ", statements=" + statements +
                '}';
    }

    // Getters and setters section.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resource getModel() {
        return model;
    }

    public void setModel(Resource model) {
        this.model = model;
    }

    public Resource getProcess() {
        return process;
    }

    public void setProcess(Resource process) {
        this.process = process;
    }

    public Granularity getGranularity() {
        return granularity;
    }

    public void setGranularity(Granularity granularity) {
        this.granularity = granularity;
    }

    public Map<GenericModel, Double> getRelations() {
        return relations;
    }

    public void setRelations(Map<GenericModel, Double> relations) {
        this.relations = relations;
    }

    public Model getStatements() {
        return statements;
    }

    public void setStatements(Model statements) {
        this.statements = statements;
    }

    // Getters for properties.
    public Property getA() {
        return a;
    }

    public Property getIsPredecessorOf() {
        return isPredecessorOf;
    }

    public Property getIsComposedOf() {
        return isComposedOf;
    }

    public Property getIsPerformedBy() {
        return isPerformedBy;
    }

    public Property getIsSupportedBy() {
        return isSupportedBy;
    }

    public Property getRequires() {
        return requires;
    }

    public Property getProduces() {
        return produces;
    }

    public Property getIsRegulatedBy() {
        return isRegulatedBy;
    }

    public Property getHasLabel() {
        return hasLabel;
    }
}
