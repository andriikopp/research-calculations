package edu.bpmanalysis.collection;

import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.collection.tools.ModelBuilder;
import edu.bpmanalysis.metamodel.Node;
import edu.kopp.phd.express.landscape.BPMNLandscape;
import edu.kopp.phd.express.metamodel.entity.Connector;

import java.util.ArrayList;
import java.util.List;

public class BPMNModels implements Models {

    @Override
    public List<Model> importModels() {
        List<Model> models = new ArrayList<>();

        for (edu.kopp.phd.express.metamodel.Model model : new BPMNLandscape().getGovernanceLog().getLandscape()) {
            Model importedModel = Model.createBPMNModel(model.getName());

            for (edu.kopp.phd.express.metamodel.entity.Node node : model.getNodes()) {
                if (node instanceof edu.kopp.phd.express.metamodel.entity.Function) {
                    importedModel.getNodesList().add(Node.createFunction(node.getLabel(),
                            node.getPreceding(), node.getSubsequent(),0, 0));
                }

                if (node instanceof edu.kopp.phd.express.metamodel.entity.Event) {
                    importedModel.getNodesList().add(Node.createEvent(node.getLabel(),
                            node.getPreceding(), node.getSubsequent()));
                }

                if (node instanceof edu.kopp.phd.express.metamodel.entity.Connector) {
                    edu.kopp.phd.express.metamodel.entity.Connector connector =
                            (edu.kopp.phd.express.metamodel.entity.Connector) node;

                    if (connector.getLogic().equals(Connector.XOR)) {
                        importedModel.getNodesList().add(Node.createXORConnector(node.getLabel(),
                                node.getPreceding(), node.getSubsequent()));
                    }

                    if (connector.getLogic().equals(Connector.OR)) {
                        importedModel.getNodesList().add(Node.createORConnector(node.getLabel(),
                                node.getPreceding(), node.getSubsequent()));
                    }

                    if (connector.getLogic().equals(Connector.AND)) {
                        importedModel.getNodesList().add(Node.createANDConnector(node.getLabel(),
                                node.getPreceding(), node.getSubsequent()));
                    }
                }
            }

            models.add(importedModel);
        }

        return models;
    }

    @Override
    public List<Model> loadModels() {
        List<Model> models = new ArrayList<>();

        models.add(ModelBuilder.selectModel(Model.createBPMNModel("Cab Booking Process"))
                .addNode(Node.createFunction(0, 1, 0, 0))
                .addNode(Node.createEvent(1, 0))
                .addNode(Node.createEvent(0, 1))
                .addNode(Node.createFunction(1, 1, 0, 0))
                .addNode(Node.createFunction(1, 1, 0, 0))
                .addNode(Node.createXORConnector(1, 2))
                .addNode(Node.createFunction(1, 1, 0, 0))
                .addNode(Node.createFunction(2, 1, 0, 0))
                .addNode(Node.createXORConnector(1, 2))
                .addNode(Node.createEvent(1, 0))
                .addNode(Node.createFunction(1, 1, 0, 0))
                .addNode(Node.createEvent(1, 1))
                .addNode(Node.createEvent(0, 1))
                .addNode(Node.createFunction(0, 1, 0, 0))
                .addNode(Node.createFunction(1, 1, 0, 0))
                .addNode(Node.createEvent(1, 0))
                .finish());

        return models;
    }

    public static void main(String[] args) {
        Models models = new BPMNModels();

        for (Model model : models.importModels()) {
            System.out.println(model.getName());
            System.out.println(model.getModelType());

            for (Node node : model.getNodesList()) {
                System.out.println(node);
            }
        }
    }
}
