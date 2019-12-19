package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.ConnectorsMismatch;
import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.description.tools.Model;

public class EvaluationUtil {

    public static double quality(Model model) {
        double guidelines[] = checkGuidelines(model);
        double weights[];

        if (model.getModelType().equals(Model.ModelType.EPC) ||
                model.getModelType().equals(Model.ModelType.BPMN)) {
            weights = new double[]{0.21, 0.19, 0.16, 0.28, 0.16};
        } else {
            weights = new double[]{0.52, 0.48, 0, 0, 0};
        }

        double quality = 0;

        for (int i = 0; i < weights.length; i++) {
            quality += weights[i] * guidelines[i];
        }

        return quality;
    }

    public static double[] checkGuidelines(Model model) {
        double nodes = ModelDensity.size(model);
        double functions = NodesSubsetsUtil.getFunctions(model).size();

        double guideline1 = Math.min(
                1 - Math.signum(
                        (model.getModelType().equals(Model.ModelType.EPC) ||
                                model.getModelType().equals(Model.ModelType.BPMN)) ?
                                Math.max(nodes, 31) - 31 :

                                model.getModelType().equals(Model.ModelType.DFD) ?
                                        Math.max(functions, 7) - 7 :

                                        Math.max(functions, 6) - 6
                ),
                1 - Math.signum(
                        (model.getModelType().equals(Model.ModelType.EPC) ||
                                model.getModelType().equals(Model.ModelType.BPMN)) ?
                                1 - Math.min(functions, 1) :

                                model.getModelType().equals(Model.ModelType.DFD) ?
                                        1 - Math.min(functions, 1) :

                                        3 - Math.min(functions, 3)
                )
        );

        double guideline2 = Math.min(
                1 - Math.signum(new ConnectorsBalance().balanceCoefficient(model)),
                1 - Math.signum(new FunctionsBalance().balanceCoefficient(model))
        );

        double guideline3 = (model.getModelType().equals(Model.ModelType.EPC) ||
                model.getModelType().equals(Model.ModelType.BPMN))
                ? (Math.min(1 - Math.signum(Math.abs(1 - NodesSubsetsUtil.getStartEvents(model).size())),
                1 - Math.signum(Math.abs(1 - NodesSubsetsUtil.getEndEvents(model).size()))))
                : 1;

        double guideline4 = (model.getModelType().equals(Model.ModelType.EPC) ||
                model.getModelType().equals(Model.ModelType.BPMN))
                ? (1 - Math.signum(ConnectorsMismatch.mismatch(model)))
                : 1;

        double guideline5 = (model.getModelType().equals(Model.ModelType.EPC) ||
                model.getModelType().equals(Model.ModelType.BPMN))
                ? (1 - Math.signum(NodesSubsetsUtil.getORRoutingElements(model).size()))
                : 1;

        double guidelines[] = {guideline1, guideline2, guideline3, guideline4, guideline5};

        return guidelines;
    }
}
