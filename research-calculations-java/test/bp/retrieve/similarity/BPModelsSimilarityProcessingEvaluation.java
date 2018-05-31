package bp.retrieve.similarity;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import main.resources.repository.BPModelsCollection;
import org.junit.Test;

/**
 * Business process models similarity results processing.
 *
 * @author Andrii Kopp
 */
public class BPModelsSimilarityProcessingEvaluation {
    private double[] averageWeights;

    @Test
    public void evaluate() {
        for (GenericProcessModel first : BPModelsCollection.models)
            for (GenericProcessModel second : BPModelsCollection.models) {
                System.out.printf("%s <> %s\t", first.getName(), second.getName());

                BPModelRDFGraph a = SemanticSimilarityUtil.normalizeBPModel(first.getModelDescription());
                BPModelRDFGraph b = SemanticSimilarityUtil.normalizeBPModel(second.getModelDescription());

                double[] similarities = BPModelsSimilarityUtil.extractSimilarities(a, b);

                averageWeights = null;

                System.out.printf("%.2f\t", BPModelsSimilarityUtil.directEstimation(similarities, similarity));
                System.out.printf("%.2f\t", BPModelsSimilarityUtil.fishbernEstimation(similarities, similarity,
                        (index, n) -> BPModelsSimilarityUtil.fishbernFirstEquation(index, n)));
                System.out.printf("%.2f\t", BPModelsSimilarityUtil.fishbernEstimation(similarities, similarity,
                        (index, n) -> BPModelsSimilarityUtil.fishbernSecondEquation(index, n)));
                System.out.printf("%.2f\t", BPModelsSimilarityUtil.pairwiseComparison(similarities, similarity));

                for (int i = 0; i < averageWeights.length; i++)
                    averageWeights[i] /= 4.0;

                BPModelsSimilarityUtil.norm(averageWeights);

                System.out.printf("%.2f\n", BPModelsSimilarityUtil.similarity.measure(averageWeights, similarities));
            }
    }

    private SimilarityFunction similarity = (weights, similarities) -> {
        if (averageWeights == null)
            averageWeights = new double[weights.length];

        for (int i = 0; i < weights.length; i++)
            averageWeights[i] += weights[i];

        double measure = 0;

        for (int i = 0; i < weights.length; i++)
            measure += weights[i] * similarities[i];

        return measure;
    };
}
