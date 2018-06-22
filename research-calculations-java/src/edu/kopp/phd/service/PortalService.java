package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.util.FileUtils;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class PortalService {
    public static final String PROCESSES_PATH = "portal/processes/";
    public static final String TEMPLATE_PROCESS = "portal/templates/tmpl_process.html";
    public static final String HTML_FILE = ".html";

    public static final String WARNING_SIGN = "<span class=\"badge badge-warning\">!</span>";
    public static final String NEXT_LINE = "<br/>";

    private ControlFlowService controlFlowService = new ControlFlowService();
    private ValidationService validationService = new ValidationService();
    private AnalysisService analysisService = new AnalysisService();

    public void deployPortal() {
        for (Process process : controlFlowService.getAllProcesses()) {
            String processName = process.getResource().getLocalName();

            FileUtils.writeFile(PROCESSES_PATH + processName + HTML_FILE, FileUtils.readFile(TEMPLATE_PROCESS,
                    Charset.defaultCharset())
                    .replace("${processName}", processName)
                    .replace("${epcWarnings}", getEPCWarningsLayout(processName))
                    .replace("${arisWarnings}", getARISWarningsLayout(processName))
                    .replace("${analysis}", getEvaluationLayout(processName))
                    .replace("${functionsMetrics}", getFunctionsMetricsLayout(processName))
                    .replace("${nodesArray}", controlFlowService.getJSONNodesRepresentationByProcessName(processName))
                    .replace("${edgesArray}", controlFlowService.getJSONEdgesRepresentationByProcessName(processName)));
        }
    }

    private String getEPCWarningsLayout(String processName) {
        String warnings = "";

        Set<FlowObject> unreachableNodes = validationService.validateNodesCoherenceByProcessName(processName);

        if (!unreachableNodes.isEmpty())
            warnings += WARNING_SIGN + " Unreachable nodes: " + unreachableNodes.toString() + NEXT_LINE;

        if (!validationService.validateStartEndNodesByProcessName(processName))
            warnings += WARNING_SIGN + " Process doesn't have at least one start and end node" + NEXT_LINE;

        Set<Event> invalidEvents = validationService.validateEventsByProcessName(processName);

        if (!invalidEvents.isEmpty())
            warnings += WARNING_SIGN + " Invalid events: " + invalidEvents.toString() + NEXT_LINE;

        Set<Function> invalidFunctions = validationService.validateFunctionsByProcessName(processName);

        if (!invalidFunctions.isEmpty())
            warnings += WARNING_SIGN + " Invalid functions: " + invalidFunctions.toString() + NEXT_LINE;

        Set<Process> invalidProcessInterfaces = validationService.validateProcessesByProcessName(processName);

        if (!invalidProcessInterfaces.isEmpty())
            warnings += WARNING_SIGN + " Invalid process interfaces: " + invalidProcessInterfaces.toString() + NEXT_LINE;

        Set<Gateway> invalidGateways = validationService.validateGatewaysByProcessName(processName);

        if (!invalidGateways.isEmpty())
            warnings += WARNING_SIGN + " Invalid gateways: " + invalidGateways.toString() + NEXT_LINE;

        Set<Event> eventDecisions = validationService.validateEventsAndGatewaysConnectionsByProcessName(processName);

        if (!eventDecisions.isEmpty())
            warnings += WARNING_SIGN + " Events that make decisions: " + eventDecisions.toString() + NEXT_LINE;

        if (warnings.isEmpty())
            warnings = "No warnings";

        return warnings;
    }

    private String getARISWarningsLayout(String processName) {
        String warnings = "";

        for (String warning : analysisService.getFunctionErrorsByProcessName(processName))
            warnings += WARNING_SIGN + " " + warning + NEXT_LINE;

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
            String state = "success";

            double metric = analysisService.getAggregatedIndicatorByProcessName(processName);

            if (metric < 0)
                state = "danger";
            else if (metric > 0)
                state = "warning";

            return "<div class=\"alert alert-" + state + "\" role=\"alert\">" +
                    String.format("%.2f", metric) +
                    "</div>";
        } catch (Exception err) {
            return "N/A";
        }
    }
}
