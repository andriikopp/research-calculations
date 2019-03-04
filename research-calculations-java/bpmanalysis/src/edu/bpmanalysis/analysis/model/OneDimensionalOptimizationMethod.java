package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.model.function.OneDimensionalFunction;

public class OneDimensionalOptimizationMethod {
    private static final double GRID = 1.0;

    /**
     * Passive search method.
     * @param a - the left bound.
     * @param b - the right bound.
     * @param f - the optimized function.
     * @return arg min for the function.
     */
    public static double findMinimum(double a, double b, OneDimensionalFunction f) {
        double minFunction = f.func(a);
        double minX = a;

        for (double x = a + GRID; x <= b; x++) {
            double newMinFunction = f.func(x);

            if (newMinFunction < minFunction) {
                minX = (int) x;
                minFunction = newMinFunction;
            }
        }

        return (int) minX;
    }
}
