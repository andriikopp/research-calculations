package edu.kopp.phd.view;

import edu.kopp.phd.model.environment.ApplicationSystem;
import edu.kopp.phd.model.environment.BusinessObject;
import edu.kopp.phd.model.environment.OrganizationalUnit;
import edu.kopp.phd.model.flow.*;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.service.AnalysisService;
import edu.kopp.phd.service.ControlFlowService;
import edu.kopp.phd.service.SimilarityService;
import edu.kopp.phd.service.ValidationService;
import edu.kopp.phd.util.FileUtils;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class PortalView {
    public static final String PROCESSES_PATH = "portal/pages/";

    public static final String TEMPLATE_PROCESS = "portal/templates/tmpl_process.html";
    public static final String TEMPLATE_INDEX = "portal/templates/tmpl_index.html";

    public static final String HTML_FILE = ".html";

    public static final String WARNING_SIGN = "<span class=\"badge badge-warning\">!</span>";
    public static final String ERROR_SIGN = "<span class=\"badge badge-danger\">!</span>";

    public static final String NEXT_LINE = "<br/>";
    public static final String LINE = "<hr/>";

    public static final String REDUNDANT = "Redundant";
    public static final String REQUIRED = "Required";

    private static final Logger LOGGER = Logger.getLogger(PortalView.class);

    private ControlFlowService controlFlowService = new ControlFlowService();

    private ValidationService validationService;
    private AnalysisService analysisService;
    private SimilarityService similarityService;

    public void deployPortal() {
        String updatedDate = getUpdatedDateLayout();

        FileUtils.writeFile(PROCESSES_PATH + "index" + HTML_FILE, FileUtils.readFile(TEMPLATE_INDEX,
                Charset.defaultCharset())
                .replace("${updatedDate}", updatedDate)
                .replace("${processes}", getHomeLayout()));

        for (Process process : controlFlowService.getAllProcesses()) {
            String processName = process.getResource().getLocalName();

            FileUtils.writeFile(PROCESSES_PATH + processName + HTML_FILE, FileUtils.readFile(TEMPLATE_PROCESS,
                    Charset.defaultCharset())
                    .replace("${updatedDate}", updatedDate)
                    .replace("${processName}", processName)
                    .replace("${epcWarnings}", getEPCWarningsLayout(processName))
                    .replace("${cscCoeff}", getCSCCoeffLayout(processName))
                    .replace("${arisWarnings}", getARISWarningsLayout(processName))
                    .replace("${analysis}", getEvaluationLayout(processName))
                    .replace("${recommendations}", getRecommendationsLayout(processName))
                    .replace("${functionsMetrics}", getFunctionsMetricsLayout(processName))
                    .replace("${nodesArray}", controlFlowService.getJSONNodesRepresentationByProcessName(processName))
                    .replace("${edgesArray}", controlFlowService.getJSONEdgesRepresentationByProcessName(processName))
                    .replace("${similarModels}", getSimilarModelsLayout(process)));
        }
    }

    private String getUpdatedDateLayout() {
        return ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    private String getEPCWarningsLayout(String processName) {
        try {
            String warnings = "";

            Set<FlowObject> unreachableNodes = validationService.validateNodesCoherenceByProcessName(processName);

            if (!unreachableNodes.isEmpty())
                warnings += ERROR_SIGN + " Unreachable nodes: " + unreachableNodes.toString() + NEXT_LINE;

            if (!validationService.validateStartEndNodesByProcessName(processName))
                warnings += ERROR_SIGN + " Process doesn't have at least one start and end node" + NEXT_LINE;

            Set<Event> invalidEvents = validationService.validateEventsByProcessName(processName);

            if (!invalidEvents.isEmpty())
                warnings += ERROR_SIGN + " Invalid events: " + invalidEvents.toString() + NEXT_LINE;

            invalidEvents = validationService.getEventsThatArePrecedingForAnotherEventsByProcessName(processName);

            if (!invalidEvents.isEmpty())
                warnings += ERROR_SIGN + " Invalid sequence of events: " + invalidEvents.toString() + NEXT_LINE;

            Set<Function> invalidFunctions = validationService.validateFunctionsByProcessName(processName);

            if (!invalidFunctions.isEmpty())
                warnings += ERROR_SIGN + " Invalid functions: " + invalidFunctions.toString() + NEXT_LINE;

            if (validationService.processDoesNotHaveAtLeastOneFunction(processName))
                warnings += ERROR_SIGN + " Process doesn't have at least one function" + NEXT_LINE;

            Set<Process> invalidProcessInterfaces = validationService.validateProcessesByProcessName(processName);

            if (!invalidProcessInterfaces.isEmpty())
                warnings += ERROR_SIGN + " Invalid process interfaces: " + invalidProcessInterfaces.toString() + NEXT_LINE;

            Set<Gateway> invalidGateways = validationService.validateGatewaysByProcessName(processName);

            if (!invalidGateways.isEmpty())
                warnings += ERROR_SIGN + " Invalid gateways: " + invalidGateways.toString() + NEXT_LINE;

            Set<Event> eventDecisions = validationService.validateEventsAndGatewaysConnectionsByProcessName(processName);

            if (!eventDecisions.isEmpty())
                warnings += ERROR_SIGN + " Events that make decisions: " + eventDecisions.toString() + NEXT_LINE;

            if (warnings.isEmpty())
                warnings = "No errors";

            return warnings;
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    private String getCSCCoeffLayout(String processName) {
        try {
            String state = "success";

            double metric = analysisService.getCSCCoefficientByProcessName(processName);

            // EPC model optimization results.
            int fixUnreachableNodes = validationService.validateNodesCoherenceByProcessName(processName).size();

            int addStartNode = validationService.getStartNodesByProcessName(processName).isEmpty() ? 1 : 0;
            int addEndNode = validationService.getEndNodesByProcessName(processName).isEmpty() ? 1 : 0;
            int addFunction = validationService.processDoesNotHaveAtLeastOneFunction(processName) ? 1 : 0;

            int fixEvents = validationService.validateEventsByProcessName(processName).size();
            int fixFunctions = validationService.validateFunctionsByProcessName(processName).size();
            int fixProcesses = validationService.validateProcessesByProcessName(processName).size();
            int fixConnectors = validationService.validateGatewaysByProcessName(processName).size();

            int fixInvalidConnections = validationService.validateEventsAndGatewaysConnectionsByProcessName(processName).size();
            int fixSequencesOfEvents = validationService.getEventsThatArePrecedingForAnotherEventsByProcessName(processName).size();

            if (metric > 0)
                state = "danger";

            return "<div class=\"alert alert-" + state + "\" role=\"alert\">" +
                    String.format("%.2f", analysisService.getWeightedProcessFlowCoefficient()) +
                    NEXT_LINE +
                    String.format("Errors: %.0f", metric) +
                    LINE +
                    "<small>" +
                    // Detail errors.
                    String.format("Unreachable nodes: %d", fixUnreachableNodes) +
                    NEXT_LINE +
                    String.format("Missing functions: %d", addFunction) +
                    NEXT_LINE +
                    String.format("Missing start nodes: %d", addStartNode) +
                    NEXT_LINE +
                    String.format("Missing end nodes: %d", addEndNode) +
                    NEXT_LINE +
                    String.format("Invalid events: %d", fixEvents) +
                    NEXT_LINE +
                    String.format("Invalid functions: %d", fixFunctions) +
                    NEXT_LINE +
                    String.format("Invalid process interfaces: %d", fixProcesses) +
                    NEXT_LINE +
                    String.format("Invalid gateways: %d", fixConnectors) +
                    NEXT_LINE +
                    String.format("Events making decisions: %d", fixInvalidConnections) +
                    NEXT_LINE +
                    String.format("Sequences of events: %d", fixSequencesOfEvents) +
                    "</small></div>";
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    private String getARISWarningsLayout(String processName) {
        try {
            String warnings = "";

            for (String error : analysisService.getFunctionErrorsByProcessName(processName))
                warnings += ERROR_SIGN + " " + error + NEXT_LINE;

            for (String warning : analysisService.getFunctionWarningsByProcessName(processName))
                warnings += WARNING_SIGN + " " + warning + NEXT_LINE;

            if (warnings.isEmpty())
                warnings = "No warnings or errors";

            return warnings;
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    private String getFunctionsMetricsLayout(String processName) {
        try {
            String functionsMetrics = "";

            for (Map.Entry<Function, Map<String, Double>> entry : analysisService.getFunctionIndicatorsByProcessName(processName)
                    .entrySet()) {
                String state = "success";

                double metric = entry.getValue().get("Agg.");

                if (metric < 0)
                    state = "danger";
                else if (metric > 0)
                    state = "warning";

                // ARIS environment optimization results.
                int fixOrgUnits = 1 - entry.getKey().getOrganizationalUnits().size();
                int fixInputs = 1 - entry.getKey().getInputs().size();
                int fixOutputs = 1 - entry.getKey().getOutputs().size();
                int fixAppSystems = 1 - entry.getKey().getApplicationSystems().size();

                functionsMetrics += "<div class=\"alert alert-" + state + "\" role=\"alert\">" +
                        "Evaluation of the function '" + entry.getKey().getResource().getLocalName() + "' is " + String.format("%.2f", metric) +
                        LINE +
                        // Detail recommendations.
                        String.format("%s organizational units: %d", fixOrgUnits < 0 ? REDUNDANT : REQUIRED, fixOrgUnits) +
                        NEXT_LINE +
                        String.format("%s inputs: %d", fixInputs < 0 ? REDUNDANT : REQUIRED, fixInputs) +
                        NEXT_LINE +
                        String.format("%s outputs: %d", fixOutputs < 0 ? REDUNDANT : REQUIRED, fixOutputs) +
                        NEXT_LINE +
                        String.format("%s application systems: %d", fixAppSystems < 0 ? REDUNDANT : REQUIRED, fixAppSystems) +
                        "</div>";
            }

            if (functionsMetrics.isEmpty())
                functionsMetrics = "N/A";

            return functionsMetrics;
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    private String getEvaluationLayout(String processName) {
        try {
            String state = "success";

            double metric = analysisService.getAggregatedIndicatorByProcessName(processName);
            double balance = analysisService.getWeightedBalanceCoefficientByProcessName(processName);

            int warnings = analysisService.getFunctionWarningsByProcessName(processName).size();
            int errors = analysisService.getFunctionErrorsByProcessName(processName).size();

            if (metric < 0)
                state = "danger";
            else if (metric > 0)
                state = "warning";

            return "<div class=\"alert alert-" + state + "\" role=\"alert\">" +
                    String.format("%.2f", metric) +
                    NEXT_LINE +
                    String.format("Warnings: %d", warnings) +
                    NEXT_LINE +
                    String.format("Errors: %d", errors) +
                    LINE +
                    "<small>" +
                    String.format("Balance: %.2f", balance) +
                    NEXT_LINE +
                    String.format("Organizational units density: %.2f",
                            analysisService.getOrganizationalUnitsDensityByProcessName(processName)) +
                    NEXT_LINE +
                    String.format("Input/output objects density: %.2f",
                            analysisService.getInputAndOutputObjectsDensityByProcessName(processName)) +
                    NEXT_LINE +
                    String.format("Application systems density: %.2f",
                            analysisService.getApplicationSystemsDensityByProcessName(processName)) +
                    "</small></div>";
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    private String getRecommendationsLayout(String processName) {
        try {
            String recommendations = "Process doesn't have any shortcomings";

            double metric = analysisService.getAggregatedIndicatorByProcessName(processName);

            if (metric < 0)
                recommendations = "There are shortcomings caused by modeling mistakes or \"bottlenecks\" that exist in organization activities:\n" +
                        "<ul>\n" +
                        "<li>Unassigned organizational units that perform process functions</li>\n" +
                        "<li>\"Useless\" functions that don't require and/or produce any material or information objects</li>\n" +
                        "<li>Not automated manual operations</li>\n" +
                        "</ul>";
            else if (metric > 0)
                recommendations = "There might be \"bottlenecks\" in organization activities:\n" +
                        "<ul>\n" +
                        "<li>Organizational gaps when functions are carried out by several organizational units</li>\n" +
                        "<li>Information gaps when functions are supported by several application systems</li>\n" +
                        "</ul>";

            return "<div class=\"card\">" +
                    "   <div class=\"card-header\">Recommendations</div>" +
                    "   <div class=\"card-body\">" + recommendations + "</div>" +
                    "</div>";
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    private String getHomeLayout() {
        String home = "";

        for (Process process : controlFlowService.getAllProcesses()) {
            String processName = process.getResource().getLocalName();

            home += "<div class=\"card bg-light mb-3\">\n" +
                    "   <div class=\"card-body\">\n" +
                    "       <h5 class=\"card-title dropright\">" +
                    "           <button class=\"btn btn-info btn-sm dropdown-toggle\" type=\"button\" " +
                    "               onclick=\"processInfo('" + processName + "');\"></button>" +
                    "           <a href=\"" + processName + HTML_FILE + "\">" + processName + "</a>" +
                    "       </h5>\n" +
                    "       <table class=\"table\" id=\"" + processName + "\" style=\"display:none;\">\n" +
                    "           <tr>\n" +
                    "               <td>Function View</td>\n" +
                    "               <td>Organization View</td>\n" +
                    "               <td>Application View</td>\n" +
                    "               <td>Inputs</td>\n" +
                    "               <td>Outputs</td>\n" +
                    "           </tr>\n";

            for (Function function : controlFlowService.getDetailedFunctionsByProcessName(processName)) {
                String orgUnits = "";

                for (OrganizationalUnit organizationalUnit : function.getOrganizationalUnits())
                    orgUnits += "<span class=\"badge badge-warning\">" + organizationalUnit.getResource().getLocalName() + "</span> ";

                String appSystems = "";

                for (ApplicationSystem applicationSystem : function.getApplicationSystems())
                    appSystems += "<span class=\"badge badge-primary\">" + applicationSystem.getResource().getLocalName() + "</span> ";

                String inputs = "";

                for (BusinessObject businessObject : function.getInputs())
                    inputs += "<span class=\"badge badge-secondary\">" + businessObject.getResource().getLocalName() + "</span> ";

                String outputs = "";

                for (BusinessObject businessObject : function.getOutputs())
                    outputs += "<span class=\"badge badge-secondary\">" + businessObject.getResource().getLocalName() + "</span> ";

                home += "<tr>\n" +
                        "   <td>\n" +
                        "       <span class=\"badge badge-success\">" + function.getResource().getLocalName() + "</span>\n" +
                        "   </td>\n" +
                        "   <td>\n" + orgUnits + "</td>\n" +
                        "   <td>\n" + appSystems + "</td>\n" +
                        "   <td>\n" + inputs + "</td>\n" +
                        "   <td>\n" + outputs + "</td>\n" +
                        "</tr>\n";
            }

            home += "       </table>\n" +
                    "   </div>\n" +
                    "</div>";
        }

        return home;
    }

    private String getSimilarModelsLayout(Process process) {
        try {
            String layout = "";

            for (Map.Entry<Process, Double> entry : similarityService.getSimilarProcessesByProcessPattern(process)) {
                String processName = entry.getKey().getResource().getLocalName();

                layout += "<a href=\"" + processName + HTML_FILE + "\">" + processName + "</a>" +
                        " <span class=\"badge badge-light\">Closeness: " + String.format("%.2f", entry.getValue()) + "</span><br/>";
            }

            if (layout.isEmpty())
                layout = "No similar models";

            return layout;
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);

            return "N/A";
        }
    }

    public void setValidationService(ValidationService validationService) {
        validationService.setControlFlowService(controlFlowService);

        this.validationService = validationService;
    }

    public void setAnalysisService(AnalysisService analysisService) {
        analysisService.setControlFlowService(controlFlowService);
        analysisService.setValidationService(validationService);

        this.analysisService = analysisService;
    }

    public void setSimilarityService(SimilarityService similarityService) {
        similarityService.setControlFlowService(controlFlowService);

        this.similarityService = similarityService;
    }
}
