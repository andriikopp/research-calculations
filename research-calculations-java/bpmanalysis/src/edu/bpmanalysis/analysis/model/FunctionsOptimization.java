package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
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

public class FunctionsOptimization extends NonLinearConjugateGradientOptimizer {
    private double[][] current;
    private double[][] changes;
    private Model model;

    public FunctionsOptimization(Formula updateFormula, ConvergenceChecker<PointValuePair> checker) {
        super(updateFormula, checker);
    }

    @Override
    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        int size = current.length;

        this.changes = new double[size][Node.arcTypes.length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < Node.arcTypes.length; j++) {
                if ((j == 2 || j == 3) && model.getArcTypes().length == 2) {
                    continue;
                }

                double max = FunctionsBalance.MAX_F;

                if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                        model.getModelType().equals(Model.ModelType.DFD)) {
                    double necessaryMin = 1;

                    if (model.getModelType().equals(Model.ModelType.IDEF0) && (j == 0)) {
                        necessaryMin = 0;
                    }

                    if (model.getModelType().equals(Model.ModelType.DFD) && (j == 1)) {
                        max = Math.max(necessaryMin, Math.min(current[i][j],
                                current[i][0]));
                    } else {
                        max = Math.max(necessaryMin, Math.min(current[i][j],
                                FunctionsBalance.MAX_FUNCTIONAL_ARCS));
                    }
                }

                final int indexX = i;
                final int indexY = j;
                final double ideal = max;

                UnivariateFunction func =
                        v -> Math.pow((current[indexX][indexY] + v) - ideal, 2);
                UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);
                double point = optimizer.optimize(new MaxEval(200),
                        new UnivariateObjectiveFunction(func),
                        GoalType.MINIMIZE,
                        new SearchInterval(ideal - current[indexX][indexY] - 1,
                                ideal - current[indexX][indexY] + 1)).getPoint();

                int aPoint = (int) point;
                int bPoint = aPoint + 1;

                if (func.value(aPoint) < func.value(bPoint)) {
                    changes[i][j] = aPoint;
                } else {
                    changes[i][j] = bPoint;
                }
            }
        }

        return null;
    }

    public static double[][] optimization(Model model) {
        List<Node> functions = NodesSubsetsUtil.getFunctions(model);

        double[][] current = getNodesDegreesVector(functions);

        FunctionsOptimization functionsOptimization =
                new FunctionsOptimization(Formula.FLETCHER_REEVES,
                        new SimpleValueChecker(1e-6, 1e-6));
        functionsOptimization.setCurrent(current);
        functionsOptimization.setModel(model);

        functionsOptimization.optimize(null);

        return functionsOptimization.getChanges();
    }

    private static double[][] getNodesDegreesVector(List<Node> functions) {
        double[][] array = new double[functions.size()][Node.arcTypes.length];

        for (int i = 0; i < functions.size(); i++) {
            array[i] = functions.get(i).getArcs();
        }

        return array;
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
