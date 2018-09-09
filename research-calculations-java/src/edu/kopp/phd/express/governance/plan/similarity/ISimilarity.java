package edu.kopp.phd.express.governance.plan.similarity;

import java.security.InvalidParameterException;

public interface ISimilarity {

    double similarity(int[] first, int[] second);

    default void validateFeatureArrays(int[] first, int[] second) {
        if (first.length != second.length) {
            throw new InvalidParameterException();
        }
    }

    default double intersection(int[] first, int[] second) {
        double result = 0;

        for (int i = 0; i < first.length; i++) {
            result += first[i] == 1 && second[i] == 1 ? 1 : 0;
        }

        return result;
    }

    default double union(int[] first, int[] second) {
        double result = 0;

        for (int i = 0; i < first.length; i++) {
            result += first[i] == 1 || second[i] == 1 ? 1 : 0;
        }

        return result;
    }
}
