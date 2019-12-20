package edu.bpmanalysis.web.bpmq.controller;

import com.google.gson.Gson;
import edu.bpmanalysis.web.bpmq.entity.BPMQualitySummary;
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

        int sizeQualityCount = bpModelRepository.countWithInvalidNodes();
        int degreesQualityCount = bpModelRepository.countWithInvalidDegrees();
        int eventsQualityCount = bpModelRepository.countWithInvalidEvents();
        int gatewaysQualityCount = bpModelRepository.countWithGatewaysMismatch();
        int orQualityCount = bpModelRepository.countWithOrGateways();

        return new Gson().toJson(new BPMQualitySummary(
                totalModels,
                correctModels,
                invalidModels,
                sizeQualityCount,
                degreesQualityCount,
                eventsQualityCount,
                gatewaysQualityCount,
                orQualityCount
        ));
    }
}
