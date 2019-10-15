package edu.bpmanalysis.web;

import com.google.gson.Gson;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.RecommendationsUtil;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.partition.ProcessModelAnalysisResultsPartition;
import edu.bpmanalysis.search.pattern.ProcessModelPatternMatchingStorage;
import edu.bpmanalysis.analysis.SummaryAnalysisUtil;
import edu.bpmanalysis.web.model.AnalysisResultsRepositoryJsonDB;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.awt.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class BPMAIApplication {

    public static void main(String[] args) {
        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.cleanRepository();
        }

        ProcessModelRepository processModelRepository = new ProcessModelRepositoryJsonDB();

        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.importModelsFromBPMNDocuments(processModelRepository);
            ProcessModelImportUtil.importModelsFromEPCDocuments(processModelRepository);
            ProcessModelImportUtil.importModelsFromIDEF0Documents(processModelRepository);
            ProcessModelImportUtil.importModelsFromDFDDocuments(processModelRepository);

            ProcessModelImportUtil.importModels();
        }

        ProcessModelPatternMatchingStorage.loadModels(processModelRepository);
        ProcessModelAnalysisResultsPartition.partitionModels(processModelRepository);

        AnalysisResultsRepository analysisResultsRepository = new AnalysisResultsRepositoryJsonDB();

        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(processModelBean);
            ProcessModelAnalysisBean processModelAnalysisBean = ProcessModelAnalysisUtil.analyzeModel(model);

            processModelAnalysisBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
            processModelAnalysisBean.setNotation(processModelBean.getNotation());
            processModelAnalysisBean.setLevel(processModelBean.getLevel());
            processModelAnalysisBean.setGraph(processModelBean.getGraph());
            processModelAnalysisBean.setDescription(processModelBean.getDescription());
            processModelAnalysisBean.setFileName(processModelBean.getFileName());

            processModelAnalysisBean.setRecommendations(RecommendationsUtil
                    .generateRecommendations(processModelAnalysisBean));

            processModelAnalysisBean.setGraph(ProcessModelPatternMatchingStorage
                    .getGraph(model.getId()).getGraph());

            processModelAnalysisBean.setGuidelines(EvaluationUtil.checkGuidelines(model));
            RecommendationsUtil.transformGuidelines(processModelAnalysisBean);

            analysisResultsRepository.addAnalysisResult(processModelAnalysisBean);
        }

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : processModelRepository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        staticFiles.location("/web");

        path("/", () -> {
            get("/bpmai/api", (req, res) ->
                    new Gson().toJson(SummaryAnalysisUtil
                            .getSummaryAnalysisResults(processModelRepository)));
            get("/bpmai/api/models", (req, res) ->
                    new Gson().toJson(analysisResultsRepository.getAnalysisResults()));
            get("/bpmai/api/model/:id", (req, res) ->
                    new Gson().toJson(analysisResultsRepository.getAnalysisResult(req.params(":id"))));
            get("/bpmai/api/partition", (req, res) ->
                    new Gson().toJson(ProcessModelAnalysisResultsPartition.getModelsClusters()));
        });

        System.err.println();
        System.err.println(
                        "▒█▀▀█ ▒█▀▀█ ▒█▀▄▀█ ░█▀▀█ ▀█▀ 　 ▀▀█▀▀ ▒█▀▀▀█ ▒█▀▀▀█ ▒█░░░ \n" +
                        "▒█▀▀▄ ▒█▄▄█ ▒█▒█▒█ ▒█▄▄█ ▒█░ 　 ░▒█░░ ▒█░░▒█ ▒█░░▒█ ▒█░░░ \n" +
                        "▒█▄▄█ ▒█░░░ ▒█░░▒█ ▒█░▒█ ▄█▄ 　 ░▒█░░ ▒█▄▄▄█ ▒█▄▄▄█ ▒█▄▄█");
        System.err.println();
        System.err.println("API is now serving at http://localhost:4567/bpmai/api/");
        System.err.println("Use /models to access all process models");
        System.err.println("Use /model/<MODEL_ID> to access a specific process model");
        System.err.println("Use /partition to access the models partition results");
        System.err.println();

        try {
            Desktop.getDesktop().browse(new URL("http://localhost:4567/bpmai.html").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
