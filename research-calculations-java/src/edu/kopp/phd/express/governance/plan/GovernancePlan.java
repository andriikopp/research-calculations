package edu.kopp.phd.express.governance.plan;

import edu.kopp.phd.express.governance.plan.similarity.ISimilarity;
import edu.kopp.phd.express.governance.plan.similarity.JaccardSimilarity;
import edu.kopp.phd.express.governance.plan.similarity.patterns.SearchPatterns;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GovernancePlan {
    public static final ISimilarity SIMILARITY = new JaccardSimilarity();

    private List<ModelFeatures> modelFeaturesList;

    public GovernancePlan(List<ModelFeatures> landscapeClusters) {
        this.modelFeaturesList = landscapeClusters;
    }

    public void plan(SearchPatterns searchPatterns) {
        for (Map.Entry<String, int[]> entry : searchPatterns.patterns().entrySet()) {
            System.out.println(entry.getKey());

            List<ModelRelevance> relevantModels = new LinkedList<>();

            for (ModelFeatures modelFeatures : modelFeaturesList) {
                double similarity = SIMILARITY.similarity(modelFeatures.getBinaryFeatures(), entry.getValue());

                if (similarity > 0) {
                    relevantModels.add(new ModelRelevance(modelFeatures.getModel(), similarity));
                }
            }

            relevantModels.sort((a, b) -> Double.compare(b.getRelevance(), a.getRelevance()));

            for (ModelRelevance modelRelevance : relevantModels) {
                System.out.println(modelRelevance);
            }
        }
    }

    public List<ModelFeatures> getModelFeaturesList() {
        return modelFeaturesList;
    }

    public void setModelFeaturesList(List<ModelFeatures> modelFeatures) {
        this.modelFeaturesList = modelFeatures;
    }
}
