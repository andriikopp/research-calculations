package edu.kopp.phd;

import edu.kopp.phd.repository.RDFRepository;
import edu.kopp.phd.service.AnalysisService;
import edu.kopp.phd.service.ControlFlowService;
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

        PortalView portalView = new PortalView(controlFlowService, validationService, analysisService);
        portalView.deployPortal();
    }
}
