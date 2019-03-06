package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.model.function.OneDimensionalFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;

public class OneDimensionalOptimizationMethod {

    public static double findMinimum(double a, double b, OneDimensionalFunction f) {
        UnivariateFunction function = x -> f.func(x);
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-14);

        try {
            UnivariatePointValuePair optimized = optimizer.optimize(new MaxEval(Integer.MAX_VALUE),
                    new UnivariateObjectiveFunction(function),
                    GoalType.MINIMIZE,
                    new SearchInterval(a, b));

            return Math.round(optimized.getPoint());
        } catch (NumberIsTooLargeException e) {
            return 0;
        }
    }
}
