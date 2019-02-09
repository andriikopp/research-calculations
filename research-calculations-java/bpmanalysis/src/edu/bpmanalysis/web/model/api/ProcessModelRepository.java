package edu.bpmanalysis.web.model.api;

import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.List;

public interface ProcessModelRepository {

    List<ProcessModelBean> getProcessModels();

    ProcessModelBean getProcessModel(String id);

    void addProcessModel(ProcessModelBean processModelBean);

    void deleteProcessModel(String id);
}
