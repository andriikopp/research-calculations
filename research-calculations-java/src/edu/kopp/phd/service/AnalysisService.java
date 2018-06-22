package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.Function;

import java.util.*;

public class AnalysisService {
    public static final double FUNCTIONAL_WEIGHT = 1.00;
    public static final double SEQUENTIAL_WEIGHT = 0.83;
    public static final double COMMUNICATION_WEIGHT = 0.67;

    private ControlFlowService controlFlowService;

    public AnalysisService(ControlFlowService controlFlowService) {
        this.controlFlowService = controlFlowService;
    }

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

            indicators.put("Org.", FUNCTIONAL_WEIGHT * sgn(function.getOrganizationalUnits().size() - 1));
            indicators.put("In.", SEQUENTIAL_WEIGHT * (sgn(function.getInputs().size()) - 1));
            indicators.put("Out.", SEQUENTIAL_WEIGHT * (sgn(function.getOutputs().size()) - 1));
            indicators.put("App.", COMMUNICATION_WEIGHT * sgn(function.getApplicationSystems().size() - 1));

            double aggregatedFunctionIndicator = 0;

            if (Collections.min(indicators.values()) < 0)
                for (double value : indicators.values())
                    aggregatedFunctionIndicator += (1.0 - teta(value)) * value;
            else
                for (double value : indicators.values())
                    aggregatedFunctionIndicator += value;

            indicators.put("Agg.", aggregatedFunctionIndicator);

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

        return aggregatedIndicator;
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
}
