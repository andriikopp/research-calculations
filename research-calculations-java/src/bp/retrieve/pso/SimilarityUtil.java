package bp.retrieve.pso;

import java.util.HashSet;
import java.util.Set;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.similarity.FuzzySimilarityUtil;
import bp.retrieve.similarity.Similarity;

/**
 * Provides utilities used to define various similarities of two business
 * process models represented in the form of RDF graph.
 * 
 * @author Andrii Kopp
 */
public class SimilarityUtil {
	// Similarity index that is used to define these measures.
	private static Similarity similarity;

	/**
	 * Set a similarity index.
	 * 
	 * @param similarity
	 *            - instance of the similarity index.
	 */
	public static void setSimilarity(Similarity similarity) {
		SimilarityUtil.similarity = similarity;
	}

	/**
	 * Returns the value of label similarity of organizational units.
	 * 
	 * @param firstModel
	 *            - first BP model;
	 * @param secondModel
	 *            - second BP model.
	 * @return the value of label similarity of organizational units.
	 */
	public static double getOrganizationalUnitsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		return Similarity.similarity(firstModel.extractOrganizationalUnits(), secondModel.extractOrganizationalUnits(),
				similarity);
	}

	/**
	 * Returns the value of label similarity of supporting systems.
	 * 
	 * @param firstModel
	 *            - first BP model;
	 * @param secondModel
	 *            - second BP model.
	 * @return the value of label similarity of supporting systems.
	 */
	public static double getSupportingSystemSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		return Similarity.similarity(firstModel.extractSupportingSystems(), secondModel.extractSupportingSystems(),
				similarity);
	}

	/**
	 * Returns the value of label similarity of process flow objects.
	 * 
	 * @param firstModel
	 *            - first BP model;
	 * @param secondModel
	 *            - second BP model.
	 * @return the value of label similarity of process flow objects.
	 */
	public static double getProcessFlowObjectsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		return Similarity.similarity(firstModel.extractFlowObjectsExceptGatewaysAndBPMNStartEndEvents(),
				secondModel.extractFlowObjectsExceptGatewaysAndBPMNStartEndEvents(), similarity);
	}

	/**
	 * Returns the value of structure similarity of organizational units.
	 * 
	 * @param firstModel
	 *            - first BP model;
	 * @param secondModel
	 *            - second BP model.
	 * @return the value of structure similarity of organizational units.
	 */
	public static double getExecutesSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = new HashSet<String>();
		Set<String> second = new HashSet<String>();

		// Use fuzzy intersection of organizational units.
		Set<String> fuzzyIntersection = FuzzySimilarityUtil
				.fuzzyIntersection(firstModel.extractOrganizationalUnits(), secondModel.extractOrganizationalUnits())
				.keySet();

		for (String resource : fuzzyIntersection) {
			first.addAll(firstModel.executes(resource));
		}

		for (String resource : fuzzyIntersection) {
			second.addAll(secondModel.executes(resource));
		}

		return Similarity.similarity(first, second, similarity);
	}

	/**
	 * Returns the value of structure similarity of supporting systems.
	 * 
	 * @param firstModel
	 *            - first BP model;
	 * @param secondModel
	 *            - second BP model.
	 * @return the value of structure similarity of supporting systems.
	 */
	public static double getUsedBySimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = new HashSet<String>();
		Set<String> second = new HashSet<String>();

		// Use fuzzy intersection of IT-systems.
		Set<String> fuzzyIntersection = FuzzySimilarityUtil
				.fuzzyIntersection(firstModel.extractSupportingSystems(), secondModel.extractSupportingSystems())
				.keySet();

		for (String resource : fuzzyIntersection) {
			first.addAll(firstModel.usedBy(resource));
		}

		for (String resource : fuzzyIntersection) {
			second.addAll(secondModel.usedBy(resource));
		}

		return Similarity.similarity(first, second, similarity);
	}

	/**
	 * Returns the value of structure similarity of process flow objects.
	 * 
	 * @param firstModel
	 *            - first BP model;
	 * @param secondModel
	 *            - second BP model.
	 * @return the value of structure similarity of process flow objects.
	 */
	public static double getTriggersSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = new HashSet<String>();
		Set<String> second = new HashSet<String>();

		// Use fuzzy intersection of process flow objects.
		Set<String> fuzzyIntersection = FuzzySimilarityUtil
				.fuzzyIntersection(firstModel.extractFlowObjects(), secondModel.extractFlowObjects()).keySet();

		for (String resource : fuzzyIntersection) {
			first.addAll(firstModel.triggers(resource));
		}

		for (String resource : fuzzyIntersection) {
			second.addAll(secondModel.triggers(resource));
		}

		return Similarity.similarity(first, second, similarity);
	}
}
