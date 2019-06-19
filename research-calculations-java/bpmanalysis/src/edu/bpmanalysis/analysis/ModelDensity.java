package edu.bpmanalysis.analysis;

import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class ModelDensity {

    public static double density(Model model) {
        return density(arcs(model), size(model));
    }

    public static double density(double arcs, double size) {
        double maxArcs = size * (size - 1.0);

        return ((arcs / 2.0) + 1.0) / (maxArcs + 1.0);
    }

    public static double connectivity(Model model) {
        return connectivity(arcs(model), size(model));
    }

    public static double connectivity(double arcs, double size) {
        return (arcs / 2.0) / size;
    }

    public static double size(Model model) {
        double value = 0;

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(Node.NodeType.FUNCTION)) {
                value++;
            }

            if (model.getModelType().equals(Model.ModelType.BPMN) ||
                    model.getModelType().equals(Model.ModelType.EPC)) {
                if (node.getNodeType().equals(Node.NodeType.EVENT) ||
                        node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                        node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                        node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)) {
                    value++;
                }
            }

            if (model.getModelType().equals(Model.ModelType.DFD)) {
                if (node.getNodeType().equals(Node.NodeType.DATA_STORE) ||
                        node.getNodeType().equals(Node.NodeType.EXTERNAL_ENTITY)) {
                    value++;
                }
            }
        }

        return value;
    }

    public static double arcs(Model model) {
        double arcs = 0;

        for (Node node : model.getNodesList()) {
            arcs += (node.getInput() + node.getControl() + node.getOutput() + node.getMechanism());
        }

        return arcs;
    }
}
