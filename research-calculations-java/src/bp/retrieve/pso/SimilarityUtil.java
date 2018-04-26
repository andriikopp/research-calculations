package bp.retrieve.pso;

import java.util.HashSet;
import java.util.Set;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.similarity.SemanticSimilarity;
import bp.retrieve.similarity.Similarity;

/**
 * Provides utilities used to define various similarities of two business
 * process models represented in the form of RDF graph.
 * 
 * @author Andrii Kopp
 */
public class SimilarityUtil {
	// Similarity index that is used to define these measures.
	private static final Similarity SIMILARITY = new SemanticSimilarity();

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
				SIMILARITY);
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
				SIMILARITY);
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
				secondModel.extractFlowObjectsExceptGatewaysAndBPMNStartEndEvents(), SIMILARITY);
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

		Set<String> intersection = Similarity.intersection(firstModel.extractOrganizationalUnits(),
				secondModel.extractOrganizationalUnits());

		for (String resource : intersection) {
			first.addAll(firstModel.executes(resource));
		}

		for (String resource : intersection) {
			second.addAll(secondModel.executes(resource));
		}

		return Similarity.similarity(first, second, SIMILARITY);
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

		Set<String> intersection = Similarity.intersection(firstModel.extractSupportingSystems(),
				secondModel.extractSupportingSystems());

		for (String resource : intersection) {
			first.addAll(firstModel.executes(resource));
		}

		for (String resource : intersection) {
			second.addAll(secondModel.executes(resource));
		}

		return Similarity.similarity(first, second, SIMILARITY);
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

		Set<String> intersection = Similarity.intersection(firstModel.extractFlowObjects(),
				secondModel.extractFlowObjects());

		for (String resource : intersection) {
			first.addAll(firstModel.executes(resource));
		}

		for (String resource : intersection) {
			second.addAll(secondModel.executes(resource));
		}

		return Similarity.similarity(first, second, SIMILARITY);
	}
}
