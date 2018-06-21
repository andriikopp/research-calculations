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

    Gateway createAndGateway(Process process);

    Gateway createOrGateway(Process process);

    Gateway createXOrGateway(Process process);

    Process createProcess(String name);

    Process createProcessInterface(String name, Process process);

    OrganizationalUnit createOrganizationalUnit(String name);

    Position createPosition(String name);

    ApplicationSystem createApplicationSystem(String name);

    Material createMaterial(String name);

    Information createInformation(String name);

    void createControlFlow(FlowObject preceding, FlowObject... subsequent);

    void assignOrganizationalUnits(Function function, OrganizationalUnit... organizationalUnits);

    void assignApplicationSystems(Function function, ApplicationSystem... applicationSystems);

    void assignInputs(Function function, BusinessObject... businessObjects);

    void assignOutputs(Function function, BusinessObject... businessObjects);

    List<Statement> retrieve(Node node);

    Map<FlowObject, List<Statement>> retrieveProcess(Process process);

    void store();

    void load();
}
