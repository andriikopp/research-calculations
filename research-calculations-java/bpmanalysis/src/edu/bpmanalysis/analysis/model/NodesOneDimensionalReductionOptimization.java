package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.model.function.ArrayFunction;
import edu.bpmanalysis.description.tools.Model;

public class NodesOneDimensionalReductionOptimization {

    public static double[] optimization(Model model) {
        double[] current = {
                ModelDensity.size(model),
                NodesSubsetsUtil.getStartEvents(model).size(),
                NodesSubsetsUtil.getEndEvents(model).size(),
                NodesSubsetsUtil.getORRoutingElements(model).size(),
                NodesSubsetsUtil.getFunctions(model).size()
        };

        int size = current.length;

        double[] ideal = getVectorOfIdealCharacteristics(model);

        double[] changes = new double[size];

        RestrictionFunction[] restrictions = {
                (i, o) -> OneDimensionalOptimizationMethod.findMinimum(i - o, 0,
                        x -> Math.pow((o + x) - i, 2)),
                (i, o) -> OneDimensionalOptimizationMethod.findMinimum(i - o, 1,
                        x -> Math.pow((o + x) - i, 2)),
                (i, o) -> OneDimensionalOptimizationMethod.findMinimum(i - o, 1,
                        x -> Math.pow((o + x) - i, 2)),
                (i, o) -> OneDimensionalOptimizationMethod.findMinimum(-o, 0,
                        x -> Math.pow((o + x) - i, 2)),
                (i, o) -> OneDimensionalOptimizationMethod.findMinimum(0, i - o,
                        x -> Math.pow((o + x) - i, 2))
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
            return new double[]{Math.min(ModelDensity.size(model), 6), 0, 0, 0,
                    Math.max(NodesSubsetsUtil.getFunctions(model).size(), 3)};
        }

        if (model.getModelType().equals(Model.ModelType.DFD)) {
            return new double[]{Math.min(ModelDensity.size(model), 7), 0, 0, 0,
                    Math.max(NodesSubsetsUtil.getFunctions(model).size(), 1)};
        }

        return new double[]{Math.min(ModelDensity.size(model), 31), 1, 1, 0,
                Math.max(NodesSubsetsUtil.getFunctions(model).size(), 1)};
    }

    private interface RestrictionFunction {
        double value(double ideal, double old);
    }
}
