package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.Event;
import edu.kopp.phd.model.flow.Function;
import edu.kopp.phd.model.flow.Gateway;
import edu.kopp.phd.model.flow.Process;
import org.apache.log4j.Logger;

import java.util.*;

public class AnalysisService {
    public static final double UNITS_WEIGHT = 0.83;
    public static final double OBJECTS_WEIGHT = 1.0;
    public static final double APPLICATIONS_WEIGHT = 0.67;

    private static final Logger LOGGER = Logger.getLogger(AnalysisService.class);

    private ControlFlowService controlFlowService;
    private ValidationService validationService;

    public List<String> getFunctionErrorsByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        List<String> functionErrors = new ArrayList<>();

        for (Function function : functions) {
            if (!(function.getOrganizationalUnits().size() >= 1))
                functionErrors.add(String.format("Function '%s' doesn't have any organizational units", function));

            if (!(function.getInputs().size() >= 1 && function.getOutputs().size() >= 1))
                functionErrors.add(String.format("Function '%s' doesn't have any inputs/outputs", function));

            if (!(function.getApplicationSystems().size() >= 1))
                functionErrors.add(String.format("Function '%s' doesn't have any application systems", function));
        }

        return functionErrors;
    }

    public Map<Function, Map<String, Double>> getFunctionIndicatorsByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        Map<Function, Map<String, Double>> functionIndicators = new HashMap<>();

        for (Function function : functions) {
            Map<String, Double> indicators = new LinkedHashMap<>();

            indicators.put("Org.", UNITS_WEIGHT * sgn(function.getOrganizationalUnits().size() - 1));
            indicators.put("In.", OBJECTS_WEIGHT * (sgn(function.getInputs().size()) - 1));
            indicators.put("Out.", OBJECTS_WEIGHT * (sgn(function.getOutputs().size()) - 1));
            indicators.put("App.", APPLICATIONS_WEIGHT * sgn(function.getApplicationSystems().size() - 1));

            double aggregatedFunctionIndicator = 0;

            if (Collections.min(indicators.values()) < 0)
                for (double value : indicators.values())
                    aggregatedFunctionIndicator += (1.0 - teta(value)) * value;
            else
                for (double value : indicators.values())
                    aggregatedFunctionIndicator += value;

            indicators.put("Agg.", aggregatedFunctionIndicator);

            LOGGER.info(String.format("FunctionEvaluation;%s;%s;%.4f;%.4f;%.4f;%.4f;%.4f",
                    function.getResource().getLocalName(),
                    processName,
                    indicators.get("Org."),
                    indicators.get("In."),
                    indicators.get("Out."),
                    indicators.get("App."),
                    indicators.get("Agg.")));

            functionIndicators.put(function, indicators);
        }

        return functionIndicators;
    }

    public double getAggregatedIndicatorByProcessName(String processName) {
        Map<Function, Map<String, Double>> functionIndicators = getFunctionIndicatorsByProcessName(processName);

        List<Double> aggregatedFunctionIndicators = new ArrayList<>();

        for (Map<String, Double> indicators : functionIndicators.values())
            aggregatedFunctionIndicators.add(indicators.get("Agg."));

        double aggregatedIndicator = 0;

        if (Collections.min(aggregatedFunctionIndicators) < 0)
            for (double value : aggregatedFunctionIndicators)
                aggregatedIndicator += (1.0 - teta(value)) * value;
        else
            for (double value : aggregatedFunctionIndicators)
                aggregatedIndicator += value;

        LOGGER.info(String.format("ProcessEvaluation;%s;%.4f", processName, aggregatedIndicator));

        return aggregatedIndicator;
    }

    public double getCSCCoefficientByProcessName(String processName) {
        double coeff = (1.0 - (validationService.validateNodesCoherenceByProcessName(processName).size() == 0 ? 1 : 0)) +
                (1.0 - (validationService.getStartNodesByProcessName(processName).size() >= 1 &&
                        validationService.getEndNodesByProcessName(processName).size() >= 1 ? 1 : 0));

        List<Event> events = controlFlowService.getDetailedEventsByProcessName(processName);

        for (Event event : events)
            coeff += 1.0 - (event.getPreceding().size() <= 1 && event.getSubsequent().size() <= 1 ? 1 : 0);

        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        for (Function function : functions)
            coeff += 1.0 - (function.getPreceding().size() == 1 && function.getSubsequent().size() == 1 ? 1 : 0);

        List<Process> processes = controlFlowService.getDetailedProcessesByProcessName(processName);

        for (Process process : processes)
            coeff += 1.0 - ((process.getPreceding().size() == 1 && process.getSubsequent().size() == 0) ||
                    (process.getPreceding().size() == 0 && process.getSubsequent().size() == 1) ? 1 : 0);

        List<Gateway> gateways = controlFlowService.getDetailedGatewaysByProcessName(processName);

        for (Gateway gateway : gateways)
            coeff += 1.0 - ((gateway.getPreceding().size() == 1 && gateway.getSubsequent().size() > 1) ||
                    (gateway.getPreceding().size() > 1 && gateway.getSubsequent().size() == 1) ? 1 : 0);

        coeff += validationService.validateEventsAndGatewaysConnectionsByProcessName(processName).size();

        LOGGER.info(String.format("ModelEvaluation;%s;%.4f", processName, coeff));

        return coeff;
    }

    private double sgn(double value) {
        if (value < 0)
            return  -1;

        if (value > 0)
            return 1;

        return 0;
    }

    private double teta(double value) {
        if (value >= 0)
            return 1;

        return 0;
    }

    public void setControlFlowService(ControlFlowService controlFlowService) {
        this.controlFlowService = controlFlowService;
    }

    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }
}
