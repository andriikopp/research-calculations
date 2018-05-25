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

    /**
     * Implementation of the direct estimation method used to define similarity of business process models.
     */
    public static final CustomizableBPModelsSimilarity DIRECT_ESTIMATION = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.directEstimation(similarities, BPModelsSimilarityUtil.similarity);
        }
    };

    /**
     * Implementation of the arithmetic progression ranking method used to define similarity of business process models.
     */
    public static final CustomizableBPModelsSimilarity ARITHMETIC_PROGRESSION = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.fishbernEstimation(similarities, BPModelsSimilarityUtil.similarity,
                    (index, n) -> BPModelsSimilarityUtil.fishbernFirstEquation(index, n));
        }
    };

    /**
     * Implementation of the geometric progression rankin method used to define similarity of business process models.
     */
    public static final CustomizableBPModelsSimilarity GEOMETRIC_PROGRESSION = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.fishbernEstimation(similarities, BPModelsSimilarityUtil.similarity,
                    (index, n) -> BPModelsSimilarityUtil.fishbernSecondEquation(index, n));
        }
    };

    /**
     * Implementation of the pairwise comparison method used to define similarity of business process models.
     */
    public static final CustomizableBPModelsSimilarity PAIRWISE_COMPARISON = new CustomizableBPModelsSimilarity() {
        @Override
        public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
            BPModelRDFGraph firstNormalized = SemanticSimilarityUtil.normalizeBPModel(first);
            BPModelRDFGraph secondNormalized = SemanticSimilarityUtil.normalizeBPModel(second);

            double[] similarities = BPModelsSimilarityUtil.extractSimilarities(firstNormalized, secondNormalized);

            return BPModelsSimilarityUtil.pairwiseComparison(similarities, BPModelsSimilarityUtil.similarity);
        }
    };
}
