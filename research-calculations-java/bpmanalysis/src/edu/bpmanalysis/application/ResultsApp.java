package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.IndicatorsUtil;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.description.tools.Model;
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
            IndicatorsUtil.printIndicators(model);
        }

        for (Model first : models) {
            for (Model second : models) {
                SimilarityApp.printSimilarModels(first, second);
            }
        }
    }
}
