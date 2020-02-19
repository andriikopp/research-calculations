package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
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

        for (int i = 0; i < size; i++) {
            final int index = i;

            UnivariateFunction func =
                    v -> Math.pow((current[index][0] + v) - current[index][1], 2);
            UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
            double point = optimizer.optimize(new MaxEval(200),
                    new UnivariateObjectiveFunction(func),
                    GoalType.MINIMIZE,
                    new SearchInterval(current[index][1] - current[index][0] - 1,
                            current[index][1] - current[index][0] + 1)).getPoint();

            int aPoint = (int) point;
            int bPoint = aPoint + 1;

            if (func.value(aPoint) < func.value(bPoint)) {
                changes[i][0] = aPoint;
            } else {
                changes[i][0] = bPoint;
            }

            func = v -> Math.pow((current[index][1] + v) - current[index][0], 2);
            optimizer = new BrentOptimizer(1e-10, 1e-14);
            point = optimizer.optimize(new MaxEval(200),
                    new UnivariateObjectiveFunction(func),
                    GoalType.MINIMIZE,
                    new SearchInterval(current[index][0] - current[index][1] - 1,
                            current[index][0] - current[index][1] + 1)).getPoint();

            aPoint = (int) point;
            bPoint = aPoint + 1;

            if (func.value(aPoint) < func.value(bPoint)) {
                changes[i][1] = aPoint;
            } else {
                changes[i][1] = bPoint;
            }
        }

        return null;
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
