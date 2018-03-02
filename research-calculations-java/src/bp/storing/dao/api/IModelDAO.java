package bp.storing.dao.api;

import java.util.List;

import bp.storing.beans.Model;

/**
 * Provides access to a storage of process models.
 * 
 * @author Andrii Kopp
 */
public interface IModelDAO {
	/**
	 * Stores a certain process model to a storage.
	 * 
	 * @param process
	 *            - a certain process model.
	 */
	void store(Model model);

	/**
	 * Retrieves a certain process model by its id.
	 * 
	 * @param id
	 *            - an id of a certain process model.
	 * @return a certain process model.
	 */
	Model retrieveById(String id);

	/**
	 * Retrieves a list of all process models from a storage.
	 * 
	 * @return a list of process models.
	 */
	List<Model> retrieve();
}
