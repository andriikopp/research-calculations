package bp.retrieve.container.api;

import java.util.Map;

import bp.retrieve.BPModelRDFGraph;

/**
 * Business process models container.
 * 
 * @author Andrii Kopp
 */
public interface BPModelsContainer {
	/**
	 * Store business process model.
	 * 
	 * @param bpModel
	 *            - business process model to be stored.
	 */
	void storeBPModel(BPModelRDFGraph bpModel);

	/**
	 * Retrieve similar business process model.
	 * 
	 * @param bpModel
	 *            - model considered as a pattern.
	 * @return similar to the pattern business process models.
	 */
	Map<BPModelRDFGraph, Double> retrieveBPModels(BPModelRDFGraph bpModel);

	/**
	 * Clear container.
	 */
	void clear();

	int getOperationsCount();
}
