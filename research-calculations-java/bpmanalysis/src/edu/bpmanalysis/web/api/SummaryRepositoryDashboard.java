package edu.bpmanalysis.web.api;

import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.similarity.ProcessModelSimilaritySearchStorage;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SummaryRepositoryDashboard {
    private int numberOfModels;
    private int duplicateModels;
    private int invalidModels;
    private int correctModels;

    private int invalidNodes;
    private int invalidFunctions;
    private int invalidConnectorsBalance;
    private int invalidFunctionsBalance;
    private int invalidStartEvents;
    private int invalidEndEvents;
    private int invalidMatching;
    private int invalidORGateways;

    public SummaryRepositoryDashboard(ProcessModelRepository repository) {
        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : repository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        for (Model model : models) {
            ProcessModelAnalysisBean analysisBean = ProcessModelAnalysisUtil.analyzeModel(model);

            double[] nodesChanges = analysisBean.getNodesChanges();

            if (!Arrays.stream(nodesChanges).allMatch(x -> x == 0) || analysisBean.getConnectorsBalance() > 0 ||
                    analysisBean.getMismatch() > 0 || analysisBean.getFunctionsBalance() > 0) {
                this.invalidModels++;

                if (nodesChanges[0] != 0) {
                    this.invalidNodes++;
                }

                if (nodesChanges[1] != 0) {
                    this.invalidStartEvents++;
                }

                if (nodesChanges[2] != 0) {
                    this.invalidEndEvents++;
                }

                if (nodesChanges[3] != 0) {
                    this.invalidORGateways++;
                }

                if (nodesChanges[4] != 0) {
                    this.invalidFunctions++;
                }

                if (analysisBean.getConnectorsBalance() > 0) {
                    this.invalidConnectorsBalance++;
                }

                if (analysisBean.getFunctionsBalance() > 0) {
                    this.invalidFunctionsBalance++;
                }

                if (analysisBean.getMismatch() > 0) {
                    this.invalidMatching++;
                }
            }
        }

        Set<String> duplicates = ProcessModelSimilaritySearchStorage.getDuplicates(models);

        this.duplicateModels = duplicates.size();

        this.numberOfModels = models.size();
        this.correctModels = this.numberOfModels - this.invalidModels;
    }

    public int getNumberOfModels() {
        return numberOfModels;
    }

    public void setNumberOfModels(int numberOfModels) {
        this.numberOfModels = numberOfModels;
    }

    public int getDuplicateModels() {
        return duplicateModels;
    }

    public void setDuplicateModels(int duplicateModels) {
        this.duplicateModels = duplicateModels;
    }

    public int getInvalidModels() {
        return invalidModels;
    }

    public void setInvalidModels(int invalidModels) {
        this.invalidModels = invalidModels;
    }

    public int getCorrectModels() {
        return correctModels;
    }

    public void setCorrectModels(int correctModels) {
        this.correctModels = correctModels;
    }

    public int getInvalidNodes() {
        return invalidNodes;
    }

    public void setInvalidNodes(int invalidNodes) {
        this.invalidNodes = invalidNodes;
    }

    public int getInvalidFunctions() {
        return invalidFunctions;
    }

    public void setInvalidFunctions(int invalidFunctions) {
        this.invalidFunctions = invalidFunctions;
    }

    public int getInvalidConnectorsBalance() {
        return invalidConnectorsBalance;
    }

    public void setInvalidConnectorsBalance(int invalidConnectorsBalance) {
        this.invalidConnectorsBalance = invalidConnectorsBalance;
    }

    public int getInvalidFunctionsBalance() {
        return invalidFunctionsBalance;
    }

    public void setInvalidFunctionsBalance(int invalidFunctionsBalance) {
        this.invalidFunctionsBalance = invalidFunctionsBalance;
    }

    public int getInvalidStartEvents() {
        return invalidStartEvents;
    }

    public void setInvalidStartEvents(int invalidStartEvents) {
        this.invalidStartEvents = invalidStartEvents;
    }

    public int getInvalidEndEvents() {
        return invalidEndEvents;
    }

    public void setInvalidEndEvents(int invalidEndEvents) {
        this.invalidEndEvents = invalidEndEvents;
    }

    public int getInvalidMatching() {
        return invalidMatching;
    }

    public void setInvalidMatching(int invalidMatching) {
        this.invalidMatching = invalidMatching;
    }

    public int getInvalidORGateways() {
        return invalidORGateways;
    }

    public void setInvalidORGateways(int invalidORGateways) {
        this.invalidORGateways = invalidORGateways;
    }
}
