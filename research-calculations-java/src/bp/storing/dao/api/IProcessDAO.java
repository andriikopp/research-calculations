package bp.storing.dao.api;

import java.util.List;
import bp.storing.beans.Process;

/**
 * Provides access to a storage of processes.
 * 
 * @author Andrii Kopp
 */
public interface IProcessDAO {
	/**
	 * Stores a certain process to a storage.
	 * 
	 * @param process
	 *            - a certain process.
	 */
	void store(Process process);

	/**
	 * Retrieves a certain process by its id.
	 * 
	 * @param id
	 *            - an id of a certain process.
	 * @return a certain process.
	 */
	Process retrieveById(String id);

	/**
	 * Retrieves a certain process by its name.
	 * 
	 * @param name
	 *            - an name of a certain process.
	 * @return a certain process.
	 */
	Process retrieveByName(String name);

	/**
	 * Retrieves a list of all processes from a storage.
	 * 
	 * @return a list of processes.
	 */
	List<Process> retrieve();
}
