package edu.bpmanalysis.web.controller.api;

import edu.bpmanalysis.web.model.api.ProcessModelRepository;

public interface ProcessModelAnalysisController {

    void init();

    void setRepository(ProcessModelRepository repository);
}
