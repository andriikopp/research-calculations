package edu.kopp.phd.repository.api;

import edu.kopp.phd.model.Node;
import edu.kopp.phd.model.environment.*;
import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import org.apache.jena.rdf.model.Statement;

import java.util.*;

public interface Repository {

    Event createEvent(String name, Process process);

    Function createFunction(String name, Process process);

    AndGateway createAndGateway(Process process);

    OrGateway createOrGateway(Process process);

    XOrGateway createXOrGateway(Process process);

    AndGateway createAndGateway(String id, Process process);

    OrGateway createOrGateway(String id, Process process);

    XOrGateway createXOrGateway(String id, Process process);

    Process createProcess(String name);

    Process createProcessInterface(String name, Process process);

    OrganizationalUnit createOrganizationalUnit(String name);

    Position createPosition(String name);

    ApplicationSystem createApplicationSystem(String name);

    BusinessObject createBusinessObject(String name);

    Material createMaterial(String name);

    Information createInformation(String name);

    void createControlFlow(FlowObject preceding, FlowObject... subsequent);

    void createResourceFlow(OrganizationalUnit organizationalUnit, Function function);

    void createOutputFlow(Function function, BusinessObject businessObject);

    void createInputFlow(BusinessObject businessObject, Function function);

    void assignOrganizationalUnits(Function function, OrganizationalUnit... organizationalUnits);

    void assignApplicationSystems(Function function, ApplicationSystem... applicationSystems);

    void assignInputs(Function function, BusinessObject... businessObjects);

    void assignOutputs(Function function, BusinessObject... businessObjects);

    List<Statement> retrieve(Node node);

    Map<FlowObject, List<Statement>> retrieveProcess(Process process);

    void store();

    void load();
}
