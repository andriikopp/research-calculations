package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.IndicatorsUtil;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.similarity.ProcessModelSimilaritySearchStorage;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.ArrayList;
import java.util.List;

public class ResultsApp {
    private static ProcessModelRepository repository;

    public static void main(String[] args) {
        repository = new ProcessModelRepositoryJsonDB();

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : repository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        for (Model model : models) {
            int duplicates = ProcessModelSimilaritySearchStorage.findDuplicates(model).size();

            if (duplicates == 0) {
                ProcessModelSimilaritySearchStorage.addModel(model);

                IndicatorsUtil.printIndicators(model);
            } else {
                System.out.printf("%s\t%d\n", model.getName(), duplicates);
            }
        }
    }
}
