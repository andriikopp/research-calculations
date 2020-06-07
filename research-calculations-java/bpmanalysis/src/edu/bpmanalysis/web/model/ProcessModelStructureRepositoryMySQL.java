package edu.bpmanalysis.web.model;

import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class ProcessModelStructureRepositoryMySQL {
    private Sql2o sql2o;

    public ProcessModelStructureRepositoryMySQL() {
        this.sql2o = Configuration.DB_CREDENTIALS;
    }

    public void storeModel(Model model) {
        String query = "insert into f_node (model_id, model_name, model_type, node_label, node_inputs, " +
                "node_outputs, node_controls, node_mechanisms, node_type) " +
                "values (:modelId, :modelName, :modelType, :nodeLabel, :nodeInputs, :nodeOutputs, " +
                ":nodeControls, :nodeMechanisms, :nodeType)";

        try (Connection con = sql2o.open()) {
            for (Node node : model.getNodesList()) {
                con.createQuery(query)
                        .addParameter("modelId", model.getId())
                        .addParameter("modelName", model.getName())
                        .addParameter("modelType", model.getModelType().toString())
                        .addParameter("nodeLabel", node.getLabel())
                        .addParameter("nodeInputs", node.getInput())
                        .addParameter("nodeOutputs", node.getOutput())
                        .addParameter("nodeControls", node.getControl())
                        .addParameter("nodeMechanisms", node.getMechanism())
                        .addParameter("nodeType", node.getNodeType().toString())
                        .executeUpdate();
            }
        }
    }
}
