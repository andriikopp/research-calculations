package bp.retrieve.container.clustering;

import bp.retrieve.CustomizableBPModelsSimilarity;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.similarity.scales.HarringtonScale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Clustering of business process models using FCM (Fuzzy Classifier Means) method.
 *
 * @author Andrii Kopp
 */
public class ProcessModelClustering {
    // Fuzziness coefficient.
    private static final double FUZZINESS_COEFF = 1.5;

    private static HarringtonScale threshold;

    /**
     * Returns the list of models and theirs fuzzy memberships.
     *
     * @param processModel - the pattern to be classified;
     * @param processModels - the set of business process models stored in repository;
     * @param similarity - the implementation of business process models similarity measure.
     * @return the results of clustering.
     */
    public static List<ProcessModelCloseness> cluster(GenericProcessModel processModel,
                                                      Collection<GenericProcessModel> processModels,
                                                      CustomizableBPModelsSimilarity similarity) {
        List<ProcessModelCloseness> clusteringResults = new ArrayList<>();
        double sumOfMemberships = 0;

        for (GenericProcessModel model : processModels) {
            double similarityValue = similarity.compareBPModelRDFGraphs(processModel.getModelDescription(),
                    model.getModelDescription());

            if (!model.equals(processModel) && similarityValue >= threshold.getThresholdValue()) {
                // Calculate the membership function value.
                double membership = 1.0 / Math.pow(1.0 - similarityValue, 1.0 / (FUZZINESS_COEFF - 1.0));

                clusteringResults.add(new ProcessModelCloseness(model, membership));
                sumOfMemberships += membership;
            }
        }

        for (ProcessModelCloseness processModelCloseness : clusteringResults)
            processModelCloseness.setClosenessValue(processModelCloseness.getClosenessValue() / sumOfMemberships);

        // Descending sort of business process models by fuzzy membership values.
        clusteringResults.sort((x, y) -> Double.compare(y.getClosenessValue(), x.getClosenessValue()));

        return clusteringResults;
    }

    /**
     * Returns the list of models and theirs similarity values.
     *
     * @param processModel - the pattern;
     * @param processModels - the set of business process models stored in repository;
     * @param similarity - the implementation of business process models similarity measure.
     * @return the results of comparison.
     */
    public static List<ProcessModelCloseness> search(GenericProcessModel processModel,
                                                      Collection<GenericProcessModel> processModels,
                                                      CustomizableBPModelsSimilarity similarity) {
        List<ProcessModelCloseness> clusteringResults = new ArrayList<>();

        for (GenericProcessModel model : processModels) {
            double similarityValue = similarity.compareBPModelRDFGraphs(processModel.getModelDescription(),
                    model.getModelDescription());

            if (!model.equals(processModel) && similarityValue > HarringtonScale.VERY_BAD) {
                clusteringResults.add(new ProcessModelCloseness(model, similarityValue));
            }
        }

        clusteringResults.sort((x, y) -> Double.compare(y.getClosenessValue(), x.getClosenessValue()));

        return clusteringResults;
    }

    public static HarringtonScale getThreshold() {
        return threshold;
    }

    public static void setThreshold(HarringtonScale threshold) {
        ProcessModelClustering.threshold = threshold;
    }
}
