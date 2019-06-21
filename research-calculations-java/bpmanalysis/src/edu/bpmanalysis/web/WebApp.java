package edu.bpmanalysis.web;

import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.web.controller.ProcessModelAnalysisControllerImpl;
import edu.bpmanalysis.web.controller.api.ProcessModelAnalysisController;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;

public class WebApp {

    public static void main(String[] args) {
        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.cleanRepository();
        }

        ProcessModelRepository repository = new ProcessModelRepositoryJsonDB();

        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.importModelsFromBPMNDocuments(repository);
        }

        ProcessModelAnalysisController controller = new ProcessModelAnalysisControllerImpl();
        controller.setRepository(repository);

        controller.init();
    }
}
