package bp.retrieve;

import java.security.InvalidParameterException;
import java.util.Arrays;

import bp.retrieve.similarity.Similarity;

/**
 * This extension allow users to customize similarity method and probably other
 * features of the superclass in future.
 * 
 * @author Andrii Kopp
 */
public class CustomizableBPModelsSimilarity extends BPModelsSimilarity {

	/**
	 * Allows to define specific implementation of the similarity measure between
	 * two sets of strings passed there as arguments.
	 * 
	 * @param similarity
	 */
	public void defineSimilarityMethod(Similarity similarity) {
		similarityImpl = similarity;
	}

	/**
	 * Allows to increase or decrease current similarity level.
	 * 
	 * @param similarityLevel
	 */
	public void modifySimilarityLevel(double similarityLevel) {
		setSimilarityLevel(getSimilarityLevel() + similarityLevel);
	}

	/**
	 * Overrides existing method with extra check for domain names.
	 */
	@Override
	public void setDomainCoefficient(String domain, double coefficient) {
		if (!Arrays.asList(UNITS_COEFF, SYSTEMS_COEFF, FLOW_OBJECTS_COEFF, INPUTS_COEFF, OUTPUTS_COEFF, KPIS_COEFF)
				.contains(domain)) {
			throw new InvalidParameterException();
		}

		super.setDomainCoefficient(domain, coefficient);
	}

	/**
	 * Overrides existing method with extra check for similarity types.
	 */
	@Override
	public void setSimilarityCoefficient(String similarity, double coefficient) {
		if (!Arrays.asList(SEMANTIC_COEFF, STRUCTURE_COEFF).contains(similarity)) {
			throw new InvalidParameterException();
		}

		super.setSimilarityCoefficient(similarity, coefficient);
	}

	/**
	 * Allows to compare models only using label similarity.
	 */
	public void compareByLabels() {
		setSimilarityCoefficient(SEMANTIC_COEFF, 1);
		setSimilarityCoefficient(STRUCTURE_COEFF, 0);
	}

	/**
	 * Allows to compare models only using structure similarity.
	 */
	public void compareByStructure() {
		setSimilarityCoefficient(SEMANTIC_COEFF, 0);
		setSimilarityCoefficient(STRUCTURE_COEFF, 1);
	}

	/**
	 * Allows to compare models only by flow objects.
	 */
	public void compareByFlowObjects() {
		setDomainCoefficient(UNITS_COEFF, 0);
		setDomainCoefficient(SYSTEMS_COEFF, 0);
		setDomainCoefficient(FLOW_OBJECTS_COEFF, 1);
		setDomainCoefficient(INPUTS_COEFF, 0);
		setDomainCoefficient(OUTPUTS_COEFF, 0);
		setDomainCoefficient(KPIS_COEFF, 0);
	}
}
