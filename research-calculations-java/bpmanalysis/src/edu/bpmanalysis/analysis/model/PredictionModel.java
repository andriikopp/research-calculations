package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.ConnectorsMismatch;
import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.collection.tools.Model;

public class PredictionModel {

    public static double understandabilityTime(Model model) {
        return understandabilityTime(model.getNodesList().size());
    }

    public static double modifiabilityTime(Model model) {
        return modifiabilityTime(ConnectorsMismatch.mismatch(model), ModelDensity.density(model));
    }

    public static double understandabilityTime(double size) {
        double b0 = 47.04;
        double b1 = 2.46;

        return b0 + b1 * size;
    }

    public static double modifiabilityTime(double mismatch, double density) {
        double b0 = 50.08;
        double b1 = 3.77;
        double b2 = 422.95;

        return b0 + b1 * mismatch + b2 * density;
    }
}
