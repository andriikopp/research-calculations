package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;

import java.util.List;

public class ConnectorsOptimization extends NonLinearConjugateGradientOptimizer {
    private double[] current;
    private double[] changes;

    public ConnectorsOptimization(Formula updateFormula, ConvergenceChecker<PointValuePair> checker) {
        super(updateFormula, checker);
    }

    @Override
    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        int size = current.length;

        this.changes = new double[size];

        // Nonlinear Conjugate Gradient Optimization
        UnivariateFunction func = v -> {
            double result = 0;

            for (int i = 0; i < size; i++) {
                result += Math.pow(current[i] - ConnectorsBalance.MAX_C + (changes[i] -
                        v * 2.0 * (current[i] - ConnectorsBalance.MAX_C + changes[i])), 2);
            }

            return result;
        };

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);

        double lambda = optimizer.optimize(
                new MaxEval(200),
                new UnivariateObjectiveFunction(func),
                GoalType.MINIMIZE,
                new SearchInterval(0, 1)
        ).getPoint();

        for (int i = 0; i < size; i++) {
            double point = changes[i] - lambda * 2 * (current[i] -
                    ConnectorsBalance.MAX_C + changes[i]);

            changes[i] = (int) point;
        }

        // Branch and Bound Optimization
        double best = goal(changes);

        for (int i = 0; i < size; i++) {
            double[] left = new double[size];
            System.arraycopy(changes, 0, left, 0, size);

            double[] right = new double[size];
            System.arraycopy(changes, 0, right, 0, size);

            left[i] = (int) changes[i];
            right[i] = ((int) changes[i]) + 1;

            if (goal(left) < best) {
                changes[i] = left[i];
            } else if (goal(right) < best) {
                changes[i] = right[i];
            } else {
                changes[i] = (int) changes[i];
            }

            best = goal(changes);
        }

        return null;
    }

    // Goal Function
    public double goal(double[] changes) {
        double result = 0;

        for (int i = 0; i < changes.length; i++) {
            result += Math.pow((current[i] + changes[i]) - ConnectorsBalance.MAX_C, 2);
        }

        return result;
    }

    public static double[] optimization(Model model) {
        List<Node> connectors = NodesSubsetsUtil.getConnectors(model);

        double[] current = getConnectorsDegreesVector(connectors);

        ConnectorsOptimization connectorsOptimization = new ConnectorsOptimization(
                Formula.FLETCHER_REEVES, new SimpleValueChecker(1e-6, 1e-6));
        connectorsOptimization.setCurrent(current);

        connectorsOptimization.optimize(null);

        return connectorsOptimization.getChanges();
    }

    private static double[] getConnectorsDegreesVector(List<Node> connectors) {
        double[] array = new double[connectors.size()];

        for (int i = 0; i < connectors.size(); i++) {
            array[i] = connectors.get(i).getInput() + connectors.get(i).getOutput();
        }

        return array;
    }

    public double[] getCurrent() {
        return current;
    }

    public void setCurrent(double[] current) {
        this.current = current;
    }

    public double[] getChanges() {
        return changes;
    }

    public void setChanges(double[] changes) {
        this.changes = changes;
    }
}
