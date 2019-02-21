package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class PredictionModel {

    public static double time(Model model) {
        return time(getTNSF(model), getTNE(model), getNSFE(model), getTNG(model));
    }

    public static double time(double tnsf, double tne, double nsfe, double tng) {
        return 40.622343
                - 0.205153 * tnsf
                + 9.562413 * tne
                - 8.387718 * nsfe
                + 5.411965 * tng;
    }

    public static double getTNSF(Model model) {
        return ModelDensity.arcs(model);
    }

    public static double getTNE(Model model) {
        return NodesSubsetsUtil.getEvents(model).size();
    }

    public static double getNSFE(Model model) {
        double value = 0;

        for (Node node : NodesSubsetsUtil.getEvents(model)) {
            value += node.getOutput();
        }

        return value;
    }

    public static double getTNG(Model model) {
        return NodesSubsetsUtil.getConnectors(model).size();
    }
}
