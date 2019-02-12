package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.ProcessModelImportUtil;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;

public class ImportBPMNModelsApp {

    public static void main(String[] args) {
        ProcessModelRepository repository = new ProcessModelRepositoryJsonDB();

        ProcessModelImportUtil.importModelsFromBPMNDocuments(repository);
    }
}
