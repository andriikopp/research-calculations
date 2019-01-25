package edu.bpmanalysis.collection;

import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.collection.tools.ModelBuilder;
import edu.bpmanalysis.metamodel.Node;
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
                .addNode(Node.createFunction(2, 1, 1, 1))
                .addNode(Node.createFunction(1, 2, 2, 2))
                .addNode(Node.createFunction(0, 2, 1, 2))
                .addNode(Node.createFunction(2, 3, 1, 3))
                .addNode(Node.createFunction(3, 2, 1, 1))
                .addNode(Node.createFunction(1, 1, 1, 1))
                .addNode(Node.createInputInterface(1))
                .addNode(Node.createControlInterface(3))
                .addNode(Node.createControlInterface(1))
                .addNode(Node.createControlInterface(1))
                .addNode(Node.createControlInterface(1))
                .addNode(Node.createControlInterface(1))
                .addNode(Node.createOutputInterface(1))
                .addNode(Node.createMechanismInterface(3))
                .addNode(Node.createMechanismInterface(3))
                .addNode(Node.createMechanismInterface(1))
                .finish());

        models.add(ModelBuilder.selectModel(Model.createIDEF0Model("Model Integration"))
                .addNode(Node.createFunction(1, 3, 1, 1))
                .addNode(Node.createFunction(1, 2, 2, 1))
                .addNode(Node.createFunction(1, 2, 2, 2))
                .addNode(Node.createInputInterface(1))
                .addNode(Node.createControlInterface(1))
                .addNode(Node.createControlInterface(2))
                .addNode(Node.createControlInterface(2))
                .addNode(Node.createOutputInterface(1))
                .addNode(Node.createOutputInterface(2))
                .addNode(Node.createOutputInterface(2))
                .addNode(Node.createMechanismInterface(1))
                .addNode(Node.createMechanismInterface(2))
                .addNode(Node.createMechanismInterface(1))
                .finish());

        models.add(ModelBuilder.selectModel(Model.createIDEF0Model("Determine Groundwater"))
                .addNode(Node.createFunction(2, 1, 2, 3))
                .addNode(Node.createFunction(1, 1, 1, 4))
                .addNode(Node.createFunction(2, 1, 1, 1))
                .addNode(Node.createInputInterface(2))
                .addNode(Node.createControlInterface(3))
                .addNode(Node.createOutputInterface(1))
                .addNode(Node.createMechanismInterface(2))
                .addNode(Node.createMechanismInterface(3))
                .addNode(Node.createMechanismInterface(1))
                .addNode(Node.createMechanismInterface(1))
                .addNode(Node.createMechanismInterface(1))
                .finish());

        return models;
    }
}
