package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.Event;
import edu.kopp.phd.model.flow.Function;
import edu.kopp.phd.model.flow.Gateway;
import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.repository.RDFRepository;
import edu.kopp.phd.service.similarity.ConcreteSimilarityMethodFactory;
import edu.kopp.phd.service.similarity.api.SimilarityMethod;
import org.apache.log4j.Logger;

import java.util.*;

public class AnalysisService {
    public static final double UNITS_WEIGHT = 0.83;
    public static final double OBJECTS_WEIGHT = 1.0;
    public static final double APPLICATIONS_WEIGHT = 0.67;

    public static final double PROCESS_FLOW_OBJECTS_WEIGHT = 0.5;
    public static final double AND_GATEWAYS_WEIGHT = 0.33;
    public static final double OR_XOR_GATEWAYS_WEIGHT = 0.17;

    private static final Logger LOGGER = Logger.getLogger(AnalysisService.class);

    private RDFRepository repository = RDFRepository.getInstance();

    private ControlFlowService controlFlowService;
    private ValidationService validationService;

    private double weightedProcessFlowCoefficient;

    public List<String> getFunctionErrorsByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        List<String> functionErrors = new ArrayList<>();

        for (Function function : functions) {
            if (!(function.getOrganizationalUnits().size() >= 1))
                functionErrors.add(String.format("Function '%s' doesn't have any organizational units", function));

            if (!(function.getInputs().size() >= 1 && function.getOutputs().size() >= 1))
                functionErrors.add(String.format("Function '%s' doesn't have any inputs/outputs", function));
        }

