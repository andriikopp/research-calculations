package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.*;

import java.util.*;

/**
 * Definition of a business process model.
 * Provides tools to create a business process models with a certain types of objects.
 *
 * @author Andrii Kopp
 */
public abstract class GenericModel {
    private String name;
    private Resource process;
    private String granularity;

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

    // Counts resources of a certain model.
    private int resourceCounter = 1;

    protected GenericModel(String name, String process) {
        this.name = name;
        this.relations = new HashMap<>();
        this.statements = ModelFactory.createDefaultModel();
        this.process = statements.createResource(NS_RESOURCE + trimURI(process));

        initProperties();
        initModel();
    }

    protected GenericModel(String name, String process, String granularity) {
        this.name = name;
        this.granularity = granularity;
        this.relations = new HashMap<>();
        this.statements = ModelFactory.createDefaultModel();
        this.process = statements.createResource(NS_RESOURCE + trimURI(process));

        initProperties();
        initModel();
    }

    public Resource createStartEvent(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "startEvent"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createIntermediateEvent(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "intermediateEvent"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createEndEvent(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "endEvent"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createAndSplitGateway(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "andSplit"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createAndJoinGateway(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "andJoin"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createOrSplitGateway(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "orSplit"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createOrJoinGateway(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "orJoin"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createXorSplitGateway(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "xorSplit"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createXorJoinGateway(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "xorJoin"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createFunction(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "function"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createProcess(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "process"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createDataStore(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "dataStore"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createExternalEntity(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "externalEntity"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createDepartment(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "department"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createPosition(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "position"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createApplicationSystem(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "applicationSystem"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createBusinessObject(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "businessObject"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    public Resource createDataFlow(String name) {
        Resource resource = statements.createResource(getURI());
        statements.add(statements.createStatement(resource, a, NS_TYPE + "dataFlow"));
        statements.add(statements.createStatement(resource, hasLabel, name));
        statements.add(statements.createStatement(process, isComposedOf, resource));
        return resource;
    }

    /**
     * Used to return the instance of a current model.
     *
     * @return
     */
    public GenericModel finish() {
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
    }

    private void initModel() {
        Resource model = statements.createResource(NS_RESOURCE + "model");
        statements.add(statements.createStatement(model, nameIs, name));
        statements.add(statements.createStatement(model, processIs, process));

        if (granularity != null) {
            statements.add(statements.createStatement(model, granularityIs, granularity));
        }
    }

    private String getURI() {
        String identifier = NS_RESOURCE + "n" + resourceCounter;
        resourceCounter++;
        return identifier;
    }

    private String trimURI(String value) {
        return value.replaceAll("\\s+", "_");
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

    public Resource getProcess() {
        return process;
    }

    public void setProcess(Resource process) {
        this.process = process;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
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
