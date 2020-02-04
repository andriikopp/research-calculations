package edu.bpmanalysis.web;

import com.google.gson.Gson;
import edu.bpmanalysis.analysis.GraphMetricsUtil;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.RecommendationsUtil;
import edu.bpmanalysis.analysis.SummaryAnalysisUtil;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.partition.ProcessModelAnalysisResultsPartition;
import edu.bpmanalysis.search.graph.ProcessModelRDFGraphStorage;
import edu.bpmanalysis.web.model.AnalysisResultsRepositoryMySQL;
import edu.bpmanalysis.web.model.PartitionRepositoryMySQL;
import edu.bpmanalysis.web.model.RecommendationsRepositoryMySQL;
import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import edu.bpmanalysis.web.model.api.PartitionRepository;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.api.RecommendationsRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.awt.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static spark.Spark.*;

public class BPMAIApplication {
    public static final String TIME_STAMP_ID = UUID.randomUUID().toString();

    public static void main(String[] args) {
        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.cleanRepository();
        }

        ProcessModelRepository processModelRepository = Configuration.MODEL_STORAGE;

        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.importModelsFromBPMNDocuments(processModelRepository);
            ProcessModelImportUtil.importModelsFromEPCDocuments(processModelRepository);
            ProcessModelImportUtil.importModelsFromIDEF0Documents(processModelRepository);
            ProcessModelImportUtil.importModelsFromDFDDocuments(processModelRepository);

            ProcessModelImportUtil.importModels();
        }

        ProcessModelRDFGraphStorage.loadModels(processModelRepository);
        ProcessModelAnalysisResultsPartition.partitionModels(processModelRepository);

        AnalysisResultsRepository analysisResultsRepository = Configuration.ANALYSIS_STORAGE;
        RecommendationsRepository recommendationsRepository = null;
        PartitionRepository partitionRepository = null;

        if (analysisResultsRepository instanceof AnalysisResultsRepositoryMySQL) {
            recommendationsRepository = new RecommendationsRepositoryMySQL();
            partitionRepository = new PartitionRepositoryMySQL();
        }

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
            processModelAnalysisBean.setGraph(ProcessModelRDFGraphStorage
                    .getGraph(model.getId()).getGraph());
            processModelAnalysisBean.setGuidelines(EvaluationUtil.checkGuidelines(model));

            analysisResultsRepository.addAnalysisResult(processModelAnalysisBean);

            if (analysisResultsRepository instanceof AnalysisResultsRepositoryMySQL) {
                recommendationsRepository.addRecommendations(processModelAnalysisBean);
                partitionRepository.addPartition(processModelAnalysisBean);
            }
        }

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : processModelRepository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        GraphMetricsUtil.analyzeModels(processModelRepository);
        GraphMetricsUtil.measurePerformance(processModelRepository);

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
                    new Gson().toJson(ProcessModelAnalysisResultsPartition.getModelGroups()));
        });

        System.out.println();
        System.out.println(
                "▒█▀▀█ ▒█▀▀█ ▒█▀▄▀█ ░█▀▀█ ▀█▀ 　 ▀▀█▀▀ ▒█▀▀▀█ ▒█▀▀▀█ ▒█░░░ \n" +
                        "▒█▀▀▄ ▒█▄▄█ ▒█▒█▒█ ▒█▄▄█ ▒█░ 　 ░▒█░░ ▒█░░▒█ ▒█░░▒█ ▒█░░░ \n" +
                        "▒█▄▄█ ▒█░░░ ▒█░░▒█ ▒█░▒█ ▄█▄ 　 ░▒█░░ ▒█▄▄▄█ ▒█▄▄▄█ ▒█▄▄█");
        System.out.println();
        System.out.println("API is now serving at http://localhost:4567/bpmai/api/");
        System.out.println("Use /models to access all process models");
        System.out.println("Use /model/<MODEL_ID> to access a specific process model");
        System.out.println("Use /partition to access the models partition results");
        System.out.println();
    }
}
