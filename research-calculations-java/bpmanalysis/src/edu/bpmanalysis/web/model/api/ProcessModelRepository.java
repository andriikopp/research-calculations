package edu.bpmanalysis.web.model.api;

import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.List;

public interface ProcessModelRepository {

    List<ProcessModelBean> getProcessModels();
    
    void addProcessModel(ProcessModelBean processModelBean);
}
