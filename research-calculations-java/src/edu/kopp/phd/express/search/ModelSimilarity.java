package edu.kopp.phd.express.search;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.search.utils.VectorSimilarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelSimilarity {
    public static final double[] BPMNvsBPMN = {0.5, 0.5, 0, 0, 0, 0};
    public static final double[] BPMNvsDFD = {1, 0, 0, 0, 0, 0};
    public static final double[] BPMNvsARISeEPC = {0.5, 0.5, 0, 0, 0, 0};
    public static final double[] BPMNvsIDEF0 = {1, 0, 0, 0, 0, 0};

    public static final double[] DFDvsDFD = {0.5, 0, 0, 0, 0.5, 0};
    public static final double[] DFDvsARISeEPC = {0.5, 0, 0, 0, 0.5, 0};
    public static final double[] DFDvsIDEF0 = {0.5, 0, 0, 0, 0.5, 0};

    public static final double[] ARISeEPCvsARISeEPC = {0.2, 0.2, 0.2, 0.2, 0.2, 0};
    public static final double[] ARISeEPCvsIDEF0 = {0.25, 0, 0.25, 0.25, 0.25, 0};

    public static final double[] IDEF0vsIDEF0 = {0.2, 0, 0.2, 0.2, 0.2, 0.2};

    public static final Map<String, double[]> COMPARE_MAPPING = new HashMap<>();

    static {
        COMPARE_MAPPING.put("BPMNvsBPMN", BPMNvsBPMN);
        COMPARE_MAPPING.put("BPMNvsDFD", BPMNvsDFD);
        COMPARE_MAPPING.put("BPMNvsARISeEPC", BPMNvsARISeEPC);
        COMPARE_MAPPING.put("BPMNvsIDEF0", BPMNvsIDEF0);

        COMPARE_MAPPING.put("DFDvsDFD", DFDvsDFD);
        COMPARE_MAPPING.put("DFDvsARISeEPC", DFDvsARISeEPC);
        COMPARE_MAPPING.put("DFDvsIDEF0", DFDvsIDEF0);

        COMPARE_MAPPING.put("ARISeEPCvsARISeEPC", ARISeEPCvsARISeEPC);
        COMPARE_MAPPING.put("ARISeEPCvsIDEF0", ARISeEPCvsIDEF0);

        COMPARE_MAPPING.put("IDEF0vsIDEF0", IDEF0vsIDEF0);
    }

    public static double[] getCompareWeights(String first, String second) {
        for (Map.Entry<String, double[]> entry : COMPARE_MAPPING.entrySet()) {
            if (entry.getKey().contains(first) && entry.getKey().contains(second)) {
                return entry.getValue();
            }
        }

        throw new RuntimeException("Invalid notations!");
    }

    public static double similarity(Model first, Model second, double[] weights) {
        return weights[0] * VectorSimilarity.similarity(first.getNodes().size(), second.getNodes().size()) +
                weights[1] * nodesSimilarity(first, second) +
                weights[2] * orgUnitsSimilarity(first, second) +
                weights[3] * appSystemsSimilarity(first, second) +
                weights[4] * 0.5 * (inputsSimilarity(first, second) + outputsSimilarity(first, second)) +
                weights[5] * regulationsSimilarity(first, second);
    }

    private static double nodesSimilarity(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getNodes().size(); i++) {
            String key = "<" + first.getNodes().get(i).getClass() + "," +
                    first.getNodes().get(i).getPreceding() + "," +
                    first.getNodes().get(i).getSubsequent() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getNodes().size(); i++) {
            String key = "<" + second.getNodes().get(i).getClass() + "," +
                    second.getNodes().get(i).getPreceding() + "," +
                    second.getNodes().get(i).getSubsequent() + ">";

            if (secondVec.containsKey(key)) {
                double count = secondVec.get(key) + 1.0;
                secondVec.put(key, count);
            } else {
                secondVec.put(key, 1.0);
            }
        }

        if (firstVec.isEmpty() && secondVec.isEmpty()) {
            return 1.0;
        }

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstVec.keySet());
        allKeys.addAll(secondVec.keySet());

        for (String key : allKeys) {
            if (firstVec.containsKey(key) && secondVec.containsKey(key)) {
                value += VectorSimilarity.similarity(firstVec.get(key), secondVec.get(key));
            }
        }

        return 2.0 * value / (firstVec.size() + secondVec.size());
    }

    private static double orgUnitsSimilarity(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getFunctions().size(); i++) {
            String key = "<" + first.getFunctions().get(i).getOrganizationalUnits() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getFunctions().size(); i++) {
            String key = "<" + second.getFunctions().get(i).getOrganizationalUnits() + ">";

            if (secondVec.containsKey(key)) {
                double count = secondVec.get(key) + 1.0;
                secondVec.put(key, count);
            } else {
                secondVec.put(key, 1.0);
            }
        }

        if (firstVec.isEmpty() && secondVec.isEmpty()) {
            return 1.0;
        }

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstVec.keySet());
        allKeys.addAll(secondVec.keySet());

        for (String key : allKeys) {
            if (firstVec.containsKey(key) && secondVec.containsKey(key)) {
                value += VectorSimilarity.similarity(firstVec.get(key), secondVec.get(key));
            }
        }

        return 2.0 * value / (firstVec.size() + secondVec.size());
    }

    private static double appSystemsSimilarity(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getFunctions().size(); i++) {
            String key = "<" + first.getFunctions().get(i).getApplicationSystems() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getFunctions().size(); i++) {
            String key = "<" + second.getFunctions().get(i).getApplicationSystems() + ">";

            if (secondVec.containsKey(key)) {
                double count = secondVec.get(key) + 1.0;
                secondVec.put(key, count);
            } else {
                secondVec.put(key, 1.0);
            }
        }

        if (firstVec.isEmpty() && secondVec.isEmpty()) {
            return 1.0;
        }

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstVec.keySet());
        allKeys.addAll(secondVec.keySet());

        for (String key : allKeys) {
            if (firstVec.containsKey(key) && secondVec.containsKey(key)) {
                value += VectorSimilarity.similarity(firstVec.get(key), secondVec.get(key));
            }
        }

        return 2.0 * value / (firstVec.size() + secondVec.size());
    }

    private static double inputsSimilarity(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getFunctions().size(); i++) {
            String key = "<" + first.getFunctions().get(i).getInputs() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getFunctions().size(); i++) {
            String key = "<" + second.getFunctions().get(i).getInputs() + ">";

            if (secondVec.containsKey(key)) {
                double count = secondVec.get(key) + 1.0;
                secondVec.put(key, count);
            } else {
                secondVec.put(key, 1.0);
            }
        }

        if (firstVec.isEmpty() && secondVec.isEmpty()) {
            return 1.0;
        }

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstVec.keySet());
        allKeys.addAll(secondVec.keySet());

        for (String key : allKeys) {
            if (firstVec.containsKey(key) && secondVec.containsKey(key)) {
                value += VectorSimilarity.similarity(firstVec.get(key), secondVec.get(key));
            }
        }

        return 2.0 * value / (firstVec.size() + secondVec.size());
    }

    private static double outputsSimilarity(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getFunctions().size(); i++) {
            String key = "<" + first.getFunctions().get(i).getOutputs() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getFunctions().size(); i++) {
            String key = "<" + second.getFunctions().get(i).getOutputs() + ">";

            if (secondVec.containsKey(key)) {
                double count = secondVec.get(key) + 1.0;
                secondVec.put(key, count);
            } else {
                secondVec.put(key, 1.0);
            }
        }

        if (firstVec.isEmpty() && secondVec.isEmpty()) {
            return 1.0;
        }

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstVec.keySet());
        allKeys.addAll(secondVec.keySet());

        for (String key : allKeys) {
            if (firstVec.containsKey(key) && secondVec.containsKey(key)) {
                value += VectorSimilarity.similarity(firstVec.get(key), secondVec.get(key));
            }
        }

        return 2.0 * value / (firstVec.size() + secondVec.size());
    }

    private static double regulationsSimilarity(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getFunctions().size(); i++) {
            String key = "<" + first.getFunctions().get(i).getRegulations() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getFunctions().size(); i++) {
            String key = "<" + second.getFunctions().get(i).getRegulations() + ">";

            if (secondVec.containsKey(key)) {
                double count = secondVec.get(key) + 1.0;
                secondVec.put(key, count);
            } else {
                secondVec.put(key, 1.0);
            }
        }

        if (firstVec.isEmpty() && secondVec.isEmpty()) {
            return 1.0;
        }

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstVec.keySet());
        allKeys.addAll(secondVec.keySet());

        for (String key : allKeys) {
            if (firstVec.containsKey(key) && secondVec.containsKey(key)) {
                value += VectorSimilarity.similarity(firstVec.get(key), secondVec.get(key));
            }
        }

        return 2.0 * value / (firstVec.size() + secondVec.size());
    }
}
