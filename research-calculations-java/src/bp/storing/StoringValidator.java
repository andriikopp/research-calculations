package bp.storing;

import bp.AppProperties;
import bp.storing.beans.Model;
import bp.storing.beans.Process;

/**
 * Provides process and model beans validation.
 * 
 * @author Andrii Kopp
 */
public class StoringValidator {
	private static final String INVALID_DATA = AppProperties.INSTANCE.getProperty("errInvalidData");

	/**
	 * Throws runtime exception if a process instance is null or its fields are
	 * null or empty.
	 * 
	 * @param process
	 *            - an instance of process.
	 */
	public static void validateProcess(Process process) {
		if (process == null) {
			throw new RuntimeException(INVALID_DATA);
		}

		if (process.getId() == null || process.getName() == null || process.getDescription() == null) {
			throw new RuntimeException(INVALID_DATA);
		}

		if (process.getId().isEmpty() || process.getName().isEmpty() || process.getDescription().isEmpty()) {
			throw new RuntimeException(INVALID_DATA);
		}
	}

	/**
	 * Throws runtime exception if a model instance is null or its fields are
	 * null or empty.
	 * 
	 * @param model
	 *            - an instance of model.
	 */
	public static void validateModel(Model model) {
		if (model == null) {
			throw new RuntimeException(INVALID_DATA);
		}

		if (model.getId() == null || model.getProcess() == null || model.getFile() == null) {
			throw new RuntimeException(INVALID_DATA);
		}

		if (model.getId().isEmpty() || model.getProcess().isEmpty() || model.getFile().isEmpty()) {
			throw new RuntimeException(INVALID_DATA);
		}
	}
}
