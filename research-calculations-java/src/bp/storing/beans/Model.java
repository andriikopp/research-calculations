package bp.storing.beans;

import java.util.UUID;

/**
 * Describes process model entity.
 * 
 * @author Andrii Kopp
 */
public class Model {
	private String id;
	// Process ID.
	private String process;
	// Model's file name stored in catalog.
	private String file;

	public Model() {
		this.id = UUID.randomUUID().toString();
	}

	public Model(String id, String process, String file) {
		this.id = id;
		this.process = process;
		this.file = file;
	}

	public Model(Model model) {
		this.id = model.id;
		this.process = model.process;
		this.file = model.file;
	}

	@Override
	public String toString() {
		return file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
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
		Model other = (Model) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
