package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.analysis.model.function.MatrixFunction;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.metamodel.Node;

import java.util.List;

public class FunctionsSteepestDescentOptimization {

    public static double[][] optimization(Model model) {
        List<Node> functions = NodesSubsetsUtil.getFunctions(model);

        int size = functions.size();

        double[][] current = getNodesDegreesVector(functions);
        double[][] changes = new double[size][Node.arcTypes.length];

        MatrixFunction function = (variables) -> {
            double result = 0;

            for (int i = 0; i < current.length; i++) {
                for (int j = 0; j < Node.arcTypes.length; j++) {
                    double max = FunctionsBalance.MAX_F;

                    if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                            model.getModelType().equals(Model.ModelType.DFD)) {
                        max = Math.max(1, Math.min((current[i][j] + variables[i][j]), 3));
                    }

                    result += Math.pow((current[i][j] + variables[i][j]) - max, 2);
                }
            }

            return result;
        };

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < Node.arcTypes.length; j++) {
                if ((j == 1 || j == 3) && model.getArcTypes().length == 2) {
                    continue;
                }

                double max = FunctionsBalance.MAX_F;

                if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                        model.getModelType().equals(Model.ModelType.DFD)) {
                    max = Math.max(1, Math.min(current[i][j], 3));
                }

                if (Math.pow(current[i][j] - max, 2) > 0) {
                    double lambda = (current[i][j] + changes[i][j] - max) /
                            (2.0 * (max + current[i][j] + changes[i][j]));

                    changes[i][j] = changes[i][j] - lambda * 2.0 * (max +
                            (current[i][j] + changes[i][j]));
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
