package edu.bpmanalysis.web.model.api;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;

public interface RecommendationsRepository {

    void addRecommendations(ProcessModelAnalysisBean processModelAnalysisBean);
}
