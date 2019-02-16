package edu.bpmanalysis.collection;

import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.description.tools.Model;

import java.util.ArrayList;
import java.util.List;

import edu.bpmanalysis.description.tools.Node;
import edu.kopp.phd.express.landscape.ARISLandscape;
import edu.kopp.phd.express.metamodel.entity.Connector;

public class EPCModels implements Models {

    @Override
    public List<Model> importModels() {
        List<Model> models = new ArrayList<>();

        for (edu.kopp.phd.express.metamodel.Model model : new ARISLandscape().getGovernanceLog().getLandscape()) {
            Model importedModel = Model.createEPCModel(model.getName());

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

    public static void main(String[] args) {
        Models models = new EPCModels();

        for (Model model : models.importModels()) {
            System.out.println(model.getName());
            System.out.println(model.getModelType());

            for (Node node : model.getNodesList()) {
                System.out.println(node);
            }
        }
    }
}
