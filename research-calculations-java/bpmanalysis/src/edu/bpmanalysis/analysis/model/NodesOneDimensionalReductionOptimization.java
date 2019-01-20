package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.model.function.ArrayFunction;
import edu.bpmanalysis.collection.tools.Model;

public class NodesOneDimensionalReductionOptimization {

    public static double[] optimization(Model model) {
        double[] current = {
                model.getNodesList().size(),
                NodesSubsetsUtil.getStartEvents(model).size(),
                NodesSubsetsUtil.getEndEvents(model).size(),
                NodesSubsetsUtil.getORRoutingElements(model).size(),
                NodesSubsetsUtil.getFunctions(model).size()
        };

        int size = current.length;

        double[] ideal = getVectorOfIdealCharacteristics(model);

        double[] changes = new double[size];

        RestrictionFunction[] restrictions = {
                (i, o) -> (1.0 - (i - o) < 0 ? 0 : 1.0) * (i - o),
                (i, o) -> i - o,
                (i, o) -> i - o,
                (i, o) -> -o,
                (i, o) -> ((i - o) < 0 ? 0 : 1.0) * (i - o)
        };

        ArrayFunction function = (variables) -> {
            double result = 0;

            for (int i = 0; i < size; i++) {
                result += Math.pow((current[i] + variables[i]) - ideal[i], 2);
            }

            return result;
        };

        for (int i = 0; i < size; i++) {
            double old = function.value(changes);
            changes[i] = restrictions[i].value(ideal[i], current[i]);

            if (function.value(changes) >= old) {
                changes[i] = 0;
            }
        }

        return changes;
    }

    private static double[] getVectorOfIdealCharacteristics(Model model) {
        if (model.getModelType().equals(Model.ModelType.IDEF0)) {
            return new double[]{6, 0, 0, 0, 3};
        }

        if (model.getModelType().equals(Model.ModelType.DFD)) {
            return new double[]{7, 0, 0, 0, 1};
        }

        return new double[]{31, 1, 1, 0, 1};
    }

    private interface RestrictionFunction {
        double value(double ideal, double old);
    }
}
