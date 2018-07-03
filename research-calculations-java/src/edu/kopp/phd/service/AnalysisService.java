package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.Event;
import edu.kopp.phd.model.flow.Function;
import edu.kopp.phd.model.flow.Gateway;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import org.apache.log4j.Logger;

import java.util.*;

public class AnalysisService {
    public static final double FUNCTIONAL_WEIGHT = 1.00;
    public static final double SEQUENTIAL_WEIGHT = 0.83;
    public static final double COMMUNICATION_WEIGHT = 0.67;

    private static final Logger LOGGER = Logger.getLogger(AnalysisService.class);

    private RDFRepository repository = RDFRepository.getInstance();

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
        Process processByName = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        double allNodes = repository.retrieveProcess(processByName).size();
        double visitedNodes = allNodes - validationService.validateNodesCoherenceByProcessName(processName).size();

        double coeff = visitedNodes / allNodes - 1.0;

        coeff += ((teta(validationService.getStartNodesByProcessName(processName).size() - 1.0) - 1.0) +
                (teta(validationService.getEndNodesByProcessName(processName).size() - 1.0) - 1.0)) / 2.0;

        List<Event> events = controlFlowService.getDetailedEventsByProcessName(processName);

        for (Event event : events)
            coeff += (teta(1.0 - event.getPreceding().size() * event.getSubsequent().size()) - 1.0)
                    / (double) events.size();

        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        for (Function function : functions)
            coeff -= Math.abs(sgn(function.getPreceding().size() * function.getSubsequent().size() - 1.0))
                    / (double) functions.size();

        List<Process> processes = controlFlowService.getDetailedProcessesByProcessName(processName);

        for (Process process : processes)
            coeff -= (Math.abs(sgn(Math.min(process.getPreceding().size(), process.getSubsequent().size()))) +
                    Math.abs(sgn(Math.max(process.getPreceding().size(), process.getSubsequent().size()) - 1.0)))
                    / (2.0 * (double) processes.size());

        List<Gateway> gateways = controlFlowService.getDetailedGatewaysByProcessName(processName);

        for (Gateway gateway : gateways)
            coeff += ((teta(Math.max(gateway.getPreceding().size(), gateway.getSubsequent().size()) - 2.0) - 1.0) -
                    Math.abs(sgn(Math.min(gateway.getPreceding().size(), gateway.getSubsequent().size()) - 1.0)))
                    / (2.0 * (double) gateways.size());

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
