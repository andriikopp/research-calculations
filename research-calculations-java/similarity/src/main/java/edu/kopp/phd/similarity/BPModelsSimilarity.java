package main.java.edu.kopp.phd.similarity;

import main.java.edu.kopp.phd.repository.domain.model.GenericModel;
import main.java.edu.kopp.phd.similarity.beans.BPModelsSimilarityCoefficients;
import main.java.edu.kopp.phd.similarity.utils.BPModelsSimilarityUtils;

/**
 * Similarity measure of business process models.
 *
 * @author Andrii Kopp
 */
public final class BPModelsSimilarity {

    /**
     * Returns similarity of business process models.
     *
     * @param first
     * @param second
     * @return
     */
    public static double similarity(GenericModel first, GenericModel second) {
        BPModelsSimilarityCoefficients coefficients = BPModelsSimilarityCoefficientsFactory.getCoefficients(first, second);

        return BPModelsSimilarityUtils.domainSimilarity(coefficients.getResourceIdentifiers(), first, second, first.getA())
                + BPModelsSimilarityUtils.domainSimilarity(coefficients.getFlowObjects(), first, second, first.getIsPredecessorOf())
                + BPModelsSimilarityUtils.domainSimilarity(coefficients.getOrganizationalUnits(), first, second, first.getIsPerformedBy())
                + BPModelsSimilarityUtils.domainSimilarity(coefficients.getApplicationSystems(), first, second, first.getIsSupportedBy())
                + 0.5 * BPModelsSimilarityUtils.domainSimilarity(coefficients.getInputsAndOutputs(), first, second, first.getRequires())
                + 0.5 * BPModelsSimilarityUtils.domainSimilarity(coefficients.getInputsAndOutputs(), first, second, first.getProduces())
                + BPModelsSimilarityUtils.domainSimilarity(coefficients.getRegulations(), first, second, first.getIsRegulatedBy());
    }
}
