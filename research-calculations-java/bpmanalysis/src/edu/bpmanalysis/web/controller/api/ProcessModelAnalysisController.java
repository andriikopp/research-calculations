package edu.bpmanalysis.web.controller.api;

import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.api.UserRepository;

public interface ProcessModelAnalysisController {

    void init();

    void setRepository(ProcessModelRepository repository);

    void setUserRepository(UserRepository userRepository);

    void setResultsRepository(AnalysisResultsRepository resultsRepository);
}
