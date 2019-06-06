package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.ConnectorsMismatch;
import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.description.tools.Model;

public class EvaluationUtil {

    public static double quality(Model model) {
        double nodes = ModelDensity.size(model);
        double functions = NodesSubsetsUtil.getFunctions(model).size();

        double guideline1 = Math.min(
                1 - Math.signum(
                        (model.getModelType().equals(Model.ModelType.EPC) ||
                                model.getModelType().equals(Model.ModelType.BPMN)) ?
                                Math.max(nodes, 31) - 31 :

                                model.getModelType().equals(Model.ModelType.DFD) ?
                                        Math.max(nodes, 7) - 7 :

                                        Math.max(nodes, 6) - 6
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

        double guideline3 = Math.min(
                1 - Math.signum(1 - NodesSubsetsUtil.getStartEvents(model).size()),
                1 - Math.signum(1 - NodesSubsetsUtil.getEndEvents(model).size())
        );

        double guideline4 = 1 - Math.signum(ConnectorsMismatch.mismatch(model));
        double guideline5 = 1 - Math.signum(NodesSubsetsUtil.getORRoutingElements(model).size());

        double guidelines[] = {guideline1, guideline2, guideline3, guideline4, guideline5};
        double weights[];

        if (model.getModelType().equals(Model.ModelType.EPC) ||
                model.getModelType().equals(Model.ModelType.BPMN)) {
            weights = new double[]{0.19, 0.2, 0.23, 0.14, 0.24};
        } else {
            weights = new double[]{0.48, 0.52, 0, 0, 0};
        }

        double quality = 0;

        for (int i = 0; i < weights.length; i++) {
            quality += weights[i] * guidelines[i];
        }

        return quality;
    }
}
