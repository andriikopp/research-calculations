package bp.retrieve.web.service;

import java.util.ArrayList;
import java.util.List;

import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.collection.ProcessModelRepository;

/**
 * Provides business logic of the repository application.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelRepositoryService {
	private static ProcessModelRepository processModelRepository;

	/**
	 * Returns the list of process models in the specified range.
	 * 
	 * @param from
	 *            - the start position of the ragne;
	 * @param to
	 *            - the end position of the range.
	 * @return the list of process models.
	 */
	public static List<GenericProcessModel> retrieveOnRange(int from, int to) {
		List<GenericProcessModel> processModels = processModelRepository.retrieveAll();

		if (from < 0 || to >= processModels.size() || (from > to))
			throw new IllegalArgumentException("Invalid range!");

		List<GenericProcessModel> processModelsOnRange = new ArrayList<GenericProcessModel>();

		for (int index = from; index <= to; index++)
			processModelsOnRange.add(processModels.get(index));

		return processModelsOnRange;
	}

	/**
	 * Returns the list of process models which name or/and description matches the
	 * specified keywords.
	 * 
	 * @param keywords
	 *            - the search request that contains keywords.
	 * @return the list of process models.
	 */
	public static List<GenericProcessModel> retrieveByKeywords(String keywords) {
		if (keywords == null || keywords.isEmpty())
			throw new IllegalArgumentException("Invalid keywords!");

		String[] keywordsArray = keywords.split("\\s+");

		List<GenericProcessModel> processModels = processModelRepository.retrieveAll();
		List<GenericProcessModel> processModelsByKeywords = new ArrayList<GenericProcessModel>();

		for (GenericProcessModel processModel : processModels) {
			for (String keyword : keywordsArray) {
				// Check name of the process model.
				if (processModel.getName().contains(keyword)) {
					processModelsByKeywords.add(processModel);
					continue;
				}

				// Check description of the process model.
				if (processModel.getDescription().contains(keyword)) {
					processModelsByKeywords.add(processModel);
					continue;
				}
			}
		}

		return processModelsByKeywords;
	}

	/**
	 * Returns the list of process models that are similar to the model retrieved by
	 * the given ID.
	 * 
	 * @param id
	 *            - the ID of model that might be considered as a search pattern.
	 * @return the list of process models.
	 */
	public static List<GenericProcessModel> retrieveSimilar(String id) {
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("Invalid model!");

		GenericProcessModel processModel = processModelRepository.retrieve(id);

		return processModelRepository.find(processModel);
	}

	/**
	 * Returns the list of process models stored in the repository.
	 * 
	 * @return
	 */
	public static List<GenericProcessModel> retrieveAll() {
		return processModelRepository.retrieveAll();
	}

	public static void setProcessModelRepository(ProcessModelRepository processModelRepository) {
		ProcessModelRepositoryService.processModelRepository = processModelRepository;
	}
}
