package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.Function;

import java.util.*;

public class RelaxedValidation {
    public static final double[] BPMN_ATTRIBUTES        = {1, 0, 0, 0, 0};
    public static final double[] DFD_ATTRIBUTES         = {0, 1, 0, 0, 0};
    public static final double[] ARISeEPC_ATTRIBUTES    = {1, 0, 1, 1, 1};
    public static final double[] IDEF0_ATTRIBUTES       = {0, 0, 1, 1, 1};

    public static final Map<String, double[]> ATTRIBUTES = new HashMap<>();

    static {
        ATTRIBUTES.put("BPMN", BPMN_ATTRIBUTES);
        ATTRIBUTES.put("DFD", DFD_ATTRIBUTES);
        ATTRIBUTES.put("ARISeEPC", ARISeEPC_ATTRIBUTES);
        ATTRIBUTES.put("IDEF0", IDEF0_ATTRIBUTES);
    }

    public static double validate(Model model, String type, boolean log) {
        double[] attributes = ATTRIBUTES.get(type);

        boolean isRegulationsConsidered = type.equals("IDEF0");

        model.enableEnvironment();

        double MSE = getMissingStartEvent(model).size();
        double MEE = getMissingEndEvent(model).size();
        double MSG = getMissingSplitGateway(model).size();
        double MJG = getMissingJoinGateway(model).size();

        double MDF = getMissingDataFlow(model).size();

        double UR = getUndefinedResponsibility(model).size();
        double RA = getResponsibilityAmbiguity(model).size();

        double UIO = getUndefinedInputsOutputs(model, isRegulationsConsidered).size();

        double UT = getUnautomatedTask(model).size();
        double AA = getAutomationAmbiguity(model).size();

        if (log) {
            System.out.printf("%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t",
                    attributes[0] * MSE, attributes[0] * MEE, attributes[0] * MSG, attributes[0] * MJG,
                    attributes[1] * MDF,
                    attributes[2] * UR, attributes[2] * RA,
                    attributes[3] * UIO,
                    attributes[4] * UT, attributes[4] * AA);
        }

        double defectDensity
                = attributes[0] * (MSE + MEE + MSG + MJG)
                + attributes[1] * MDF
                + attributes[2] * (UR + RA)
                + attributes[3] * UIO
                + attributes[4] * (UT + AA);

        return defectDensity;
    }

    private static List<Function> getMissingStartEvent(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getPreceding() == 0) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getMissingEndEvent(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getSubsequent() == 0) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getMissingSplitGateway(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getSubsequent() > 1) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getMissingJoinGateway(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getPreceding() > 1) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getMissingDataFlow(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getPreceding() == 0 || function.getSubsequent() == 0) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getUndefinedResponsibility(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getOrganizationalUnits() == 0) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getUndefinedInputsOutputs(Model model, boolean isRegulationsConsidered) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (isRegulationsConsidered) {
                if (function.getInputs() == 0 || function.getOutputs() == 0 || function.getRegulations() == 0) {
                    functions.add(function);
                }
            } else {
                if (function.getInputs() == 0 || function.getOutputs() == 0) {
                    functions.add(function);
                }
            }
        }

        return functions;
    }

    private static List<Function> getUnautomatedTask(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getApplicationSystems() == 0) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getResponsibilityAmbiguity(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getOrganizationalUnits() > 1) {
                functions.add(function);
            }
        }

        return functions;
    }

    private static List<Function> getAutomationAmbiguity(Model model) {
        List<Function> functions = new ArrayList<>();

        for (Function function : model.getFunctions()) {
            if (function.getApplicationSystems() > 1) {
                functions.add(function);
            }
        }

        return functions;
    }
}
