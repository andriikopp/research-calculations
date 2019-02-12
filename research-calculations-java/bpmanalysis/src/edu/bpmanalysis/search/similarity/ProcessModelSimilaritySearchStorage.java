package edu.bpmanalysis.search.similarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.bpmanalysis.analysis.ModelDensity;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.search.similarity.api.Similarity;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.LinkedList;
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

        for (Model storedModel : modelList) {
            double distance = 1.0 - similarity.compare(model, storedModel);

            if (distance == 0 && !model.getId().equals(storedModel.getId())) {
                duplicateModelsIDs.put(storedModel.getId(), storedModel.getName());
            }
        }

        return duplicateModelsIDs;
    }
}