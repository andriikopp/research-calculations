package edu.bpmanalysis.web;

import edu.bpmanalysis.web.controller.ProcessModelAnalysisControllerImpl;
import edu.bpmanalysis.web.controller.api.ProcessModelAnalysisController;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;

public class WebApp {

    public static void main(String[] args) {
        ProcessModelAnalysisController service = new ProcessModelAnalysisControllerImpl();
        service.init(new ProcessModelRepositoryJsonDB());
    }
}
