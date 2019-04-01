package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.similarity.GraphStructureSimilarity;
import edu.bpmanalysis.search.similarity.api.Similarity;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.*;

public class SimilarityApp {
    private static Similarity similarity = new GraphStructureSimilarity();

    public static void printSimilarModels(Model first, Model second) {
        double value = similarity.compare(first, second);

        if (value >= Configuration.SIMILARITY_THRESHOLD && !first.getName().equals(second.getName())) {
            System.out.printf("%s\t%s\t%.2f\n",
                    first.getName(),
                    second.getName(),
                    value);
        }
    }

    public static void main(String[] args) {
        ProcessModelRepository repository = new ProcessModelRepositoryJsonDB();

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : repository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        for (Model first : models) {
            for (Model second : models) {
                printSimilarModels(first, second);
            }
        }
    }
}