        return functionErrors;
    }

    public List<String> getFunctionWarningsByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        List<String> functionWarnings = new ArrayList<>();

        for (Function function : functions) {
            if (!(function.getApplicationSystems().size() >= 1))
                functionWarnings.add(String.format("Function '%s' doesn't have any application systems", function));

            if (function.getOrganizationalUnits().size() > 1)
                functionWarnings.add(String.format("Function '%s' is carried out by several organizational units", function));

            if (function.getApplicationSystems().size() > 1)
                functionWarnings.add(String.format("Function '%s' is supported by several application systems", function));
        }

        return functionWarnings;
    }

    public Map<Function, Map<String, Double>> getFunctionIndicatorsByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        Map<Function, Map<String, Double>> functionIndicators = new HashMap<>();

        for (Function function : functions) {
            Map<String, Double> indicators = new LinkedHashMap<>();

            indicators.put("Org.", UNITS_WEIGHT * sgn(function.getOrganizationalUnits().size() - 1));
            indicators.put("In.", OBJECTS_WEIGHT * (sgn(function.getInputs().size()) - 1));
            indicators.put("Out.", OBJECTS_WEIGHT * (sgn(function.getOutputs().size()) - 1));
            indicators.put("App.", APPLICATIONS_WEIGHT * Math.abs(sgn(function.getApplicationSystems().size() - 1)));

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

        if (functionIndicators.isEmpty())
            return 0;

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

    public double getWeightedBalanceCoefficientByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        double sum = 0;
        double max = 0;

        for (Function function : functions) {
            double functionValue = UNITS_WEIGHT * function.getOrganizationalUnits().size() +
                    OBJECTS_WEIGHT * function.getInputs().size() +
                    OBJECTS_WEIGHT * function.getOutputs().size() +
                    APPLICATIONS_WEIGHT * function.getApplicationSystems().size();

            if (functionValue > max)
                max = functionValue;
        }

        return Math.abs((1.0 / (double) functions.size()) * sum - max);
    }

    public int[] getDetailedEvaluationOfFunctionsWarningsAndErrorsByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        int unassignedOrgUnits = (int) functions.stream()
                .filter(function -> function.getOrganizationalUnits().size() < 1).count();
        int redundantOrgUnits = (int) functions.stream()
                .filter(function -> function.getOrganizationalUnits().size() > 1).count();

        int unassignedInputs = (int) functions.stream()
                .filter(function -> function.getInputs().size() < 1).count();
        int unassignedOutputs = (int) functions.stream()
                .filter(function -> function.getOutputs().size() < 1).count();

        int redundantInputs = (int) functions.stream()
                .filter(function -> function.getInputs().size() > 1).count();
        int redundantOutputs = (int) functions.stream()
                .filter(function -> function.getOutputs().size() > 1).count();

        int unassignedAppSystems = (int) functions.stream()
                .filter(function -> function.getApplicationSystems().size() < 1).count();
        int redundantAppSystems = (int) functions.stream()
                .filter(function -> function.getApplicationSystems().size() > 1).count();

        return new int[]{unassignedOrgUnits, redundantOrgUnits, unassignedInputs, unassignedOutputs,
                redundantInputs, redundantOutputs, unassignedAppSystems, redundantAppSystems};
    }

    public double getOrganizationalUnitsDensityByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        double density = 0;

        for (Function function : functions)
            density += function.getOrganizationalUnits().size();

        density /= (double) (functions.size() * (functions.size() - 1));

        return density;
    }

    public double getInputAndOutputObjectsDensityByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        double density = 0;

        for (Function function : functions)
            density += function.getInputs().size() + function.getOutputs().size();

        density /= (double) (functions.size() * (functions.size() - 1));

        return density;
    }

    public double getApplicationSystemsDensityByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        double density = 0;

        for (Function function : functions)
            density += function.getApplicationSystems().size();

        density /= (double) (functions.size() * (functions.size() - 1));

        return density;
    }

    public double getCSCCoefficientByProcessName(String processName) {
        List<Function> functions = controlFlowService.getDetailedFunctionsByProcessName(processName);

        // Nodes coherence validation.
        double coeff = sgn(validationService.validateNodesCoherenceByProcessName(processName).size()) +

                // EPC diagram should has at least one function.
                (1.0 - sgn(functions.size())) +

                // Start and end nodes validation.
                (1.0 - (validationService.getStartNodesByProcessName(processName).size() >= 1 &&
                        validationService.getEndNodesByProcessName(processName).size() >= 1 ? 1 : 0));

        // Weighted validation coefficient.
        double wCoeff = 0;

        List<Event> events = controlFlowService.getDetailedEventsByProcessName(processName);

        for (Event event : events)
            coeff += 1.0 - (event.getPreceding().size() <= 1 && event.getSubsequent().size() <= 1 ? 1 : 0);

        for (Function function : functions)
            coeff += 1.0 - (function.getPreceding().size() == 1 && function.getSubsequent().size() == 1 ? 1 : 0);

        List<Process> processes = controlFlowService.getDetailedProcessesByProcessName(processName);

        for (Process process : processes)
            coeff += 1.0 - ((process.getPreceding().size() == 1 && process.getSubsequent().size() == 0) ||
                    (process.getPreceding().size() == 0 && process.getSubsequent().size() == 1) ? 1 : 0);

        // Before connectors.
        wCoeff = PROCESS_FLOW_OBJECTS_WEIGHT * coeff;

        SimilarityMethod similarityMethod = ConcreteSimilarityMethodFactory.createInstance().getSimilarityMethod();

        Process process = new Process(repository.getModel().createResource(RDFRepository.NS_REPOSITORY +
                processName.replaceAll("\\s+", "_")));

        List<Gateway> gateways = controlFlowService.getDetailedGatewaysByProcessName(processName);

        for (Gateway gateway : gateways) {
            coeff += 1.0 - ((gateway.getPreceding().size() == 1 && gateway.getSubsequent().size() > 1) ||
                    (gateway.getPreceding().size() > 1 && gateway.getSubsequent().size() == 1) ? 1 : 0);

            // Connectors segregation.
            if (similarityMethod.getNodeTypeByLabel(gateway.getResource().getLocalName(), process).
                    equals(repository.getAndGateway()))
                wCoeff += AND_GATEWAYS_WEIGHT * (1.0 - ((gateway.getPreceding().size() == 1 && gateway.getSubsequent().size() > 1) ||
                        (gateway.getPreceding().size() > 1 && gateway.getSubsequent().size() == 1) ? 1 : 0));
            else
                wCoeff += OR_XOR_GATEWAYS_WEIGHT * (1.0 - ((gateway.getPreceding().size() == 1 && gateway.getSubsequent().size() > 1) ||
                        (gateway.getPreceding().size() > 1 && gateway.getSubsequent().size() == 1) ? 1 : 0));
        }

        coeff += validationService.validateEventsAndGatewaysConnectionsByProcessName(processName).size();

        coeff += validationService.getEventsThatArePrecedingForAnotherEventsByProcessName(processName).size();

        wCoeff += PROCESS_FLOW_OBJECTS_WEIGHT * validationService.validateEventsAndGatewaysConnectionsByProcessName(
                processName).size();

        wCoeff += PROCESS_FLOW_OBJECTS_WEIGHT * validationService.getEventsThatArePrecedingForAnotherEventsByProcessName(
                processName).size();

        weightedProcessFlowCoefficient = wCoeff;

        LOGGER.info(String.format("ModelEvaluationCSC;%s;%.4f", processName, coeff));
        LOGGER.info(String.format("ModelEvaluationWCSC;%s;%.4f", processName, weightedProcessFlowCoefficient));

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

    public double getWeightedProcessFlowCoefficient() {
        return weightedProcessFlowCoefficient;
    }
}
