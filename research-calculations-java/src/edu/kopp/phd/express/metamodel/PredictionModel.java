package edu.kopp.phd.express.metamodel;

public class PredictionModel {

    public static double hasErrorsSizeAnalysis(Model model) {
        return (model.countNodes() > 48) || (model.getConnectors().size() > 8) ||
                (model.getEvents().size() > 22) || (model.getFunctions().size() > 40) ||
                (model.countArcs() > 62) ? 1 : 0;
    }

    public static double hasErrorsConnectionAnalysis(Model model) {
        return (model.density() > 0.22) || (model.connectivity() > 1.1) ? 1 : 0;
    }

    public static double hasErrorsControlFlowComplexityAnalysis(Model model) {
        return (model.complexitySplitXor() > 17) || (model.complexitySplitOr() > 4) ||
                (model.complexitySplitAnd() > 2) ? 1 : 0;
    }

    public static double hasErrorsBalanceAnalysis(Model model) {
        return model.balance() > 0 ? 1 : 0;
    }
}
