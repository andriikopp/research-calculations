package bp.retrieve.generator.system;

/**
 * Describes a Supporting System within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class SupportingSystem {
	private String URI;

	/**
	 * Creates a Supporting System with the specified name.
	 * 
	 * @param name
	 *            - the name of IT-system.
	 * @return created IT-system.
	 */
	public static SupportingSystem system(String name) {
		return new SupportingSystem(name);
	}

	private SupportingSystem(String name) {
		this.URI = name;
	}

	/**
	 * Returns the URI (name) of this object.
	 * 
	 * @return the URI of this object.
	 */
	public String getURI() {
		return URI;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((URI == null) ? 0 : URI.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupportingSystem other = (SupportingSystem) obj;
		if (URI == null) {
			if (other.URI != null)
				return false;
		} else if (!URI.equals(other.URI))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SupportingSystem [URI=" + URI + "]";
	}
}
