package bp.retrieve.container;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.BPModelsSimilarity;
import bp.retrieve.container.api.BPModelsContainer;

/**
 * Business process model container implementation, based on a hash table like
 * structure.
 * 
 * @author Andrii Kopp
 */
public class TableBPModelsContainer implements BPModelsContainer {
	// Primary models <-> related models collection.
	private Map<BPModelRDFGraph, Set<BPModelRDFGraph>> models;

	// Dictionaries according to the BP modeling domains.
	private Map<String, Set<BPModelRDFGraph>> organizationalUnitsDictionary;
	private Map<String, Set<BPModelRDFGraph>> supportingSystemsDictionary;
	private Map<String, Set<BPModelRDFGraph>> processFlowObjectsDictionary;

	private int operationsCount;

	// Implementation of a similarity measure.
	private BPModelsSimilarity bpModelsSimilarity;

	public TableBPModelsContainer(BPModelsSimilarity bpModelsSimilarity) {
		this.models = new HashMap<BPModelRDFGraph, Set<BPModelRDFGraph>>();

		this.organizationalUnitsDictionary = new HashMap<String, Set<BPModelRDFGraph>>();
		this.supportingSystemsDictionary = new HashMap<String, Set<BPModelRDFGraph>>();
		this.processFlowObjectsDictionary = new HashMap<String, Set<BPModelRDFGraph>>();

		this.bpModelsSimilarity = bpModelsSimilarity;
	}

	@Override
	public void storeBPModel(BPModelRDFGraph bpModel) {
		if (models.containsKey(bpModel)) {
			throw new IllegalArgumentException();
		}

		// Extract resources from a model according to the domains.
		Set<String> organizationalUnits = bpModel.extractOrganizationalUnits();
		Set<String> supportingSystems = bpModel.extractSupportingSystems();

		// Extract flow objects except gateways to provide an adequate closeness of
		// models.
		Set<String> processFlowObjects = bpModel.extractFlowObjects(false);

		// Set of models, related to the considered one.
		Set<BPModelRDFGraph> relatedModels = new LinkedHashSet<BPModelRDFGraph>();

		// Use utility method to perform preparation phase of the storing process.
		storeBPModelDictionary(organizationalUnitsDictionary, organizationalUnits, relatedModels, bpModel);
		storeBPModelDictionary(supportingSystemsDictionary, supportingSystems, relatedModels, bpModel);
		storeBPModelDictionary(processFlowObjectsDictionary, processFlowObjects, relatedModels, bpModel);

		// Store considered model and related ones into the collection.
		models.put(bpModel, relatedModels);
	}

	@Override
	public Map<BPModelRDFGraph, Double> retrieveBPModels(BPModelRDFGraph bpModel) {
		operationsCount = 0;

		// Collection of models, similar to a considered one.
		Map<BPModelRDFGraph, Double> similarModels = new HashMap<BPModelRDFGraph, Double>();

		// Check each model, related to the considered one.
		for (BPModelRDFGraph model : models.get(bpModel)) {
			double similarity = bpModelsSimilarity.compareBPModelRDFGraphs(model, bpModel);
			operationsCount++;

			if (similarity >= bpModelsSimilarity.getSimilarityLevel()) {
				similarModels.put(model, similarity);
			}
		}

		return similarModels;
	}

	// Utility method used to store models.
	private static void storeBPModelDictionary(Map<String, Set<BPModelRDFGraph>> dictionary, Set<String> resources,
			Set<BPModelRDFGraph> relatedModels, BPModelRDFGraph bpModel) {
		// Considering resources of a certain BP modeling domain.
		for (String resource : resources) {

			// If considered resource is already stored in a dictionary,
			if (dictionary.containsKey(resource)) {

				// retrieve all models, related to this resources, and consider them as related
				// to a considered model (bpModel).
				relatedModels.addAll(dictionary.get(resource));

				// Then, also assign considered model (bpModel) as a related to a certain
				// resource.
				dictionary.get(resource).add(bpModel);
			} else {
				// If a dictionary doesn't contain such resource,
				Set<BPModelRDFGraph> resourceRelatedModels = new LinkedHashSet<BPModelRDFGraph>();
				resourceRelatedModels.add(bpModel);

				// store it into a dictionary with the considered model (bpModel), assigned as a
				// related.
				dictionary.put(resource, resourceRelatedModels);
			}
		}
	}

	@Override
	public void clear() {
		this.models.clear();

		this.organizationalUnitsDictionary.clear();
		this.supportingSystemsDictionary.clear();
		this.processFlowObjectsDictionary.clear();
	}

	@Override
	public int getOperationsCount() {
		return operationsCount;
	}
}
