package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer;

public class RoutingOptimization extends NonLinearConjugateGradientOptimizer {
    private double[][] current;
    private double[][] changes;

    public RoutingOptimization(Formula updateFormula, ConvergenceChecker<PointValuePair> checker) {
        super(updateFormula, checker);
    }

    @Override
    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        int size = 3;

        this.changes = new double[size][2];

        // Nonlinear Conjugate Gradient Optimization
        double lambda = 0.5;

        for (int i = 0; i < size; i++) {
            // splits
            double point = changes[i][0] - lambda * 2 *
                    (current[i][0] - current[i][1] + changes[i][0]);

            changes[i][0] = point;

            // joins
            point = changes[i][1] - lambda * 2 *
                    (current[i][1] - current[i][0] + changes[i][1]);

            changes[i][1] = point;
        }

        // Branch and Bound Optimization
        double best = goal(changes);

        for (int i = 0; i < size; i++) {
            for (int k = 0; k < changes[i].length; k++) {
                double[][] left = new double[size][changes[i].length];

                for (int j = 0; j < size; j++) {
                    System.arraycopy(changes[j], 0, left[j], 0, changes[i].length);
                }

                double[][] right = new double[size][changes[i].length];

                for (int j = 0; j < size; j++) {
                    System.arraycopy(changes[j], 0, right[j], 0, changes[i].length);
                }

                left[i][k] = (int) changes[i][k];
                right[i][k] = ((int) changes[i][k]) + 1;

                if (goal(left) < best) {
                    changes[i][k] = left[i][k];
                } else if (goal(right) < best) {
                    changes[i][k] = right[i][k];
                } else {
                    changes[i][k] = (int) changes[i][k];
                }

                best = goal(changes);
            }
        }

        return null;
    }

    // Goal Function
    public double goal(double[][] changes) {
        double result = 0;

        for (int i = 0; i < changes.length; i++) {
            // splits
            result += Math.pow((current[i][0] + changes[i][0]) - current[i][1], 2);

            // joins
            result += Math.pow((current[i][1] + changes[i][1]) - current[i][0], 2);
        }

        return result;
    }

    public static double[][] optimization(Model model) {
        int size = 3;

        double[][] current = new double[size][2];

        Node.NodeType[] routingTypes = {
                Node.NodeType.XOR_CONNECTOR,
                Node.NodeType.OR_CONNECTOR,
                Node.NodeType.AND_CONNECTOR
        };

        for (int i = 0; i < size; i++) {
            current[i][0] = NodesSubsetsUtil.getSplitConnectors(model, routingTypes[i]).size();
            current[i][1] = NodesSubsetsUtil.getJoinConnectors(model, routingTypes[i]).size();
        }

        RoutingOptimization routingOptimization =
                new RoutingOptimization(Formula.FLETCHER_REEVES,
                        new SimpleValueChecker(1e-6, 1e-6));
        routingOptimization.setCurrent(current);

        routingOptimization.optimize(null);

        return routingOptimization.getChanges();
    }

    public double[][] getCurrent() {
        return current;
    }

    public void setCurrent(double[][] current) {
        this.current = current;
    }

    public double[][] getChanges() {
        return changes;
    }

    public void setChanges(double[][] changes) {
        this.changes = changes;
    }
}
