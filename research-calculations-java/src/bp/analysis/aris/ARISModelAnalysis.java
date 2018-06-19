package bp.analysis.aris;

import bp.analysis.aris.model.*;

import java.util.*;

/**
 * Analysis of ARIS extended Event-driven Process Chain (eEPC) models.
 *
 * @author Andrii Kopp
 */
public class ARISModelAnalysis {
    public static final double RESOURCE_FLOW_WEIGHT = 1.00;
    public static final double OBJECT_FLOW_WEIGHT = 0.83;
    public static final double INFORMATION_FLOW_WEIGHT = 0.67;

    public static final int FUNCTION_INDICATORS_NUMBER = 4;

    public static boolean validateNodesCoherence(ARISeEPCBusinessModel model) {
        Set<ARISeEPCFlowObject> nodes = model.getFlowObjects();

        ARISeEPCFlowObject startNode = (ARISeEPCFlowObject) nodes.toArray()[0];

        Queue<ARISeEPCFlowObject> queue = new LinkedList<>();
        Set<ARISeEPCFlowObject> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            ARISeEPCFlowObject node = queue.poll();

            for (ARISeEPCFlowObject flowObject : model.getRelatedNodes(node)) {
                if (!visited.contains(flowObject)) {
                    queue.add(flowObject);
                    visited.add(flowObject);
                }
            }
        }

