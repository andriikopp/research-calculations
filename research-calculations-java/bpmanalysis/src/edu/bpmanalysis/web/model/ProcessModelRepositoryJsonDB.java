package edu.bpmanalysis.web.model;

import edu.bpmanalysis.similarity.SearchBasedStorage;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import io.jsondb.JsonDBTemplate;

import java.util.List;

public class ProcessModelRepositoryJsonDB implements ProcessModelRepository {
    private JsonDBTemplate jsonDBTemplate;

    public ProcessModelRepositoryJsonDB() {
        this.jsonDBTemplate = new JsonDBTemplate("processModelsStorage/",
                "edu.bpmanalysis.web.model.bean");

        if (!jsonDBTemplate.collectionExists(ProcessModelBean.class)) {
            jsonDBTemplate.createCollection(ProcessModelBean.class);
        }
    }

    @Override
    public List<ProcessModelBean> getProcessModels() {
        return jsonDBTemplate.findAll(ProcessModelBean.class);
    }

    @Override
    public ProcessModelBean getProcessModel(String id) {
        return jsonDBTemplate.findById(id, ProcessModelBean.class);
    }

    @Override
    public void addProcessModel(ProcessModelBean processModelBean) {
        jsonDBTemplate.insert(processModelBean);

        SearchBasedStorage.addModel(processModelBean);
    }

    @Override
    public void deleteProcessModel(String id) {
        ProcessModelBean processModelBean = new ProcessModelBean();
        processModelBean.setId(id);

        jsonDBTemplate.remove(processModelBean, ProcessModelBean.class);
    }
}
