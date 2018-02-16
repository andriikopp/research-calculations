package bp.similarity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import bp.AppProperties;

/**
 * Provides features for RDF statements collection management.
 * 
 * @author Andrii Kopp
 */
public class RDFStatementsContainer {
	private static final String RDF_FORMAT = AppProperties.INSTANCE.getProperty("rdfFormat");;
	private static final String RESOURCE_VALIDATION_RULE = AppProperties.INSTANCE.getProperty("resourceValidationRule");
	private static final String ERR_SAVING = AppProperties.INSTANCE.getProperty("errSaving");
	private static final String ERR_EXISTS_STATEMENT = AppProperties.INSTANCE.getProperty("errExistsStatement");
	private static final String ERR_LETTERS = AppProperties.INSTANCE.getProperty("errLetters");
	private static final String ERR_SPACES = AppProperties.INSTANCE.getProperty("errSpaces");
	private static final String ERR_EMPTY = AppProperties.INSTANCE.getProperty("errEmpty");

	private static final String URI_BASE_BPMODEL = "http://process-model.org/bpmodel/";
	private static final String URI_BASE_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String RDF_TYPE = "type";
	private static final String URI_BASE_BPMODEL_PREFIX = "bpmodel:";
	private static final String URI_BASE_RDF_PREFIX = "rdf:";

	private Model model;

	public RDFStatementsContainer() {
		this.model = ModelFactory.createDefaultModel();
	}

	/**
	 * Adds statement to the RDF statements container.
	 * 
	 * @param subject
	 *            - a subject of RDF triple;
	 * @param property
	 *            - a property of RDF triple;
	 * @param object
	 *            - an object of RDF triple.
	 */
	public void addStatement(String subject, String property, String object) {
		if (subject.isEmpty() || object.isEmpty()) {
			throw new RuntimeException(ERR_EMPTY);
		}

		if (subject.contains(" ") || object.contains(" ")) {
			throw new RuntimeException(ERR_SPACES);
		}

		if (!subject.matches(RESOURCE_VALIDATION_RULE) || !object.matches(RESOURCE_VALIDATION_RULE)) {
			throw new RuntimeException(ERR_LETTERS);
		}

		Resource subjectResource = model.createResource(URI_BASE_BPMODEL + subject);
		Property _property;
		Resource objectResource = model.createResource(URI_BASE_BPMODEL + object);

		if (property.equals(RDF_TYPE)) {
			_property = model.createProperty(URI_BASE_RDF + property);
		} else {
			_property = model.createProperty(URI_BASE_BPMODEL + property);
		}

		Statement statement = model.createStatement(subjectResource, _property, objectResource);

		if (model.contains(statement)) {
			throw new RuntimeException(ERR_EXISTS_STATEMENT);
		}

		model.add(statement);
	}

	/**
	 * Returns list of RDF statements.
	 * 
	 * @return RDF statements list.
	 */
	public List<String> getStatements() {
		List<String> statements = new ArrayList<String>();

		statements.add(String.format("@prefix rdf: <%s> .", URI_BASE_RDF));
		statements.add(String.format("@prefix bpmodel: <%s> .", URI_BASE_BPMODEL));

		for (StmtIterator iter = model.listStatements(); iter.hasNext();) {
			Statement statement = iter.nextStatement();

			String subject = statement.getSubject().toString().replaceAll(URI_BASE_BPMODEL, URI_BASE_BPMODEL_PREFIX);
			String property;

			if (statement.getPredicate().toString().contains(RDF_TYPE)) {
				property = statement.getPredicate().toString().replaceAll(URI_BASE_RDF, URI_BASE_RDF_PREFIX);
			} else {
				property = statement.getPredicate().toString().replaceAll(URI_BASE_BPMODEL, URI_BASE_BPMODEL_PREFIX);
			}

			String object = statement.getObject().toString().replaceAll(URI_BASE_BPMODEL, URI_BASE_BPMODEL_PREFIX);

			statements.add(String.format("%s %s %s .", subject, property, object));
		}

		return statements;
	}

	/**
	 * Allows to save RDF statements into a file.
	 * 
	 * @param targetFile
	 *            - a file where RDF statements should be saved.
	 */
	public void saveStatements(File targetFile) {
		try {
			FileWriter writer = new FileWriter(targetFile);
			model.write(writer, RDF_FORMAT);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(ERR_SAVING);
		}
	}

	/**
	 * Clears all statements from a container.
	 */
	public void clearStatements() {
		model.removeAll();
	}
}
