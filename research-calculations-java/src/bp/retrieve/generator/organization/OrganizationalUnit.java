package bp.retrieve.generator.organization;

/**
 * Describes an Organizational Unit within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class OrganizationalUnit {
	private String URI;

	public OrganizationalUnit(String name) {
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
		OrganizationalUnit other = (OrganizationalUnit) obj;
		if (URI == null) {
			if (other.URI != null)
				return false;
		} else if (!URI.equals(other.URI))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrganizationalUnit [URI=" + URI + "]";
	}
}
