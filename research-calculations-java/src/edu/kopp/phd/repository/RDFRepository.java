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

    public static final String REPOSITORY_PATH = "repository.xml";

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
    private Resource gateway;
    private Resource process;

    private Resource organizationalUnit;
    private Resource position;
    private Resource applicationSystem;
    private Resource material;
    private Resource information;

    private Resource andGateway;
    private Resource orGateway;
    private Resource xOrGateway;

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
        gateway = model.createResource(NS_REPOSITORY_TYPE + "Gateway");
        process = model.createResource(NS_REPOSITORY_TYPE + "Process");

        organizationalUnit = model.createResource(NS_REPOSITORY_TYPE + "OrganizationalUnit");
        position = model.createResource(NS_REPOSITORY_TYPE + "Position");
        applicationSystem = model.createResource(NS_REPOSITORY_TYPE + "ApplicationSystem");
        material = model.createResource(NS_REPOSITORY_TYPE + "Material");
        information = model.createResource(NS_REPOSITORY_TYPE + "Information");

        andGateway = model.createResource(NS_REPOSITORY + "AND");
        orGateway = model.createResource(NS_REPOSITORY + "OR");
        xOrGateway = model.createResource(NS_REPOSITORY + "XOR");
    }

    public static RDFRepository getInstance() {
        return INSTANCE;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public Event createEvent(String name, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, event));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new Event(subject);
    }

    @Override
    public Function createFunction(String name, Process process) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, function));

        model.add(model.createStatement(process.getResource(), isComposedBy, subject));

        return new Function(subject);
    }

    @Override
    public Gateway createAndGateway(Process process) {
        model.add(model.createStatement(andGateway, a, gateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, andGateway));

        return new Gateway(andGateway);
    }

    @Override
    public Gateway createOrGateway(Process process) {
        model.add(model.createStatement(orGateway, a, gateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, orGateway));

        return new Gateway(orGateway);
    }

    @Override
    public Gateway createXOrGateway(Process process) {
        model.add(model.createStatement(xOrGateway, a, gateway));

        model.add(model.createStatement(process.getResource(), isComposedBy, xOrGateway));

        return new Gateway(xOrGateway);
    }

    @Override
    public Process createProcess(String name) {
        Resource subject = model.createResource(NS_REPOSITORY + name.replaceAll("\\s+", "_"));

        model.add(model.createStatement(subject, a, process));

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

    public Resource getGateway() {
        return gateway;
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

    public Resource getAndGateway() {
        return andGateway;
    }

    public Resource getOrGateway() {
        return orGateway;
    }

    public Resource getxOrGateway() {
        return xOrGateway;
    }
}