        return nodes.size() == visited.size();
    }

    public static boolean validateStartEndNodes(ARISeEPCBusinessModel model) {
        int countStartEvents = 0;
        int countEndEvents = 0;
        int countStartProcesses = 0;
        int countEndProcesses = 0;

        for (ARISeEPCEvent event : model.getEvents())
            if (model.countPreceding(event) == 0)
                countStartEvents++;
            else if (model.countSubsequent(event) == 0)
                countEndEvents++;

        for (ARISeEPCProcess process : model.getProcesses())
            if (model.countPreceding(process) == 0)
                countStartProcesses++;
            else if (model.countSubsequent(process) == 0)
                countEndProcesses++;

        return (countStartEvents + countStartProcesses) >= 1 && (countEndEvents + countEndProcesses) >= 1;
    }

    public static Set<ARISeEPCEvent> validateEvents(ARISeEPCBusinessModel model) {
        Set<ARISeEPCEvent> invalidEvents = new HashSet<>();

        for (ARISeEPCEvent event : model.getEvents())
            if (!(model.countPreceding(event) <= 1 && model.countSubsequent(event) <= 1))
                invalidEvents.add(event);

        return invalidEvents;
    }

    public static Set<ARISeEPCFunction> validateFunctions(ARISeEPCBusinessModel model) {
        Set<ARISeEPCFunction> invalidFunctions = new HashSet<>();

        for (ARISeEPCFunction function : model.getFunctions())
            if (!(model.countPreceding(function) == 1 && model.countSubsequent(function) == 1))
                invalidFunctions.add(function);

        return invalidFunctions;
    }

    public static Set<ARISeEPCProcess> validateProcesses(ARISeEPCBusinessModel model) {
        Set<ARISeEPCProcess> invalidProcesses = new HashSet<>();

        for (ARISeEPCProcess process : model.getProcesses())
            if (!((model.countPreceding(process) == 1 && model.countSubsequent(process) == 0) ||
                    (model.countPreceding(process) == 0 && model.countSubsequent(process) == 1)))
                invalidProcesses.add(process);

        return invalidProcesses;
    }

    public static Set<ARISeEPCGateway> validateGateways(ARISeEPCBusinessModel model) {
        Set<ARISeEPCGateway> invalidGateways = new HashSet<>();

        for (ARISeEPCGateway gateway : model.getGateways())
            if (!((model.countPreceding(gateway) == 1 && model.countSubsequent(gateway) >= 1) ||
                    (model.countPreceding(gateway) >= 1 && model.countSubsequent(gateway) == 1)))
                invalidGateways.add(gateway);

        return invalidGateways;
    }

    public static Set<ARISeEPCFunction> validateFunctionsOrganizationalUnits(ARISeEPCBusinessModel model) {
        Set<ARISeEPCFunction> invalidFunctions = new HashSet<>();

        for (ARISeEPCFunction function : model.getFunctions())
            if (!(model.countOrganizationalUnits(function) >= 1))
                invalidFunctions.add(function);

        return invalidFunctions;
    }

    public static Set<ARISeEPCFunction> validateFunctionsInputsOutputs(ARISeEPCBusinessModel model) {
        Set<ARISeEPCFunction> invalidFunctions = new HashSet<>();

        for (ARISeEPCFunction function : model.getFunctions())
            if (!(model.countInputs(function) >= 1 && model.countOutputs(function) >= 1))
                invalidFunctions.add(function);

        return invalidFunctions;
    }

    public static Set<ARISeEPCFunction> validateFunctionsApplicationSystems(ARISeEPCBusinessModel model) {
        Set<ARISeEPCFunction> invalidFunctions = new HashSet<>();

        for (ARISeEPCFunction function : model.getFunctions())
            if (!(model.countApplicationSystems(function) >= 1))
                invalidFunctions.add(function);

        return invalidFunctions;
    }

    /* Chain of analysis functions. */
    public static Map<ARISeEPCFunction, Double[]> calculateFunctionsIndicators(ARISeEPCBusinessModel model) {
        Map<ARISeEPCFunction, Double[]> functionsIndicators = new HashMap<>();

        for (ARISeEPCFunction function : model.getFunctions()) {
            Double[] indicators = new Double[FUNCTION_INDICATORS_NUMBER];

            indicators[0] = RESOURCE_FLOW_WEIGHT * sgn(model.countOrganizationalUnits(function) - 1);
            indicators[1] = OBJECT_FLOW_WEIGHT * (sgn(model.countInputs(function)) - 1);
            indicators[2] = OBJECT_FLOW_WEIGHT * (sgn(model.countOutputs(function)) - 1);
            indicators[3] = INFORMATION_FLOW_WEIGHT * sgn(model.countApplicationSystems(function) - 1);

            functionsIndicators.put(function, indicators);
        }

        return functionsIndicators;
    }

    public static Map<ARISeEPCFunction, Double> calculateFunctionsBalancing(Map<ARISeEPCFunction, Double[]> functionsIndicators) {
        Map<ARISeEPCFunction, Double> functionsBalancing = new HashMap<>();

        for (Map.Entry<ARISeEPCFunction, Double[]> entry : functionsIndicators.entrySet()) {
            double balancing = 0;

            if (Collections.min(Arrays.asList(entry.getValue())) < 0)
                for (Double indicator : entry.getValue())
                    balancing += (1 - teta(indicator)) * indicator;
            else
                for (Double indicator : entry.getValue())
                    balancing += indicator;

            functionsBalancing.put(entry.getKey(), balancing);
        }

        return functionsBalancing;
    }

    public static double calculateModelBalancing(Map<ARISeEPCFunction, Double> functionsBalancing) {
        double balancing = 0;

        if (Collections.min(functionsBalancing.values()) < 0)
            for (Double indicator : functionsBalancing.values())
                balancing += (1 - teta(indicator)) * indicator;
        else
            for (Double indicator : functionsBalancing.values())
                balancing += indicator;

        return balancing;
    }

    /* Private functions. */
    private static double sgn(double value) {
        if (value > 0) return 1;
        if (value < 0) return  -1;
        return 0;
    }

    private static double teta(double value) {
        if (value >= 0) return 1;
        return 0;
    }

    public static void main(String[] args) {
        ARISeEPCBusinessModel model = ARISeEPCBusinessModel.createInstance();

        ARISeEPCEvent                       startEvent  = ARISeEPCEvent.event("Start event");
        ARISeEPCFunction                    functionA   = ARISeEPCFunction.function("Function A");
        ARISeEPCGateway.ARISeEPCXorGateway  xorGateway  = ARISeEPCGateway.ARISeEPCXorGateway.xorGateway();
        ARISeEPCEvent                       eventA      = ARISeEPCEvent.event("Event A");
        ARISeEPCEvent                       eventB      = ARISeEPCEvent.event("Event B");
        ARISeEPCFunction                    functionB   = ARISeEPCFunction.function("Function B");
        ARISeEPCFunction                    functionC   = ARISeEPCFunction.function("Function C");
        ARISeEPCEvent                       endEventA   = ARISeEPCEvent.event("End event A");
        ARISeEPCEvent                       endEventB   = ARISeEPCEvent.event("End event B");

        model.createControlFlow(startEvent, functionA);
        model.createControlFlow(functionA, xorGateway);
        model.createControlFlow(xorGateway, eventA, eventB);
        model.createControlFlow(eventA, functionB);
        model.createControlFlow(eventB, functionC);
        model.createControlFlow(functionB, endEventA);
        model.createControlFlow(functionC, endEventB);

        System.out.println(validateNodesCoherence(model));
        System.out.println(validateStartEndNodes(model));
        System.out.println(validateEvents(model));
        System.out.println(validateFunctions(model));
        System.out.println(validateProcesses(model));
        System.out.println(validateGateways(model));

        System.out.println(validateFunctionsOrganizationalUnits(model));
        System.out.println(validateFunctionsInputsOutputs(model));
        System.out.println(validateFunctionsApplicationSystems(model));

        Map<ARISeEPCFunction, Double[]> functionsIndicators = calculateFunctionsIndicators(model);
        Map<ARISeEPCFunction, Double> functionsBalancing = calculateFunctionsBalancing(functionsIndicators);

        System.out.println(calculateModelBalancing(functionsBalancing));

        model.buildKnowledgeModel();
        model.getKnowledgeModel().write(System.out, "JSON-LD");
    }
}
