package edu.bpmanalysis.collection;

import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.collection.tools.ModelBuilder;
import edu.bpmanalysis.metamodel.Node;
import edu.kopp.phd.express.landscape.DFDLandscape;

import java.util.ArrayList;
import java.util.List;

public class DFDModels implements Models {

    @Override
    public List<Model> importModels() {
        List<Model> models = new ArrayList<>();

        for (edu.kopp.phd.express.metamodel.Model model : new DFDLandscape().getGovernanceLog().getLandscape()) {
            Model importedModel = Model.createDFDModel(model.getName());

            for (edu.kopp.phd.express.metamodel.entity.Node node : model.getNodes()) {
                if (node instanceof edu.kopp.phd.express.metamodel.entity.Function) {
                    importedModel.getNodesList().add(Node.createFunction(node.getLabel(),
                            node.getPreceding(), node.getSubsequent(),0, 0));
                }

                if (node instanceof edu.kopp.phd.express.metamodel.entity.DataStore) {
                    importedModel.getNodesList().add(Node.createDataStore(node.getLabel(),
                            node.getPreceding(), node.getSubsequent()));
                }

                if (node instanceof edu.kopp.phd.express.metamodel.entity.ExternalEntity) {
                    importedModel.getNodesList().add(Node.createExternalEntity(node.getLabel(),
                            node.getPreceding(), node.getSubsequent()));
                }
            }

            models.add(importedModel);
        }

        return models;
    }

    public static void main(String[] args) {
        Models models = new DFDModels();

        for (Model model : models.importModels()) {
            System.out.println(model.getName());
            System.out.println(model.getModelType());

            for (Node node : model.getNodesList()) {
                System.out.println(node);
            }
        }
    }
}
