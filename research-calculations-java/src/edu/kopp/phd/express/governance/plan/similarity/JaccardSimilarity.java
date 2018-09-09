package edu.kopp.phd.express.governance.plan.similarity;

public class JaccardSimilarity implements ISimilarity {

    @Override
    public double similarity(int[] first, int[] second) {
        validateFeatureArrays(first, second);

        return intersection(first, second) / union(first, second);
    }
}
