package edu.kopp.phd.express.search.utils;

public class VectorSimilarity {

    public static double similarity(double first, double second) {
        return 1.0 / (1.0 + Math.abs(first - second));
    }

    public static double similarity(double[] first, double[] second) {
        double sum = 0;

        for (int i = 0; i < first.length; i++) {
            sum += Math.abs(first[i] - second[i]);
        }

        return 1.0 / (1.0 + sum);
    }
}
