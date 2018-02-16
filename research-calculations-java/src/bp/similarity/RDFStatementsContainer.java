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
	private static final String ERR_SAVING = AppProperties.INSTANCE.getProperty("errSaving");
	private static final String ERR_EXISTS_STATEMENT = AppProperties.INSTANCE.getProperty("errExistsStatement");
	private static final String ERR_LETTERS = AppProperties.INSTANCE.getProperty("errLetters");
	private static final String ERR_SPACES = AppProperties.INSTANCE.getProperty("errSpaces");
	private static final String ERR_EMPTY = AppProperties.INSTANCE.getProperty("errEmpty");

	private static final String URI_BASE = "http://process-model.org/bpmodel/";
	private static final String PROPERTY = "ConnectingObject";

	private Model model;

	public RDFStatementsContainer() {
		this.model = ModelFactory.createDefaultModel();
	}

	/**
	 * Adds statement to the RDF statements container.
	 * 
	 * @param subject
	 *            - a subject of RDF triple;
	 * @param object
	 *            - an object of RDF triple.
	 */
	public void addStatement(String subject, String object) {
		if (subject.isEmpty() || object.isEmpty()) {
			throw new RuntimeException(ERR_EMPTY);
		}

		if (subject.contains(" ") || object.contains(" ")) {
			throw new RuntimeException(ERR_SPACES);
		}

		if (!subject.matches("[A-Za-z]*") || !object.matches("[A-Za-z]*")) {
			throw new RuntimeException(ERR_LETTERS);
		}

		Resource subjectResource = model.createResource(URI_BASE + subject);
		Property property = model.createProperty(URI_BASE + PROPERTY);
		Resource objectResource = model.createResource(URI_BASE + object);

		Statement statement = model.createStatement(subjectResource, property, objectResource);

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

		for (StmtIterator iter = model.listStatements(); iter.hasNext();) {
			Statement statement = iter.nextStatement();
			statements.add(statement.toString());
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
			model.write(writer, "N-TRIPLES");
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
