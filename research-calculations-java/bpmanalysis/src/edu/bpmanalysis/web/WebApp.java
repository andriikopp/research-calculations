package edu.bpmanalysis.web;

import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.web.controller.ProcessModelAnalysisControllerImpl;
import edu.bpmanalysis.web.controller.api.ProcessModelAnalysisController;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.UserRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.api.UserRepository;

public class WebApp {

    public static void main(String[] args) {
        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.cleanRepository();
        }

        ProcessModelRepository processModelRepository = new ProcessModelRepositoryJsonDB();

        if (Configuration.LOAD_MODELS_BEFORE_START) {
            ProcessModelImportUtil.importModelsFromBPMNDocuments(processModelRepository);
            ProcessModelImportUtil.importModelsFromEPCDocuments(processModelRepository);
            ProcessModelImportUtil.importModelsFromIDEF0Documents(processModelRepository);
            ProcessModelImportUtil.importModelsFromDFDDocuments(processModelRepository);

            ProcessModelImportUtil.importDiagramImages("svg");
        }

        UserRepository userRepository = new UserRepositoryJsonDB();

        ProcessModelAnalysisController controller = new ProcessModelAnalysisControllerImpl();
        controller.setRepository(processModelRepository);
        controller.setUserRepository(userRepository);

        controller.init();
    }
}
