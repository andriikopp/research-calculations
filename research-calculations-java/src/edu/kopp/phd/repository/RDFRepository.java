package edu.kopp.phd.repository;

import edu.kopp.phd.model.Node;
import edu.kopp.phd.model.environment.*;
import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.api.Repository;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RDFRepository implements Repository {
    public static final String NS_REPOSITORY = "http://www.repository.edu/";

    public static final String NS_REPOSITORY_RELATION = "http://www.repository.edu/relation#";
    public static final String NS_REPOSITORY_TYPE = "http://www.repository.edu/type#";

    public static final String REPOSITORY_PATH = "portal/repository.xml";

    private Model model;

    private Property a;
    private Property isPredecessorOf;
    private Property isComposedBy;
    private Property isPerformedBy;
    private Property isSupportedBy;
    private Property requires;
    private Property produces;

    private Resource event;
    private Resource function;
    private Resource andGateway;
    private Resource orGateway;
    private Resource xOrGateway;
    private Resource process;

    private Resource organizationalUnit;
    private Resource position;
    private Resource applicationSystem;
    private Resource businessObject;
    private Resource material;
    private Resource information;

    private static final RDFRepository INSTANCE = new RDFRepository();

    private RDFRepository() {
        this.model = ModelFactory.createDefaultModel();

        a = model.createProperty(NS_REPOSITORY_RELATION + "a");
        isPredecessorOf = model.createProperty(NS_REPOSITORY_RELATION + "isPredecessorOf");
        isComposedBy = model.createProperty(NS_REPOSITORY_RELATION + "isComposedBy");
        isPerformedBy = model.createProperty(NS_REPOSITORY_RELATION + "isPerformedBy");
        isSupportedBy = model.createProperty(NS_REPOSITORY_RELATION + "isSupportedBy");
        requires = model.createProperty(NS_REPOSITORY_RELATION + "requires");
        produces = model.createProperty(NS_REPOSITORY_RELATION + "produces");

        event = model.createResource(NS_REPOSITORY_TYPE + "Event");
        function = model.createResource(NS_REPOSITORY_TYPE + "Function");
        andGateway = model.createResource(NS_REPOSITORY_TYPE + "AND");
        orGateway = model.createResource(NS_REPOSITORY_TYPE + "OR");
        xOrGateway = model.createResource(NS_REPOSITORY_TYPE + "XOR");
        process = model.createResource(NS_REPOSITORY_TYPE + "Process");

        organizationalUnit = model.createResource(NS_REPOSITORY_TYPE + "OrganizationalUnit");
        position = model.createResource(NS_REPOSITORY_TYPE + "Position");
        applicationSystem = model.createResource(NS_REPOSITORY_TYPE + "ApplicationSystem");
        businessObject = model.createResource(NS_REPOSITORY_TYPE + "BusinessObject");
        material = model.createResource(NS_REPOSITORY_TYPE + "Material");
        information = model.createResource(NS_REPOSITORY_TYPE + "Information");
    }

    public static RDFRepository getInstance() {
        return INSTANCE;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public Event createEvent(String name, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/" +
                name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, event));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new Event(subject);
    }

    @Override
    public Function createFunction(String name, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/"
                + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, function));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new Function(subject);
    }

    @Override
    public AndGateway createAndGateway(Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/and/AND"
                + (getNumberOfGatewaysByProcess(process) + 1));

        model.add(model.createStatement(subject, a, andGateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new AndGateway(subject);
    }

    @Override
    public OrGateway createOrGateway(Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/or/OR"
                + (getNumberOfGatewaysByProcess(process) + 1));

        model.add(model.createStatement(subject, a, orGateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new OrGateway(subject);
    }

    @Override
    public XOrGateway createXOrGateway(Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/xor/XOR"
                + (getNumberOfGatewaysByProcess(process) + 1));

        model.add(model.createStatement(subject, a, xOrGateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new XOrGateway(subject);
    }

    @Override
    public AndGateway createAndGateway(String id, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/and/AND"
                + id);

        model.add(model.createStatement(subject, a, andGateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new AndGateway(subject);
    }

    @Override
    public OrGateway createOrGateway(String id, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/or/OR"
                + id);

        model.add(model.createStatement(subject, a, orGateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new OrGateway(subject);
    }

    @Override
    public XOrGateway createXOrGateway(String id, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/xor/XOR"
                + id);

        model.add(model.createStatement(subject, a, xOrGateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new XOrGateway(subject);
    }

    @Override
    public Process createProcess(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, process));

        return new Process(subject);
    }

    @Override
    public Process createProcessInterface(String name, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + process.getResource().getLocalName() + "/interface#"
                + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, this.process));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new Process(subject);
    }

    @Override
    public OrganizationalUnit createOrganizationalUnit(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, organizationalUnit));

        return new OrganizationalUnit(subject);
    }

    @Override
    public Position createPosition(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, position));

        return new Position(subject);
    }

    @Override
    public ApplicationSystem createApplicationSystem(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, applicationSystem));

        return new ApplicationSystem(subject);
    }

    @Override
    public BusinessObject createBusinessObject(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, businessObject));

        return new BusinessObject(subject);
    }

    @Override
    public Material createMaterial(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, material));

        return new Material(subject);
    }

    @Override
    public Information createInformation(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, information));

        return new Information(subject);
    }

    @Override
    public void createControlFlow(FlowObject preceding, FlowObject... subsequent) {
        for (FlowObject flowObject : subsequent)
            model.add(model.createStatement(preceding.getResource(), isPredecessorOf, flowObject.getResource()));
    }

    @Override
    public void createResourceFlow(OrganizationalUnit organizationalUnit, Function function) {
        model.add(model.createStatement(function.getResource(), isPerformedBy, organizationalUnit.getResource()));
    }

    @Override
    public void createOutputFlow(Function function, BusinessObject businessObject) {
        model.add(model.createStatement(function.getResource(), produces, businessObject.getResource()));
    }

    @Override
    public void createInputFlow(BusinessObject businessObject, Function function) {
        model.add(model.createStatement(function.getResource(), requires, businessObject.getResource()));
    }

    @Override
    public void assignOrganizationalUnits(Function function, OrganizationalUnit... organizationalUnits) {
        for (OrganizationalUnit organizationalUnit : organizationalUnits)
            model.add(model.createStatement(function.getResource(), isPerformedBy, organizationalUnit.getResource()));
    }

    @Override
    public void assignApplicationSystems(Function function, ApplicationSystem... applicationSystems) {
        for (ApplicationSystem applicationSystem : applicationSystems)
            model.add(model.createStatement(function.getResource(), isSupportedBy, applicationSystem.getResource()));
    }

    @Override
    public void assignInputs(Function function, BusinessObject... businessObjects) {
        for (BusinessObject businessObject : businessObjects)
            model.add(model.createStatement(function.getResource(), requires, businessObject.getResource()));
    }

    @Override
    public void assignOutputs(Function function, BusinessObject... businessObjects) {
        for (BusinessObject businessObject : businessObjects)
            model.add(model.createStatement(function.getResource(), produces, businessObject.getResource()));
    }

    @Override
    public List<Statement> retrieve(Node node) {
        List<Statement> list = new ArrayList<>();

        StmtIterator iterator = model.listStatements();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();

            if (statement.getSubject().equals(node.getResource()))
                list.add(statement);
        }

        return list;
    }

    @Override
    public Map<FlowObject, List<Statement>> retrieveProcess(Process process) {
        Map<FlowObject, List<Statement>> flowObjects = new HashMap<>();

        for (Statement statement : retrieve(process)) {
            if (statement.getPredicate().equals(isComposedBy)) {
                FlowObject flowObject = new FlowObject((Resource) statement.getObject());

                List<Statement> statements = new ArrayList<>();

                for (Statement flowObjectStatement : retrieve(flowObject))
                    statements.add(flowObjectStatement);

                flowObjects.put(flowObject, statements);
            }
        }

        return flowObjects;
    }

    @Override
    public void store() {
        try (OutputStream outputStream = new FileOutputStream(REPOSITORY_PATH)) {
            model.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load() {
        InputStream inputStream = FileManager.get().open(REPOSITORY_PATH);

        model.read(inputStream, null);
    }

    private int getNumberOfGatewaysByProcess(Process process) {
        int count = 0;

        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.nextStatement();

            if (statement.getSubject().equals(process.getResource()) &&
                    statement.getPredicate().equals(isComposedBy) &&
                    (((Resource)statement.getObject()).getURI().contains(NS_REPOSITORY +
                            process.getResource().getLocalName() + "/and") ||
                            ((Resource)statement.getObject()).getURI().contains(NS_REPOSITORY +
                                    process.getResource().getLocalName() + "/or") ||
                            ((Resource)statement.getObject()).getURI().contains(NS_REPOSITORY +
                                    process.getResource().getLocalName() + "/xor")))
                count++;
        }

        return count;
    }

    public Property getA() {
        return a;
    }

    public Property getIsPredecessorOf() {
        return isPredecessorOf;
    }

    public Property getIsComposedBy() {
        return isComposedBy;
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

    public Resource getEvent() {
        return event;
    }

    public Resource getFunction() {
        return function;
    }

    public Resource getAndGateway() {
        return andGateway;
    }

    public Resource getOrGateway() {
        return orGateway;
    }

    public Resource getxOrGateway() {
        return xOrGateway;
    }

    public Resource getProcess() {
        return process;
    }

    public Resource getOrganizationalUnit() {
        return organizationalUnit;
    }

    public Resource getPosition() {
        return position;
    }

    public Resource getApplicationSystem() {
        return applicationSystem;
    }

    public Resource getMaterial() {
        return material;
    }

    public Resource getInformation() {
        return information;
    }

    public Resource getBusinessObject() { return businessObject; }
}
