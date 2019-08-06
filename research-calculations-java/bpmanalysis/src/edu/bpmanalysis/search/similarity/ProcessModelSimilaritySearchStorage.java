package edu.bpmanalysis.search.similarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.similarity.api.Similarity;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.*;

public class ProcessModelSimilaritySearchStorage {
    private static Table<Integer, Integer, List<Model>> modelTable = HashBasedTable.create();

    private static Similarity similarity = new GraphStructureSimilarity();

    public static void loadModels(ProcessModelRepository processModelRepository) {
        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            addModel(processModelBean);
        }
    }

    public static void loadModels(List<Model> models) {
        for (Model model : models) {
            addModel(model);
        }
    }

    public static Set<String> getDuplicates(List<Model> models) {
        Set<String> duplicates = new HashSet<>();

        for (Model model : models) {
            if (!duplicates.contains(model.getId())) {
                duplicates.addAll(findDuplicates(model).values());
            }
        }

        return duplicates;
    }

    public static void addModel(ProcessModelBean processModelBean) {
        Model model = ProcessModelAnalysisUtil.transformToModel(processModelBean);

        addModel(model);
    }

    public static void addModel(Model model) {
        int size = (int) ModelDensity.size(model);
        int arcs = (int) ModelDensity.arcs(model);

        List<Model> modelList = modelTable.get(size, arcs);

        if (modelList == null) {
            List<Model> list = new LinkedList<>();
            list.add(model);

            modelTable.put(size, arcs, list);
        } else {
            modelList.add(model);
        }
    }

    public static Map<String, String> findDuplicates(Model model) {
        int size = (int) ModelDensity.size(model);
        int arcs = (int) ModelDensity.arcs(model);

        List<Model> modelList = modelTable.get(size, arcs);

        Map<String, String> duplicateModelsIDs = new HashMap<>();

        if (modelList != null) {
            for (Model storedModel : modelList) {
                double distance = 1.0 - similarity.compare(model, storedModel);

                if (distance <= (1.0 - Configuration.SIMILARITY_THRESHOLD) &&
                        !model.getId().equals(storedModel.getId())) {
                    duplicateModelsIDs.put(storedModel.getId(), storedModel.getName());
                }
            }
        }

        return duplicateModelsIDs;
    }

    public static Table<Integer, Integer, List<Model>> getModelTable() {
        return modelTable;
    }

    public static void setModelTable(Table<Integer, Integer, List<Model>> modelTable) {
        ProcessModelSimilaritySearchStorage.modelTable = modelTable;
    }

    public static Similarity getSimilarity() {
        return similarity;
    }

    public static void setSimilarity(Similarity similarity) {
        ProcessModelSimilaritySearchStorage.similarity = similarity;
    }
}
