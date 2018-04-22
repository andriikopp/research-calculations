package bp.retrieve.generator.organization;

/**
 * Describes a Role within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class Role extends OrganizationalUnit {

	/**
	 * Creates a Role with the specified name.
	 * 
	 * @param name
	 *            - the name of role.
	 * @return created role.
	 */
	public static Role role(String name) {
		return new Role(name);
	}

	private Role(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Role [getURI()=" + getURI() + "]";
	}
}
