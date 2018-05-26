package bp.retrieve.similarity;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.CustomizableBPModelsSimilarity;

/**
 * Provides implementations of the {@link bp.retrieve.CustomizableBPModelsSimilarity} class
 * according to the various methods of similarity estimation of business process models.
 *
 * @author Andrii Kopp
 */
public class SimilarityMethodsToolset {
    private static CustomizableBPModelsSimilarity currentMethod;

    /**
     * Use direct estimation method.
     *
     * @return the instance of corresponding implementation.
     */
    public static CustomizableBPModelsSimilarity useDirectEstimation() {
        currentMethod = DIRECT_ESTIMATION;
        return currentMethod;
    }

    /**
     * Use arithmetic progression ranking method.
     *
     * @return the instance of corresponding implementation.
     */
    public static CustomizableBPModelsSimilarity useArithmeticProgression() {
        currentMethod = ARITHMETIC_PROGRESSION;
        return currentMethod;
    }

    /**
     * Use geometric progression ranking method.
     *
     * @return the instance of corresponding implementation.
     */
    public static CustomizableBPModelsSimilarity useGeometricProgression() {
        currentMethod = GEOMETRIC_PROGRESSION;
        return currentMethod;
    }

    /**
     * Use pairwise comparison method.
     *
     * @return the instance of corresponding implementation.
     */
    public static CustomizableBPModelsSimilarity usePairwiseComparison() {
        currentMethod = PAIRWISE_COMPARISON;
        return currentMethod;
    }

    /**
     * Use average similarity of business process models.
     *
     * @return the instance of corresponding implementation.
     */
    public static CustomizableBPModelsSimilarity useAverageSimilarity() {
        currentMethod = AVERAGE_SIMILARITY;
        return currentMethod;
    }

    /**
     * Get current method of similarity estimation.
     *
     * @return the instance of corresponding implementation.
     */
    public static CustomizableBPModelsSimilarity getCurrentMethod() {
        return currentMethod;
    }

    /*
     * Average similarity value of business process models.
     */
    private static final CustomizableBPModelsSimilarity AVERAGE_SIMILARITY = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            final double numberOfMethods = 4;

            double averageSimilarity = (DIRECT_ESTIMATION.compareBPModelRDFGraphs(first, second) +
                    ARITHMETIC_PROGRESSION.compareBPModelRDFGraphs(first, second) +
                    GEOMETRIC_PROGRESSION.compareBPModelRDFGraphs(first, second) +
                    PAIRWISE_COMPARISON.compareBPModelRDFGraphs(first, second)) / numberOfMethods;

            return averageSimilarity;
        }
    };

    /*
     * Implementation of the direct estimation method used to define similarity of business process models.
     */
    private static final CustomizableBPModelsSimilarity DIRECT_ESTIMATION = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.directEstimation(similarities, BPModelsSimilarityUtil.similarity);
        }
    };

    /*
     * Implementation of the arithmetic progression ranking method used to define similarity of business process models.
     */
    private static final CustomizableBPModelsSimilarity ARITHMETIC_PROGRESSION = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.fishbernEstimation(similarities, BPModelsSimilarityUtil.similarity,
                    (index, n) -> BPModelsSimilarityUtil.fishbernFirstEquation(index, n));
        }
    };

    /*
     * Implementation of the geometric progression rankin method used to define similarity of business process models.
     */
    private static final CustomizableBPModelsSimilarity GEOMETRIC_PROGRESSION = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.fishbernEstimation(similarities, BPModelsSimilarityUtil.similarity,
                    (index, n) -> BPModelsSimilarityUtil.fishbernSecondEquation(index, n));
        }
    };

    /*
     * Implementation of the pairwise comparison method used to define similarity of business process models.
     */
    private static final CustomizableBPModelsSimilarity PAIRWISE_COMPARISON = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.pairwiseComparison(similarities, BPModelsSimilarityUtil.similarity);
        }
    };
}
