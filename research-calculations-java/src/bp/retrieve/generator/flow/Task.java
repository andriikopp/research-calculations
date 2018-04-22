package bp.retrieve.generator.flow;

/**
 * Describes a Task within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class Task extends ProcessFlowObject {

	/**
	 * Creates a Task with the specified name.
	 * 
	 * @param name
	 *            - the name of task.
	 * @return created task.
	 */
	public static Task task(String name) {
		return new Task(name);
	}

	private Task(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Task [getURI()=" + getURI() + "]";
	}
}
