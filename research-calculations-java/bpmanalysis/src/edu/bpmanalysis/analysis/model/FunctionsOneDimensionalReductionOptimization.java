package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.analysis.model.function.MatrixFunction;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.tools.Node;

import java.util.List;

public class FunctionsOneDimensionalReductionOptimization {

    public static double[][] optimization(Model model) {
        List<Node> functions = NodesSubsetsUtil.getFunctions(model);

        int size = functions.size();

        double[][] current = getNodesDegreesVector(functions);
        double[][] changes = new double[size][Node.arcTypes.length];

        MatrixFunction function = (variables) -> {
            double result = 0;

            for (int i = 0; i < current.length; i++) {
                for (int j = 0; j < Node.arcTypes.length; j++) {
                    if ((j == 2 || j == 3) && model.getArcTypes().length == 2) {
                        continue;
                    }

                    double max = FunctionsBalance.MAX_F;

                    if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                            model.getModelType().equals(Model.ModelType.DFD)) {
                        double necessaryMin;

                        if (model.getModelType().equals(Model.ModelType.IDEF0) && (j == 0)) {
                            necessaryMin = 0;
                        } else {
                            necessaryMin = 1;
                        }

                        max = Math.max(necessaryMin, Math.min((current[i][j] + variables[i][j]),
                                FunctionsBalance.MAX_FUNCTIONAL_ARCS));
                    }

                    result += Math.pow((current[i][j] + variables[i][j]) - max, 2);
                }
            }

            return result;
        };

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < Node.arcTypes.length; j++) {
                if ((j == 2 || j == 3) && model.getArcTypes().length == 2) {
                    continue;
                }

                double max = FunctionsBalance.MAX_F;

                if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                        model.getModelType().equals(Model.ModelType.DFD)) {
                    double necessaryMin;

                    if (model.getModelType().equals(Model.ModelType.IDEF0) && (j == 0)) {
                        necessaryMin = 0;
                    } else {
                        necessaryMin = 1;
                    }

                    max = Math.max(necessaryMin, Math.min(current[i][j],
                            FunctionsBalance.MAX_FUNCTIONAL_ARCS));
                }

                double old = function.value(changes);
                final int row = i;
                final int col = j;
                final double finalMax = max;
                changes[i][j] = OneDimensionalOptimizationMethod.findMinimum(
                        -current[i][j],
                        max - current[i][j],
                        x -> Math.pow((current[row][col] + x) - finalMax, 2));

                if (function.value(changes) >= old) {
                    changes[i][j] = 0;
                }
            }
        }

        if (Configuration.DEBUG) {
            System.out.printf("%.2f\n",function.value(changes));
        }

        return changes;
    }

    private static double[][] getNodesDegreesVector(List<Node> functions) {
        double[][] array = new double[functions.size()][Node.arcTypes.length];

        for (int i = 0; i < functions.size(); i++) {
            array[i] = functions.get(i).getArcs();
        }

        return array;
    }
}
