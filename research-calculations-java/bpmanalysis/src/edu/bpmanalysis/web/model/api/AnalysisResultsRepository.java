package edu.bpmanalysis.web.model.api;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;

import java.util.List;

public interface AnalysisResultsRepository {

    void addAnalysisResult(ProcessModelAnalysisBean processModelAnalysisBean);

    List<ProcessModelAnalysisBean> getAnalysisResults(String userName);

    List<ProcessModelAnalysisBean> getAnalysisResults();

    ProcessModelAnalysisBean getAnalysisResult(String id);
}
