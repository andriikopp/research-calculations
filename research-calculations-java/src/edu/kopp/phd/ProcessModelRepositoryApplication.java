package edu.kopp.phd;

import edu.kopp.phd.repository.RDFRepository;
import edu.kopp.phd.service.AnalysisService;
import edu.kopp.phd.service.SimilarityService;
import edu.kopp.phd.service.ValidationService;
import edu.kopp.phd.view.PortalView;

/**
 * Entry point of the ARIS Process Model Repository.
 */
public class ProcessModelRepositoryApplication {

    public static void run() {
        RDFRepository.getInstance().load();

        ValidationService validationService = new ValidationService();
        AnalysisService analysisService = new AnalysisService();
        SimilarityService similarityService = new SimilarityService();

        PortalView portalView = new PortalView();

        portalView.setValidationService(validationService);
        portalView.setAnalysisService(analysisService);
        portalView.setSimilarityService(similarityService);

        portalView.deployPortal();
    }
}
