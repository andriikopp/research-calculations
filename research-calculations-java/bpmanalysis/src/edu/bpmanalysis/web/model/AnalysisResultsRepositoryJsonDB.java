package edu.bpmanalysis.web.model;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import io.jsondb.JsonDBTemplate;

import java.util.List;

public class AnalysisResultsRepositoryJsonDB implements AnalysisResultsRepository {
    private JsonDBTemplate jsonDBTemplate;

    public AnalysisResultsRepositoryJsonDB() {
        this.jsonDBTemplate = new JsonDBTemplate("processModelsStorage/",
                "edu.bpmanalysis.analysis.bean");

        if (!jsonDBTemplate.collectionExists(ProcessModelAnalysisBean.class)) {
            jsonDBTemplate.createCollection(ProcessModelAnalysisBean.class);
        }
    }

    @Override
    public void addAnalysisResult(ProcessModelAnalysisBean processModelAnalysisBean) {
        jsonDBTemplate.insert(processModelAnalysisBean);
    }

    @Override
    public List<ProcessModelAnalysisBean> getAnalysisResults() {
        return jsonDBTemplate.findAll(ProcessModelAnalysisBean.class);
    }

    @Override
    public ProcessModelAnalysisBean getAnalysisResult(String id) {
        return jsonDBTemplate.findById(id, ProcessModelAnalysisBean.class);
    }
}
