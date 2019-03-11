package edu.bpmanalysis.collection;

import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.ModelBuilder;
import edu.bpmanalysis.description.tools.Node;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class IDEF0Models implements Models {

    @Override
    public List<Model> importModels() {
        throw new NotImplementedException();
    }

    @Override
    public List<Model> loadModels() {
        List<Model> models = new ArrayList<>();

        models.add(ModelBuilder.selectModel(Model.createIDEF0Model("Application Development"))
                .addNode(Node.createInterfaceArcInput(1))

                .addNode(Node.createInterfaceArcControl(3))
                .addNode(Node.createInterfaceArcControl(1))
                .addNode(Node.createInterfaceArcControl(1))
                .addNode(Node.createInterfaceArcControl(1))
                .addNode(Node.createInterfaceArcControl(1))

                .addNode(Node.createInterfaceArcOutput(1))

                .addNode(Node.createInterfaceArcMechanism(3))
                .addNode(Node.createInterfaceArcMechanism(3))
                .addNode(Node.createInterfaceArcMechanism(1))

                .addNode(Node.createFunction(2, 1, 1, 1))
                .addNode(Node.createFunction(1, 2, 2, 2))
                .addNode(Node.createFunction(0, 2, 1, 2))
                .addNode(Node.createFunction(2, 3, 1, 3))
                .addNode(Node.createFunction(3, 2, 1, 1))
                .addNode(Node.createFunction(1, 1, 1, 1))
                .finish());

        models.add(ModelBuilder.selectModel(Model.createIDEF0Model("Model Integration"))
                .addNode(Node.createInterfaceArcInput(1))

                .addNode(Node.createInterfaceArcControl(1))
                .addNode(Node.createInterfaceArcControl(2))
                .addNode(Node.createInterfaceArcControl(2))

                .addNode(Node.createInterfaceArcOutput(1))
                .addNode(Node.createInterfaceArcOutput(2))
                .addNode(Node.createInterfaceArcOutput(2))

                .addNode(Node.createInterfaceArcMechanism(2))
                .addNode(Node.createInterfaceArcMechanism(1))
                .addNode(Node.createInterfaceArcMechanism(1))

                .addNode(Node.createFunction(1, 3, 1, 1))
                .addNode(Node.createFunction(1, 2, 2, 1))
                .addNode(Node.createFunction(1, 2, 2, 2))
                .finish());

        models.add(ModelBuilder.selectModel(Model.createIDEF0Model("Determine Groundwater"))
                .addNode(Node.createInterfaceArcInput(3))

                .addNode(Node.createInterfaceArcControl(3))

                .addNode(Node.createInterfaceArcOutput(1))

                .addNode(Node.createInterfaceArcMechanism(2))
                .addNode(Node.createInterfaceArcMechanism(3))
                .addNode(Node.createInterfaceArcMechanism(1))
                .addNode(Node.createInterfaceArcMechanism(1))
                .addNode(Node.createInterfaceArcMechanism(1))

                .addNode(Node.createFunction(2, 1, 2, 3))
                .addNode(Node.createFunction(1, 1, 1, 4))
                .addNode(Node.createFunction(2, 1, 1, 1))
                .finish());

        return models;
    }
}
