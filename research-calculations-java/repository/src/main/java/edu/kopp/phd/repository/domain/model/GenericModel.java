package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

/**
 * Definition of a business process model.
 * Provides tools to create a business process models with a certain types of objects.
 *
 * @author Andrii Kopp
 */
public class GenericModel {
    private String name;
    private String granularity;

    // Mapping relations between similar business process models.
    private Map<GenericModel, Double> relations;

    // RDF graph that represents a business process model.
    private Model statements;

    // Namespaces of the corresponding resources, properties, and types.
    public static final String NS_RESOURCE = "http://www.repository.edu/resource#";
    public static final String NS_PROPERTY = "http://www.repository.edu/relation#";
    public static final String NS_TYPE = "http://www.repository.edu/type#";

    // Prepared properties that should be used to form RDF statements.
    protected Property a;
    protected Property isPredecessorOf;
    protected Property isComposedOf;
    protected Property isPerformedBy;
    protected Property isSupportedBy;
    protected Property requires;
    protected Property produces;
    protected Property isRegulatedBy;

    public GenericModel(String name) {
        this.name = name;
        this.relations = new HashMap<>();
        this.statements = ModelFactory.createDefaultModel();

        initProperties();
    }

    public GenericModel(String name, String granularity) {
        this.name = name;
        this.granularity = granularity;
        this.relations = new HashMap<>();
        this.statements = ModelFactory.createDefaultModel();

        initProperties();
    }

    public Resource createStartEvent(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "startEvent");
        return resource;
    }

    public Resource createIntermediateEvent(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "intermediateEvent");
        return resource;
    }

    public Resource createEndEvent(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "endEvent");
        return resource;
    }

    public Resource createAndSplitGateway(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "andSplit");
        return resource;
    }

    public Resource createAndJoinGateway(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "andJoin");
        return resource;
    }

    public Resource createOrSplitGateway(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "orSplit");
        return resource;
    }

    public Resource createOrJoinGateway(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "orJoin");
        return resource;
    }

    public Resource createXorSplitGateway(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "xorSplit");
        return resource;
    }

    public Resource createXorJoinGateway(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "xorJoin");
        return resource;
    }

    public Resource createFunction(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "function");
        return resource;
    }

    public Resource createProcess(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "process");
        return resource;
    }

    public Resource createDataStore(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "dataStore");
        return resource;
    }

    public Resource createExternalEntity(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "externalEntity");
        return resource;
    }

    public Resource createDepartment(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "department");
        return resource;
    }

    public Resource createPosition(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "position");
        return resource;
    }

    public Resource createApplicationSystem(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "applicationSystem");
        return resource;
    }

    public Resource createBusinessObject(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "businessObject");
        return resource;
    }

    public Resource createDataFlow(String name) {
        Resource resource = statements.createResource(NS_RESOURCE + name);
        statements.createStatement(resource, a, NS_TYPE + "dataFlow");
        return resource;
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
    }

    // Getters and setters section.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
