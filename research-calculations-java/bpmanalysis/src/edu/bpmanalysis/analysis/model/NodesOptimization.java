package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.description.tools.Model;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SearchInterval;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;

public class NodesOptimization extends NonLinearConjugateGradientOptimizer {
    private double[] current;
    private double[] ideal;
    private double[] changes;

    public NodesOptimization(Formula updateFormula, ConvergenceChecker<PointValuePair> checker) {
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
                result += Math.pow(current[i] - ideal[i] + (changes[i] -
                        v * 2.0 * (current[i] - ideal[i] + changes[i])), 2);
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
                    ideal[i] + changes[i]);

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
            result += Math.pow((current[i] + changes[i]) - ideal[i], 2);
        }

        return result;
    }

    public static double[] optimization(Model model) {
        double[] current = {
                ModelDensity.size(model),
                NodesSubsetsUtil.getStartEvents(model).size(),
                NodesSubsetsUtil.getEndEvents(model).size(),
                NodesSubsetsUtil.getORRoutingElements(model).size(),
                NodesSubsetsUtil.getFunctions(model).size()
        };

        double[] ideal = getVectorOfIdealCharacteristics(model);

        NodesOptimization nodesOptimization = new NodesOptimization(Formula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6, 1e-6));
        nodesOptimization.setCurrent(current);
        nodesOptimization.setIdeal(ideal);

        nodesOptimization.optimize(null);

        return nodesOptimization.getChanges();
    }

    private static double[] getVectorOfIdealCharacteristics(Model model) {
        if (model.getModelType().equals(Model.ModelType.IDEF0)) {
            return new double[]{ModelDensity.size(model), 0, 0, 0,
                    Math.max(Math.min(NodesSubsetsUtil.getFunctions(model).size(), 6), 3)};
        }

        if (model.getModelType().equals(Model.ModelType.DFD)) {
            return new double[]{ModelDensity.size(model), 0, 0, 0,
                    Math.max(Math.min(NodesSubsetsUtil.getFunctions(model).size(), 7), 1)};
        }

        return new double[]{Math.min(ModelDensity.size(model), 31), 1, 1, 0,
                Math.max(NodesSubsetsUtil.getFunctions(model).size(), 1)};
    }

    public double[] getCurrent() {
        return current;
    }

    public void setCurrent(double[] current) {
        this.current = current;
    }

    public double[] getIdeal() {
        return ideal;
    }

    public void setIdeal(double[] ideal) {
        this.ideal = ideal;
    }

    public double[] getChanges() {
        return changes;
    }

    public void setChanges(double[] changes) {
        this.changes = changes;
    }
}
