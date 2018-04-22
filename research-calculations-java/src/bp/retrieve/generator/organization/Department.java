package bp.retrieve.generator.organization;

/**
 * Describes a Department within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class Department extends OrganizationalUnit {

	/**
	 * Creates a Department with the specified name.
	 * 
	 * @param name
	 *            - the name of department.
	 * @return created department.
	 */
	public static Department department(String name) {
		return new Department(name);
	}

	private Department(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Department [getURI()=" + getURI() + "]";
	}
}
