package bp.retrieve.container;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.BPModelsSimilarity;
import bp.retrieve.container.api.BPModelsContainer;

/**
 * Business process model container implementation, based on a simple
 * collection.
 * 
 * @author Andrii Kopp
 */
public class SimpleBPModelsContainer implements BPModelsContainer {
	// The set of business process models.
	private Set<BPModelRDFGraph> models;

	private int operationsCount;

	// Implementation of a similarity measure.
	private BPModelsSimilarity bpModelsSimilarity;

	public SimpleBPModelsContainer(BPModelsSimilarity bpModelsSimilarity) {
		this.models = new HashSet<BPModelRDFGraph>();
		this.bpModelsSimilarity = bpModelsSimilarity;
	}

	@Override
	public void storeBPModel(BPModelRDFGraph bpModel) {
		models.add(bpModel);
	}

	@Override
	public Map<BPModelRDFGraph, Double> retrieveBPModels(BPModelRDFGraph bpModel) {
		operationsCount = 0;

		// Collection of models, similar to a considered one.
		Map<BPModelRDFGraph, Double> retrievedBPModels = new HashMap<BPModelRDFGraph, Double>();

		// Check each model in the collection,
		for (BPModelRDFGraph model : models) {
			// except the same model.
			if (!model.equals(bpModel)) {
				double similarity = bpModelsSimilarity.compareBPModelRDFGraphs(model, bpModel);
				operationsCount++;

				if (similarity >= bpModelsSimilarity.getSimilarityLevel()) {
					retrievedBPModels.put(model, similarity);
				}
			}
		}

		return retrievedBPModels;
	}

	@Override
	public void clear() {
		this.models.clear();
	}

	@Override
	public int getOperationsCount() {
		return operationsCount;
	}
}
