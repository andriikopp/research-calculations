package edu.bpmanalysis.web.api;

import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.ArrayList;
import java.util.List;

public class DetailedRepositoryDashboard {
    private List<ProcessModelAnalysisBean> repositoryDashboardList;

    public DetailedRepositoryDashboard(ProcessModelRepository repository) {
        this.repositoryDashboardList = new ArrayList<>();

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : repository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        for (Model model : models) {
            ProcessModelAnalysisBean analysisBean = ProcessModelAnalysisUtil.analyzeModel(model);
            repositoryDashboardList.add(analysisBean);
        }
    }

    public List<ProcessModelAnalysisBean> getRepositoryDashboardList() {
        return repositoryDashboardList;
    }

    public void setRepositoryDashboardList(List<ProcessModelAnalysisBean> repositoryDashboardList) {
        this.repositoryDashboardList = repositoryDashboardList;
    }
}
