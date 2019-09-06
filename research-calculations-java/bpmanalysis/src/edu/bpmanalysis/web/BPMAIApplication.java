package edu.bpmanalysis.web;

import com.google.gson.Gson;
import edu.bpmanalysis.analysis.GraphMetricsUtil;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.partition.ProcessModelAnalysisResultsPartition;
import edu.bpmanalysis.search.pattern.ProcessModelPatternMatchingStorage;
import edu.bpmanalysis.search.similarity.ProcessModelSimilaritySearchStorage;
import edu.bpmanalysis.web.api.SummaryAnalysisBean;
import edu.bpmanalysis.web.model.AnalysisResultsRepositoryJsonDB;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.awt.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
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

            processModelAnalysisBean.setRecommendations(generateRecommendations(processModelAnalysisBean));

            processModelAnalysisBean.setGraph(ProcessModelPatternMatchingStorage.getGraph(model.getId()).getGraph());

            processModelAnalysisBean.setGuidelines(EvaluationUtil.checkGuidelines(model));
            transformGuidelines(processModelAnalysisBean);

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
                    new Gson().toJson(new SummaryAnalysisBean(processModelRepository).getResults()));
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
        System.err.println();

        if (Configuration.CALCULATE_METRICS) {
            GraphMetricsUtil.analyzeModels(processModelRepository);
        }

        if (Configuration.MEASURE_PERFORMANCE) {
            GraphMetricsUtil.measurePerformance(processModelRepository);
        }

        try {
            Desktop.getDesktop().browse(new URL("http://localhost:4567/bpmai.html").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void transformGuidelines(ProcessModelAnalysisBean processModelAnalysisBean) {
        for (int i = 0; i < processModelAnalysisBean.getGuidelines().length; i++) {
            double value = processModelAnalysisBean.getGuidelines()[i];

            if (value < 1) {
                processModelAnalysisBean.getGuidelines()[i] = -1;
            }
        }
    }

    private static List<String> generateRecommendations(ProcessModelAnalysisBean processModelAnalysisBean) {
        List<String> recommendations = new ArrayList<>();

        double[] nodesChanges = processModelAnalysisBean.getNodesChanges();

        Map<String, Integer> descriptionOfNodesChanges = new LinkedHashMap<>();

        descriptionOfNodesChanges.put("element(s)", (int) nodesChanges[0]);
        descriptionOfNodesChanges.put("function(s)", (int) nodesChanges[4]);
        descriptionOfNodesChanges.put("start event(s)", (int) nodesChanges[1]);
        descriptionOfNodesChanges.put("end event(s)", (int) nodesChanges[2]);
        descriptionOfNodesChanges.put("OR routing element(s)", (int) nodesChanges[3]);

        for (Map.Entry<String, Integer> value : descriptionOfNodesChanges.entrySet()) {
            if (value.getValue() != 0) {
                if (value.getValue() > 0) {
                    recommendations.add("Add " + value.getValue() + " " + value.getKey());
                } else {
                    recommendations.add("Remove " + Math.abs(value.getValue()) + " " + value.getKey());
                }
            }
        }

        for (Map.Entry<String, Double> entry : processModelAnalysisBean.getConnectorsChanges().entrySet()) {
            if (entry.getValue() != 0) {
                if (entry.getValue() > 0) {
                    recommendations.add("Add " + (int) entry.getValue().doubleValue() + " arc(s) to '" +
                            entry.getKey() + "' connector");
                } else {
                    recommendations.add("Remove " + (int) Math.abs(entry.getValue()) + " arc(s) from '" +
                            entry.getKey() + "' connector");
                }
            }
        }

        Map<Integer, String> arcTypeMapping = new HashMap<>();

        arcTypeMapping.put(0, "input");
        arcTypeMapping.put(1, "output");
        arcTypeMapping.put(2, "control");
        arcTypeMapping.put(3, "mechanism");

        for (Map.Entry<String, double[]> entry : processModelAnalysisBean.getFunctionsChanges().entrySet()) {
            for (int i = 0; i < entry.getValue().length; i++) {
                if (entry.getValue()[i] != 0) {
                    if (entry.getValue()[i] > 0) {
                        recommendations.add("Add " + (int) entry.getValue()[i] + " " +
                                arcTypeMapping.get(i) + " arc(s) to '" +
                                entry.getKey() + "' function");
                    } else {
                        recommendations.add("Remove " + (int) Math.abs(entry.getValue()[i]) + " " +
                                arcTypeMapping.get(i) + " arc(s) from '" +
                                entry.getKey() + "' function");
                    }
                }
            }
        }

        for (Map.Entry<String, double[]> entry : processModelAnalysisBean.getRoutingChanges().entrySet()) {
            if (entry.getValue()[0] != 0 || entry.getValue()[1] != 0) {
                String splitAction = entry.getValue()[0] > 0 ? "Add" : "Remove";
                String joinAction = entry.getValue()[1] > 0 ? "add" : "remove";

                recommendations.add(splitAction + " " + (int) Math.abs(entry.getValue()[0]) + " " +
                        entry.getKey() + "-split connector(s) or " + joinAction + " " +
                        (int) Math.abs(entry.getValue()[1]) + " " + entry.getKey() + "-join connector(s)");
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("No changes required");
        }

        return recommendations;
    }
}
