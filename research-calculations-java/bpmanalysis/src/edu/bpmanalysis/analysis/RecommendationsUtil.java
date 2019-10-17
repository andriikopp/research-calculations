package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;

import java.util.*;

public class RecommendationsUtil {

    public static List<String> generateRecommendations(ProcessModelAnalysisBean processModelAnalysisBean) {
        List<String> recommendations = new ArrayList<>();

        double[] nodesChanges = processModelAnalysisBean.getNodesChanges();

        Map<String, Integer> descriptionOfNodesChanges = new LinkedHashMap<>();

        descriptionOfNodesChanges.put("element(s)", (int) nodesChanges[0]);
        descriptionOfNodesChanges.put("function(s)", (int) nodesChanges[4]);
        descriptionOfNodesChanges.put("start event(s)", (int) nodesChanges[1]);
        descriptionOfNodesChanges.put("end event(s)", (int) nodesChanges[2]);
        descriptionOfNodesChanges.put("OR routing element(s)", (int) nodesChanges[3]);

        for (Map.Entry<String, Integer> value : descriptionOfNodesChanges.entrySet()) {
            if (value.getValue() != 0) {
                if (value.getValue() > 0) {
                    recommendations.add("Add " + value.getValue() + " " + value.getKey());
                } else {
                    recommendations.add("Remove " + Math.abs(value.getValue()) + " " + value.getKey());
                }
            }
        }

        for (Map.Entry<String, Double> entry : processModelAnalysisBean.getConnectorsChanges().entrySet()) {
            if (entry.getValue() != 0) {
                if (entry.getValue() > 0) {
                    recommendations.add("Add " + (int) entry.getValue().doubleValue() + " arc(s) to '" +
                            entry.getKey() + "' connector");
                } else {
                    recommendations.add("Remove " + (int) Math.abs(entry.getValue()) + " arc(s) from '" +
                            entry.getKey() + "' connector");
                }
            }
        }

        Map<Integer, String> arcTypeMapping = new HashMap<>();

        arcTypeMapping.put(0, "input");
        arcTypeMapping.put(1, "output");
        arcTypeMapping.put(2, "control");
        arcTypeMapping.put(3, "mechanism");

        for (Map.Entry<String, double[]> entry : processModelAnalysisBean.getFunctionsChanges().entrySet()) {
            for (int i = 0; i < entry.getValue().length; i++) {
                if (entry.getValue()[i] != 0) {
                    if (entry.getValue()[i] > 0) {
                        recommendations.add("Add " + (int) entry.getValue()[i] + " " +
                                arcTypeMapping.get(i) + " arc(s) to '" +
                                entry.getKey() + "' function");
                    } else {
                        recommendations.add("Remove " + (int) Math.abs(entry.getValue()[i]) + " " +
                                arcTypeMapping.get(i) + " arc(s) from '" +
                                entry.getKey() + "' function");
                    }
                }
            }
        }

        for (Map.Entry<String, double[]> entry : processModelAnalysisBean.getRoutingChanges().entrySet()) {
            if (entry.getValue()[0] != 0 || entry.getValue()[1] != 0) {
                String splitAction = entry.getValue()[0] > 0 ? "Add" : "Remove";
                String joinAction = entry.getValue()[1] > 0 ? "add" : "remove";

                recommendations.add(splitAction + " " + (int) Math.abs(entry.getValue()[0]) + " " +
                        entry.getKey() + "-split connector(s) or " + joinAction + " " +
                        (int) Math.abs(entry.getValue()[1]) + " " + entry.getKey() + "-join connector(s)");
            }
        }

        if (recommendations.isEmpty()) {
            recommendations.add("No changes required");
        }

        return recommendations;
    }
}
