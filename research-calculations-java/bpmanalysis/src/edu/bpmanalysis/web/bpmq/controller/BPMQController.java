package edu.bpmanalysis.web.bpmq.controller;

import com.google.gson.Gson;
import edu.bpmanalysis.web.bpmq.entity.BPMQualitySummary;
import edu.bpmanalysis.web.bpmq.entity.BPModel;
import edu.bpmanalysis.web.bpmq.repository.api.BPModelRepository;
import edu.bpmanalysis.web.bpmq.util.BPMOntologyUtil;
import org.apache.jena.rdf.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @RequestMapping(value = "query", method = RequestMethod.GET)
    public @ResponseBody
    String queryOntology(@RequestParam("property") String property,
                         @RequestParam("object") String object) {
        Model model = BPMOntologyUtil.MODEL;

        Selector selector = new SimpleSelector(null, model.createProperty(property), object);
        StmtIterator iterator = model.listStatements(selector);

        List<BPModel> results = new ArrayList<>();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            String fileName = statement.getSubject().getURI();
            results.addAll(bpModelRepository.findByFileName(fileName));
        }

        return new Gson().toJson(results);
    }

    @GetMapping("/allObjects")
    public String getAllObjects() {
        Model model = BPMOntologyUtil.MODEL;

        NodeIterator iterator = model.listObjects();

        List<String> result = new ArrayList<>();

        while (iterator.hasNext()) {
            result.add(iterator.nextNode().asLiteral().getString());
        }

        return new Gson().toJson(result);
    }
}
