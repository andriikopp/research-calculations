package bp.retrieve.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.container.api.BPModelsContainer;

/**
 * Collection of business process models.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelRepository {
	/**
	 * Instance of the business process model collection.
	 */
	public static final ProcessModelRepository INSTANCE = new ProcessModelRepository();

	private BPModelsContainer processModelsContainer;
	private Map<BPModelRDFGraph, GenericProcessModel> processModelsMapping;

	/**
	 * Returns a list of business process models that are similar enough to the
	 * given process model or fragment thereof.
	 * 
	 * @param processModel
	 *            - given process model that acts as a pattern for similarity
	 *            search.
	 * @return a list of business process models found by the specified pattern.
	 */
	public List<GenericProcessModel> find(GenericProcessModel processModel) {
		List<GenericProcessModel> retrievedModels = new ArrayList<GenericProcessModel>();

		for (Map.Entry<BPModelRDFGraph, Double> entry : processModelsContainer
				.retrieveBPModels(processModel.getModelDescription()).entrySet()) {
			retrievedModels.add(processModelsMapping.get(entry.getKey()));
		}

		return retrievedModels;
	}

	/**
	 * Store a given business process model into the repository.
	 * 
	 * @param processModel
	 *            - stored process model.
	 */
	public void add(GenericProcessModel processModel) {
		if (processModelsMapping.containsKey(processModel.getModelDescription()))
			throw new IllegalStateException("Process model " + processModel.getId() + " already exists!");

		processModelsContainer.storeBPModel(processModel.getModelDescription());
		processModelsMapping.put(processModel.getModelDescription(), processModel);
	}

	/**
	 * Store multiple business process models into the repository.
	 * 
	 * @param processModels
	 *            - a list of business process models that might be stored.
	 */
	public void addAll(List<GenericProcessModel> processModels) {
		for (GenericProcessModel processModel : processModels) {
			add(processModel);
		}
	}

	private ProcessModelRepository() {
		this.processModelsContainer = null;
		this.processModelsMapping = new HashMap<BPModelRDFGraph, GenericProcessModel>();
	}

	public BPModelsContainer getProcessModelsContainer() {
		return processModelsContainer;
	}

	public void setProcessModelsContainer(BPModelsContainer processModelsContainer) {
		this.processModelsContainer = processModelsContainer;
	}
}
