package edu.kopp.phd.express.search;

import edu.kopp.phd.express.metamodel.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessControlFlowPatternMatching {

    public static double match(Model first, Model second) {
        double value = 0;

        Map<String, Double> firstVec = new HashMap<>();

        for (int i = 0; i < first.getFunctions().size(); i++) {
            String key = "<" + first.getFunctions().get(i).getPreceding() + "," +
                    first.getFunctions().get(i).getSubsequent() + ">";

            if (firstVec.containsKey(key)) {
                double count = firstVec.get(key) + 1.0;
                firstVec.put(key, count);
            } else {
                firstVec.put(key, 1.0);
            }
        }

        Map<String, Double> secondVec = new HashMap<>();

        for (int i = 0; i < second.getFunctions().size(); i++) {
            String key = "<" + second.getFunctions().get(i).getPreceding() + "," +
                    second.getFunctions().get(i).getSubsequent() + ">";

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
                value += firstVec.get(key);
            }
        }

        return value;
    }
}
