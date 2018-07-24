package edu.kopp.phd.service;

import edu.kopp.phd.model.flow.Process;
import edu.kopp.phd.service.fcm.FuzzyClassifierMeansSimilarityTool;
import edu.kopp.phd.service.similarity.ConcreteSimilarityMethodFactory;
import edu.kopp.phd.service.similarity.SimilarityMethodFactory;
import edu.kopp.phd.service.similarity.api.SimilarityMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityService {
    private ControlFlowService controlFlowService;

    private SimilarityMethod similarityMethod = ConcreteSimilarityMethodFactory.createInstance().getSimilarityMethod();

    public List<Map.Entry<Process, Double>> getSimilarProcessesByProcessPattern(Process pattern) {
        List<Map.Entry<Process, Double>> similarProcesses = new ArrayList<>();

        for (Process process : controlFlowService.getAllProcesses()) {
            if (!process.equals(pattern)) {
                double similarity = similarityMethod.calculateSimilarityMeasure(process, pattern);

                if (similarity > 0)
                    similarProcesses.add(new HashMap.SimpleEntry<>(process, similarity));
            }
        }

        similarProcesses.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        return similarProcesses;
    }

    public void setControlFlowService(ControlFlowService controlFlowService) {
        this.controlFlowService = controlFlowService;
    }
}
