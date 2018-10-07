package edu.kopp.phd.express.search.utils;

public class VectorSimilarity {

    public static double similarity(double first, double second) {
        return 1.0 / (1.0 + Math.abs(first - second));
    }
}
