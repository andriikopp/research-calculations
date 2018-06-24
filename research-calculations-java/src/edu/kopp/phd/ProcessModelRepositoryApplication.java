package edu.kopp.phd;

import edu.kopp.phd.repository.RDFRepository;
import edu.kopp.phd.service.AnalysisService;
import edu.kopp.phd.service.ControlFlowService;
import edu.kopp.phd.service.SimilarityService;
import edu.kopp.phd.service.ValidationService;
import edu.kopp.phd.view.PortalView;

/**
 * Entry point of the ARIS Process Model Repository.
 */
public class ProcessModelRepositoryApplication {

    public static void run() {
        RDFRepository.getInstance().load();

        ControlFlowService controlFlowService = new ControlFlowService();
        ValidationService validationService = new ValidationService(controlFlowService);
        AnalysisService analysisService = new AnalysisService(controlFlowService);
        SimilarityService similarityService = new SimilarityService(controlFlowService);

        PortalView portalView = new PortalView();

        portalView.setControlFlowService(controlFlowService);
        portalView.setValidationService(validationService);
        portalView.setAnalysisService(analysisService);
        portalView.setSimilarityService(similarityService);

        portalView.deployPortal();
    }
}
