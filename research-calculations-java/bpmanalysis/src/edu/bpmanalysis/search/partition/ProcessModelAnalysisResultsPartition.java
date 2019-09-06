package edu.bpmanalysis.search.partition;

import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.*;

public class ProcessModelAnalysisResultsPartition {
    public static Map<String, List<Model>> clusters = new HashMap<>();

    public static Map<String, String> getPartitionedModels(Model model) {
        Map<String, String> partitionModelsIDs = new HashMap<>();

        String modelDescription = getModelDescription(model);
        List<Model> modelList = clusters.get(modelDescription);

        if (modelList != null) {
            for (Model clusteredModel : modelList) {

                if (!model.getId().equals(clusteredModel.getId())) {
                    partitionModelsIDs.put(clusteredModel.getId(), clusteredModel.getName());
                }
            }
        }

        return partitionModelsIDs;
    }

    public static void partitionModels(ProcessModelRepository processModelRepository) {
        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(processModelBean);

            String modelDescription = getModelDescription(model);
            List<Model> modelList = clusters.get(modelDescription);

            if (modelList == null) {
                modelList = new ArrayList<>();
                modelList.add(model);

                clusters.put(modelDescription, modelList);
            } else {
                modelList.add(model);
            }
        }
    }

    public static String getModelDescription(Model model) {
        double[] modelDescriptionArray = EvaluationUtil.checkGuidelines(model);
        String modelDescription = "";

        for (double property : modelDescriptionArray) {
            modelDescription += String.valueOf((int) property);
        }

        return modelDescription;
    }

    public static double[] getModelDescriptionArray(String modelDescription) {
        double[] features = new double[modelDescription.length()];

        for (int i = 0; i < modelDescription.length(); i++) {
            features[i] = Double.valueOf(String.valueOf(modelDescription.charAt(i)));
        }

        return features;
    }

    public static Map<String, Integer> getModelsClusters() {
        Map<String, Integer> results = new LinkedHashMap<>();

        for (Map.Entry<String, List<Model>> entry : clusters.entrySet()) {
            double[] features = getModelDescriptionArray(entry.getKey());

            String categoryName = "";

            if (features[0] == 0) {
                categoryName += "Elements";
            }

            if (features[1] == 0) {
                if (categoryName.isEmpty()) {
                    categoryName += "Degrees";
                } else {
                    categoryName += ", degrees";
                }
            }

            if (features[2] == 0) {
                if (categoryName.isEmpty()) {
                    categoryName += "Events";
                } else {
                    categoryName += ", events";
                }
            }

            if (features[3] == 0) {
                if (categoryName.isEmpty()) {
                    categoryName += "Mismatch";
                } else {
                    categoryName += ", mismatch";
                }
            }

            if (features[4] == 0) {
                if (categoryName.isEmpty()) {
                    categoryName += "ORs";
                } else {
                    categoryName += ", ORs";
                }
            }

            if (categoryName.isEmpty()) {
                categoryName += "Correct models";
            }

            results.put(categoryName, entry.getValue().size());
        }

        return results;
    }
}
