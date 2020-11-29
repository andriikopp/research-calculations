package edu.bpmanalysis.util;

import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

public class App {
    private static final boolean IMPORT_MODELS = false;

    public static void main(String[] args) {
        ProcessModelRepository processModelRepository = new ProcessModelRepositoryJsonDB();

        if (IMPORT_MODELS) {
            ProcessModelImportUtil.importModelsFromBPMNDocuments(processModelRepository);
        }

        for (ProcessModelBean bean : processModelRepository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);

            int mismatch = ConnectorInterplayUtil.mismatch(model);

            double mismatchImproved = ConnectorInterplayUtil.mismatch2(model);

            System.out.println(bean.getFileName() + "\t" +
                    mismatch + "\t" +
                    mismatchImproved);
        }
    }
}
