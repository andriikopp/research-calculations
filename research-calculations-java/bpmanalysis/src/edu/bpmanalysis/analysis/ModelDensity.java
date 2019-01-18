package edu.bpmanalysis.analysis;

import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.metamodel.Node;

public class ModelDensity {

    public static double density(Model model) {
        return density(arcs(model), model.getNodesList().size());
    }

    public static double density(double arcs, double size) {
        double maxArcs = size * (size - 1.0);

        return maxArcs > 0 ? (arcs / 2.0) / maxArcs : 0;
    }

    public static double arcs(Model model) {
        double arcs = 0;

        for (Node node : model.getNodesList()) {
            arcs += (node.getInput() + node.getControl() + node.getOutput() + node.getMechanism());
        }

        return arcs;
    }
}
