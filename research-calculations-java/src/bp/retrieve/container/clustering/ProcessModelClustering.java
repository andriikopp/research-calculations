package bp.retrieve.container.clustering;

import bp.retrieve.CustomizableBPModelsSimilarity;
import bp.retrieve.collection.GenericProcessModel;

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

        for (GenericProcessModel model : processModels)
            if (!model.equals(processModel)) {
                // Calculate the membership function value.
                double membership = 1.0 / Math.pow(1.0 - similarity.compareBPModelRDFGraphs(processModel.getModelDescription(),
                        model.getModelDescription()), 1.0 / (FUZZINESS_COEFF - 1.0));

                clusteringResults.add(new ProcessModelCloseness(model, membership));
                sumOfMemberships += membership;
            }

        for (ProcessModelCloseness processModelCloseness : clusteringResults)
            processModelCloseness.setClosenessValue(processModelCloseness.getClosenessValue() / sumOfMemberships);

        // Descending sort of business process models by fuzzy membership values.
        clusteringResults.sort((x, y) -> Double.compare(y.getClosenessValue(), x.getClosenessValue()));

        return clusteringResults;
    }
}
