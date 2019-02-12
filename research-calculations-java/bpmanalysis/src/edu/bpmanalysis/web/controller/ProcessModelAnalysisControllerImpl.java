package edu.bpmanalysis.web.controller;

import com.google.gson.Gson;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.ProcessModelImportUtil;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.search.pattern.ProcessModelPatternMatchingStorage;
import edu.bpmanalysis.search.similarity.ProcessModelSimilaritySearchStorage;
import edu.bpmanalysis.web.controller.api.ProcessModelAnalysisController;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import spark.Route;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static spark.Spark.*;

public class ProcessModelAnalysisControllerImpl implements ProcessModelAnalysisController {
    private ProcessModelRepository repository;

    public Route models = (req, res) -> {
        List<ProcessModelBean> processModels = repository.getProcessModels();
        Collections.reverse(processModels);

        return new Gson().toJson(processModels);
    };

    public Route retrieve = (req, res) -> {
        String id = req.params(":id");
        ProcessModelBean processModelBean = repository.getProcessModel(id);
        return new Gson().toJson(processModelBean);
    };

    public Route store = (req, res) -> {
        String json = req.body();

        ProcessModelBean processModelBean = new Gson().fromJson(json, ProcessModelBean.class);
        processModelBean.setId(UUID.randomUUID().toString());
        processModelBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());

        repository.addProcessModel(processModelBean);

        return null;
    };

    public Route analyze = (req, res) -> {
        String id = req.params(":id");
        ProcessModelBean processModelBean = repository.getProcessModel(id);

        Model model = ProcessModelAnalysisUtil.transformToModel(processModelBean);
        ProcessModelAnalysisBean processModelAnalysisBean = ProcessModelAnalysisUtil.analyzeModel(model);

        return new Gson().toJson(processModelAnalysisBean);
    };

    public Route remove = (req, res) -> {
        String id = req.params(":id");

        repository.deleteProcessModel(id);

        res.redirect("/");
        return null;
    };

    public Route search = (req, res) -> {
        String type = req.params("type");
        String node = req.params("node");

        return new Gson().toJson(ProcessModelPatternMatchingStorage.match(type + "#" + node));
    };

    public Route searchAll = (req, res) ->
            new Gson().toJson(ProcessModelPatternMatchingStorage.match(null));

    @Override
    public void init() {
        ProcessModelSimilaritySearchStorage.loadModels(repository);

        ProcessModelPatternMatchingStorage.loadModels(repository);

        staticFiles.location("/web");

        path("/", () -> {
            get("/models", models);
            get("/retrieve/:id", retrieve);
            post("/store", store);
            get("/analyze/:id", analyze);
            get("/remove/:id", remove);
            get("/search/:type/:node", search);
            get("/search", searchAll);
        });
    }

    @Override
    public void setRepository(ProcessModelRepository repository) {
        this.repository = repository;
    }
}
