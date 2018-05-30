package bp.retrieve.container.clustering;

import bp.retrieve.similarity.SimilarityMethodsToolset;
import bp.retrieve.similarity.scales.HarringtonScale;
import main.resources.repository.BPModelsCollection;
import main.resources.repository.files.TravelRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Evaluation of business process models clustering (FCM).
 *
 * @author Andrii Kopp
 */
public class ProcessModelClusteringEvaluation {

    @Test
    public void evaluate() {
        ProcessModelClustering.setThreshold(HarringtonScale.VERY_BAD_THRESHOLD);

        List<ProcessModelCloseness> directEstimation = ProcessModelClustering.cluster(new TravelRequest(),
                Arrays.asList(BPModelsCollection.models), SimilarityMethodsToolset.useDirectEstimation());
        List<ProcessModelCloseness> arithmeticProgression = ProcessModelClustering.cluster(new TravelRequest(),
                Arrays.asList(BPModelsCollection.models), SimilarityMethodsToolset.useArithmeticProgression());
        List<ProcessModelCloseness> geometricProgression = ProcessModelClustering.cluster(new TravelRequest(),
                Arrays.asList(BPModelsCollection.models), SimilarityMethodsToolset.useGeometricProgression());
        List<ProcessModelCloseness> pairwiseComparison = ProcessModelClustering.cluster(new TravelRequest(),
                Arrays.asList(BPModelsCollection.models), SimilarityMethodsToolset.usePairwiseComparison());

        printClusteringResults(directEstimation);
        printClusteringResults(arithmeticProgression);
        printClusteringResults(geometricProgression);
        printClusteringResults(pairwiseComparison);
    }

    private void printClusteringResults(List<ProcessModelCloseness> results) {
        for (ProcessModelCloseness closeness : results) {
            System.out.printf("%s\t%.4f\n", closeness.getProcessModel().getName(), closeness.getClosenessValue());
        }
    }
}
