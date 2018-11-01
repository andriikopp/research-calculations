package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.Function;

public class WeightedBalanceAnalysis {
    private static final double W_OBJ = 1.00;
    private static final double W_ORG = 0.83;
    private static final double W_APP = 0.67;
    private static final double W_FLOW = 0.50;

    public static double analyze(Model model, String type) {
        double[] attributes = RelaxedValidation.ATTRIBUTES.get(type);

        boolean isRegulationsConsidered = type.equals("IDEF0");

        model.enableEnvironment();

        double size = model.countNodes();

        double sum = 0;
        double max = 0;

        for (Function function : model.getFunctions()) {
            double arcs = W_FLOW * Math.max(attributes[0], attributes[1]) * (function.getPreceding() + function.getSubsequent())
                    + W_ORG * attributes[2] * function.getOrganizationalUnits()
                    + W_OBJ * attributes[3] * (function.getInputs() + (isRegulationsConsidered ? function.getRegulations() : 0)
                        + function.getOutputs())
                    + W_APP * attributes[4] * function.getApplicationSystems();

            if (arcs > max) {
                max = arcs;
            }

            sum += arcs;
        }

        return Math.abs(sum / size - max);
    }
}
