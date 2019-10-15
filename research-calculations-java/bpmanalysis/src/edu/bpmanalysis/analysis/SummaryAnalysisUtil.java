package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.*;

public class SummaryAnalysisUtil {

    public static Map<String, Integer> getSummaryAnalysisResults(ProcessModelRepository repository) {
        Map<String, Integer> results = new LinkedHashMap<>();

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : repository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        int invalidNodes = 0, invalidStartEvents = 0, invalidEndEvents = 0,
                invalidORGateways = 0, invalidFunctions = 0, invalidConnectorsBalance = 0,
                invalidFunctionsBalance = 0, invalidMatching = 0;

        for (Model model : models) {
            ProcessModelAnalysisBean analysisBean = ProcessModelAnalysisUtil.analyzeModel(model);

            double[] nodesChanges = analysisBean.getNodesChanges();

            if (!Arrays.stream(nodesChanges).allMatch(x -> x == 0) || analysisBean.getConnectorsBalance() > 0 ||
                    analysisBean.getMismatch() > 0 || analysisBean.getFunctionsBalance() > 0) {

                if (nodesChanges[0] != 0) {
                    invalidNodes++;
                }

                if (nodesChanges[1] != 0) {
                    invalidStartEvents++;
                }

                if (nodesChanges[2] != 0) {
                    invalidEndEvents++;
                }

                if (nodesChanges[3] != 0) {
                    invalidORGateways++;
                }

                if (nodesChanges[4] != 0) {
                    invalidFunctions++;
                }

                if (analysisBean.getConnectorsBalance() > 0) {
                    invalidConnectorsBalance++;
                }

                if (analysisBean.getFunctionsBalance() > 0) {
                    invalidFunctionsBalance++;
                }

                if (analysisBean.getMismatch() > 0) {
                    invalidMatching++;
                }
            }
        }

        results.put("Nodes", invalidNodes);
        results.put("Functions", invalidFunctions);
        results.put("Connectors balance", invalidConnectorsBalance);
        results.put("Functions balance", invalidFunctionsBalance);
        results.put("Start events", invalidStartEvents);
        results.put("End events", invalidEndEvents);
        results.put("Mismatch", invalidMatching);
        results.put("ORs", invalidORGateways);

        return results;
    }
}
