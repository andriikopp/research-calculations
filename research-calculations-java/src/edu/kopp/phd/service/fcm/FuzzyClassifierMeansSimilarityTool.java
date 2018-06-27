package edu.kopp.phd.service.fcm;

import edu.kopp.phd.model.flow.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuzzyClassifierMeansSimilarityTool {
    public static final double FUZZINESS_COEFFICIENT = 1.5;

    public static List<Map.Entry<Process, Double>> applyFuzzyClassification(List<Map.Entry<Process, Double>> similarities) {
        List<Map.Entry<Process, Double>> classifiedResults = new ArrayList<>(similarities);

        double sumOfValues = 0;

        for (int i = 0 ; i < classifiedResults.size(); i++) {
            double value = 1.0 / Math.pow(1.0 - classifiedResults.get(i).getValue(), 1.0 / (FUZZINESS_COEFFICIENT + 1.0));

            classifiedResults.get(i).setValue(value);

            sumOfValues += value;
        }

        for (int i = 0 ; i < classifiedResults.size(); i++)
            classifiedResults.get(i).setValue(classifiedResults.get(i).getValue() / sumOfValues);

        return classifiedResults;
    }
}
