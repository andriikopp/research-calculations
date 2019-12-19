package edu.bpmanalysis.web.bpmq.controller;

import com.google.gson.Gson;
import edu.bpmanalysis.web.bpmq.entity.BPMQualitySummary;
import edu.bpmanalysis.web.bpmq.entity.BinBPMQualityTuple;
import edu.bpmanalysis.web.bpmq.repository.api.BPModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BPMQController {

    @Autowired
    @Qualifier("jdbcBPModelRepository")
    private BPModelRepository bpModelRepository;

    @GetMapping("/allModels")
    public String getAllModels() {
        return new Gson().toJson(bpModelRepository.findAll());
    }

    @GetMapping("/summary")
    public String getSummary() {
        int totalModels = bpModelRepository.count();
        int correctModels = bpModelRepository.countCorrectModels();
        int invalidModels = bpModelRepository.countInvalidModels();

        int sizeQualityCount = bpModelRepository.findModelsByBinaryQualityTuple(
                new BinBPMQualityTuple(1, 0, 0, 0, 0))
                .size();
        int degreesQualityCount = bpModelRepository.findModelsByBinaryQualityTuple(
                new BinBPMQualityTuple(0, 1, 0, 0, 0))
                .size();
        int eventsQualityCount = bpModelRepository.findModelsByBinaryQualityTuple(
                new BinBPMQualityTuple(0, 0, 1, 0, 0))
                .size();
        int gatewaysQualityCount = bpModelRepository.findModelsByBinaryQualityTuple(
                new BinBPMQualityTuple(0, 0, 0, 1, 0))
                .size();
        int orQualityCount = bpModelRepository.findModelsByBinaryQualityTuple(
                new BinBPMQualityTuple(0, 0, 0, 0, 1))
                .size();

        BPMQualitySummary qualitySummary = new BPMQualitySummary(
                totalModels,
                correctModels,
                invalidModels,
                sizeQualityCount,
                degreesQualityCount,
                eventsQualityCount,
                gatewaysQualityCount,
                orQualityCount
        );

        return new Gson().toJson(qualitySummary);
    }
}
