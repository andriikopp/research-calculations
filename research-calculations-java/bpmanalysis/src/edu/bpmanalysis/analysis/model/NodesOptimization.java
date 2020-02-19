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

        for (int i = 0; i < size; i++) {
            final int index = i;

            UnivariateFunction func =
                    v -> Math.pow((current[index] + v) - ideal[index], 2);
            UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
            double point = optimizer.optimize(new MaxEval(200),
                    new UnivariateObjectiveFunction(func),
                    GoalType.MINIMIZE,
                    new SearchInterval(ideal[index] - current[i] - 1,
                            ideal[index] - current[i] + 1)).getPoint();

            int aPoint = (int) point;
            int bPoint = aPoint + 1;

            if (func.value(aPoint) < func.value(bPoint)) {
                changes[i] = aPoint;
            } else {
                changes[i] = bPoint;
            }
        }

        return null;
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
