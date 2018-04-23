package bp.retrieve.collection;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.generator.BPMNModelGenerator;

/**
 * Describes Business Process Model.
 * 
 * @author Andrii Kopp
 */
public abstract class GenericProcessModel {
	private String id;
	private String description;
	private String file;
	private String image;

	/**
	 * Initialize information about this Business Process Model. Information about
	 * the model must include a short description of the presented business process,
	 * a name of file that corresponds to this models, and a name of image that
	 * corresponds to this model. The most important aspect you should pay attention
	 * is that both files and images must be placed in
	 * {@code 'src/main/resources/repository'} - files into
	 * {@code 'src/main/resources/repository/files'}, and images into
	 * {@code 'src/main/resources/repository/images'} respectively.
	 * 
	 * @param description
	 *            - a short description of the presented business process;
	 * @param file
	 *            - a name of file that corresponds to this model;
	 * @param image
	 *            - a name of image that corresponds to this model.
	 */
	public GenericProcessModel(String description, String file, String image) {
		this.id = "id" + getModelDescription().hashCode();

		this.description = description;
		this.file = file;
		this.image = image;
	}

	/**
	 * Returns description of BP model in the form of RDF graph. Should be
	 * implemented in subclasses that describe specific BP models. BP models
	 * generator {@link BPMNModelGenerator} might be used for this purpose.
	 * 
	 * @return description of BP model in the form of RDF graph.
	 */
	public abstract BPModelRDFGraph getModelDescription();

	public String getId() {
		return id;
	}

	public String getName() {
		return getModelDescription().getName();
	}

	public String getDescription() {
		return description;
	}

	public String getFile() {
		return file;
	}

	public String getImage() {
		return image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
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
		GenericProcessModel other = (GenericProcessModel) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
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
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + "\n" + description + "\n" + file + "\n" + image + "\n" + getModelDescription();
	}
}
