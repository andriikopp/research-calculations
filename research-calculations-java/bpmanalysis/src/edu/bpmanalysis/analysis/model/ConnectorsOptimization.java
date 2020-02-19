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

        for (int i = 0; i < size; i++) {
            final int index = i;

            UnivariateFunction func =
                    v -> Math.pow((current[index] + v) - ConnectorsBalance.MAX_C, 2);
            UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
            double point = optimizer.optimize(new MaxEval(200),
                    new UnivariateObjectiveFunction(func),
                    GoalType.MINIMIZE,
                    new SearchInterval(ConnectorsBalance.MAX_C - current[index] - 1,
                            ConnectorsBalance.MAX_C - current[index] + 1)).getPoint();

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
