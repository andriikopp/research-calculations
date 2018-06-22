package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.Function;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.util.FileUtils;

import java.nio.charset.Charset;
import java.util.Map;

public class PortalService {
    public static final String PROCESSES_PATH = "portal/processes/";
    public static final String TEMPLATE_PROCESS = "portal/templates/tmpl_process.html";
    public static final String HTML_FILE = ".html";

    private ControlFlowService controlFlowService = new ControlFlowService();
    private ValidationService validationService = new ValidationService();
    private AnalysisService analysisService = new AnalysisService();

    public void deployPortal() {
        for (Process process : controlFlowService.getAllProcesses()) {
            String processName = process.getResource().getLocalName();

            FileUtils.writeFile(PROCESSES_PATH + processName + HTML_FILE, FileUtils.readFile(TEMPLATE_PROCESS,
                    Charset.defaultCharset())
                    .replace("${processName}", processName)
                    .replace("${unreachableNodes}", validationService.validateNodesCoherenceByProcessName(processName).toString())
                    .replace("${startEndExist}", String.valueOf(validationService.validateStartEndNodesByProcessName(processName)))
                    .replace("${invalidEvents}", validationService.validateEventsByProcessName(processName).toString())
                    .replace("${invalidFunctions}", validationService.validateFunctionsByProcessName(processName).toString())
                    .replace("${invalidProcessInterfaces}", validationService.validateProcessesByProcessName(processName).toString())
                    .replace("${invalidGateways}", validationService.validateGatewaysByProcessName(processName).toString())
                    .replace("${eventDecisions}", validationService.validateEventsAndGatewaysConnectionsByProcessName(processName).toString())
                    .replace("${arisWarnings}", getWarningsLayout(processName))
                    .replace("${analysis}", getEvaluationLayout(processName))
                    .replace("${functionsMetrics}", getFunctionsMetricsLayout(processName))
                    .replace("${nodesArray}", controlFlowService.getJSONNodesRepresentationByProcessName(processName))
                    .replace("${edgesArray}", controlFlowService.getJSONEdgesRepresentationByProcessName(processName)));
        }
    }

    private String getWarningsLayout(String processName) {
        String warnings = "";

        for (String warning : analysisService.getFunctionErrorsByProcessName(processName))
            warnings += warning + "<br/>";

        if (warnings.isEmpty())
            warnings = "No warnings";

        return warnings;
    }

    private String getFunctionsMetricsLayout(String processName) {
        String functionsMetrics = "";

        for (Map.Entry<Function, Map<String, Double>> entry : analysisService.getFunctionIndicatorsByProcessName(processName)
                .entrySet()) {
            String state = "success";

            double metric = entry.getValue().get("Agg.");

            if (metric < 0)
                state = "danger";
            else if (metric > 0)
                state = "warning";

            functionsMetrics += "<div class=\"alert alert-" + state + "\" role=\"alert\">" +
                    "Evaluation of the function '" + entry.getKey().getResource().getLocalName() + "' is " + String.format("%.2f", metric) +
                    "</div>";
        }

        if (functionsMetrics.isEmpty())
            functionsMetrics = "N/A";

        return functionsMetrics;
    }

    private String getEvaluationLayout(String processName) {
        try {
            return String.format("%.2f", analysisService.getAggregatedIndicatorByProcessName(processName));
        } catch (Exception err) {
            return "N/A";
        }
    }
}
