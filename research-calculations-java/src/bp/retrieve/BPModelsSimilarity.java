package bp.retrieve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.jena.rdf.model.ModelFactory;

import bp.AppProperties;
import bp.retrieve.similarity.Similarity;
import bp.storing.beans.Model;
import bp.storing.beans.Process;
import bp.storing.dao.api.IModelDAO;
import bp.storing.dao.api.IProcessDAO;

/**
 * Provides similarity search of business process models.
 * 
 * @author Andrii Kopp
 */
public class BPModelsSimilarity {
	private static final String RETRIEVE_PROCESSES = AppProperties.INSTANCE.getProperty("retrProcesses");
	private static final String RDF_FORMAT = AppProperties.INSTANCE.getProperty("rdfFormat");
	private static final String TRIPLESTORE_PATH = AppProperties.INSTANCE.getProperty("triplestorePath");

	public static final String UNITS_COEFF = "units";
	public static final String SYSTEMS_COEFF = "systems";
	public static final String FLOW_OBJECTS_COEFF = "flow";
	public static final String INPUTS_COEFF = "inputs";
	public static final String OUTPUTS_COEFF = "outputs";
	public static final String KPIS_COEFF = "kpis";

	public static final String SEMANTIC_COEFF = "semantic";
	public static final String STRUCTURE_COEFF = "structure";

	private static boolean isLoggingOn = true;

	private IProcessDAO processDAO;
	private IModelDAO modelDAO;

	/**
	 * This properties might be considered as customizable in further extensions.
	 */
	protected double similarityLevel;
	protected Map<String, Double> domainCoefficients;
	protected Map<String, Double> similarityCoefficients;

	/**
	 * This property might be considered as customizable implementation of the
	 * similarity method. By default it is implemented as the Jaccard's similarity
	 * index. It's expected to be also defined as others similarity indexes. Also it
	 * might be defined as similarity metric that is based on semantic similarity
	 * between the concepts represented in the sets of strings (words) passed as
	 * arguments.
	 */
	protected Similarity similarityImpl = (a, b) -> {
		// Models should be considered as similar if they both doesn't
		// contain common resources of a certain domain.
		if (Similarity.union(a, b).isEmpty()) {
			return 1.0;
		}

		double aSize = a.size();
		double bSize = b.size();

		double cSize = Similarity.intersection(a, b).size();

		// Jaccard coefficient.
		return cSize / (aSize + bSize - cSize);
	};

	public BPModelsSimilarity() {
		initialize();
	}

	public BPModelsSimilarity(IProcessDAO processDAO, IModelDAO modelDAO) {
		this.processDAO = processDAO;
		this.modelDAO = modelDAO;

		initialize();
	}

	private void initialize() {
		double similarityLevel = AppProperties.INSTANCE.getSimilarityLevel();
		Map<String, Double> domainCoefficients = AppProperties.INSTANCE.getDomainCoefficients();
		Map<String, Double> similarityCoefficients = AppProperties.INSTANCE.getSimilarityCoefficients();

		this.domainCoefficients = new HashMap<String, Double>();
		this.similarityCoefficients = new HashMap<String, Double>();

		setSimilarityLevel(similarityLevel);

		for (Map.Entry<String, Double> entry : domainCoefficients.entrySet()) {
			setDomainCoefficient(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<String, Double> entry : similarityCoefficients.entrySet()) {
			setSimilarityCoefficient(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Sets a value of a similarity level.
	 * 
	 * @param similarityLevel
	 *            - a value of a similarity level.
	 */
	public void setSimilarityLevel(double similarityLevel) {
		if (similarityLevel < 0 || similarityLevel > 1) {
			throw new IllegalArgumentException("Similarity level value is invalid!");
		}

		this.similarityLevel = similarityLevel;
	}

	/**
	 * Sets a value of a certain domain coefficient.
	 * 
	 * @param coefficient
	 *            - a domain coefficient name, either {@link #UNITS_COEFF},
	 *            {@link #SYSTEMS_COEFF}, {@link #FLOW_OBJECTS_COEFF},
	 *            {@link #INPUTS_COEFF}, {@link #OUTPUTS_COEFF} or
	 *            {@link #KPIS_COEFF};
	 * @param value
	 *            - a domain coefficient value.
	 */
	public void setDomainCoefficient(String coefficient, double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException("Domain coefficient value is invalid!");
		}

		domainCoefficients.put(coefficient, value);
	}

	/**
	 * Sets a value of a certain similarity coefficient.
	 * 
	 * @param coefficient
	 *            - a similarity coefficient name, either {@link #SEMANTIC_COEFF} or
	 *            {@link #STRUCTURE_COEFF};
	 * @param value
	 *            - a similarity coefficient value.
	 */
	public void setSimilarityCoefficient(String coefficient, double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException("Similarity coefficient value is invalid!");
		}

		similarityCoefficients.put(coefficient, value);
	}

	/**
	 * Returns tree of similar models grouped by processes.
	 * 
	 * @param modelFile
	 *            - a file of model for which similar ones are retrieved.
	 * @return a tree of similar models grouped by processes.
	 */
	public DefaultMutableTreeNode getSimilarModels(String modelFile) {
		if (modelFile == null || modelFile.isEmpty()) {
			throw new IllegalArgumentException("Model file name is invalid!");
		}

		if (calculateCoefficientsSum(domainCoefficients) != 1) {
			throw new IllegalStateException("Domain coefficients sum should be 1.00!");
		}

		if (calculateCoefficientsSum(similarityCoefficients) != 1) {
			throw new IllegalStateException("Similarity coefficients sum should be 1.00!");
		}

		List<Process> processes = processDAO.retrieve();
		List<Model> models = modelDAO.retrieve();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(RETRIEVE_PROCESSES);

		for (Process process : processes) {
			int similarModels = 0;

			DefaultMutableTreeNode processNode = new DefaultMutableTreeNode(process);

			for (Model model : models) {
				if (model.getProcess().equals(process.getId())) {
					String comparedModelFile = model.getFile();

					if (modelFile.equals(comparedModelFile)) {
						continue;
					}

					if (isModelSimilar(modelFile, model)) {
						processNode.add(new DefaultMutableTreeNode(model));
						similarModels++;
					}
				}
			}

			if (similarModels > 0) {
				root.add(processNode);
			}
		}

		return root;
	}

	/**
	 * Returns similarity of two RDF graphs, represented business process models.
	 * 
	 * @param first
	 *            - first RDF graph;
	 * @param second
	 *            - second RDF graph.
	 * @return - similarity of two RDF graphs.
	 */
	public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
		return compareRDFGraphs(first, second);
	}

	private boolean isModelSimilar(String modelFile, Model comparedModel) {
		org.apache.jena.rdf.model.Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.read(TRIPLESTORE_PATH + "/" + modelFile, RDF_FORMAT);

		org.apache.jena.rdf.model.Model rdfComparedModel = ModelFactory.createDefaultModel();
		rdfComparedModel.read(TRIPLESTORE_PATH + "/" + comparedModel.getFile(), RDF_FORMAT);

		BPModelRDFGraph modelRdfGraph = new BPModelRDFGraph(modelFile, rdfModel);
		BPModelRDFGraph comparedModelRdfGraph = new BPModelRDFGraph(comparedModel.getFile(), rdfComparedModel);

		double similarity = compareRDFGraphs(modelRdfGraph, comparedModelRdfGraph);

		comparedModel.setFile(String.format("%s - %.2f%s", comparedModel.getFile(), (similarity * 100), "%"));

		return similarity >= similarityLevel ? true : false;
	}

	private double compareRDFGraphs(BPModelRDFGraph modelRdfGraph, BPModelRDFGraph comparedModelRdfGraph) {
		double similarity = 0;

		similarity += organizationalUnitsSimilarity(modelRdfGraph, comparedModelRdfGraph)
				* domainCoefficients.get(UNITS_COEFF);
		similarity += supporingSystemsSimilarity(modelRdfGraph, comparedModelRdfGraph)
				* domainCoefficients.get(SYSTEMS_COEFF);
		similarity += flowObjectsSimilarity(modelRdfGraph, comparedModelRdfGraph)
				* domainCoefficients.get(FLOW_OBJECTS_COEFF);
		similarity += inputsSimilarity(modelRdfGraph, comparedModelRdfGraph) * domainCoefficients.get(INPUTS_COEFF);
		similarity += outputsSimilarity(modelRdfGraph, comparedModelRdfGraph) * domainCoefficients.get(OUTPUTS_COEFF);
		similarity += kpisSimilarity(modelRdfGraph, comparedModelRdfGraph) * domainCoefficients.get(KPIS_COEFF);

		if (isLoggingOn) {
			// Logging similarity of business process models.
			System.out.printf("Similarity: %s, %s, %.2f\n", modelRdfGraph.getName(), comparedModelRdfGraph.getName(),
					similarity);
		}

		return similarity;
	}

	private double organizationalUnitsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = firstModel.extractOrganizationalUnits();
		Set<String> second = secondModel.extractOrganizationalUnits();

		double semanticSimilarity = Similarity.similarity(first, second, similarityImpl);

		Set<String> firstExecutes = new HashSet<String>();
		Set<String> secondExecutes = new HashSet<String>();

		Set<String> intersection = Similarity.intersection(first, second);

		for (String resource : intersection) {
			firstExecutes.addAll(firstModel.executes(resource));
		}

		for (String resource : intersection) {
			secondExecutes.addAll(secondModel.executes(resource));
		}

		double structureSimilarity = Similarity.similarity(firstExecutes, secondExecutes, similarityImpl);

		System.out.printf("\tOrganizational Units:\t%.4f %.4f\n", semanticSimilarity, structureSimilarity);

		return similarityCoefficients.get(SEMANTIC_COEFF) * semanticSimilarity
				+ similarityCoefficients.get(STRUCTURE_COEFF) * structureSimilarity;
	}

	private double supporingSystemsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = firstModel.extractSupportingSystems();
		Set<String> second = secondModel.extractSupportingSystems();

		double semanticSimilarity = Similarity.similarity(first, second, similarityImpl);

		Set<String> firstUsedBy = new HashSet<String>();
		Set<String> secondUsedBy = new HashSet<String>();

		Set<String> intersection = Similarity.intersection(first, second);

		for (String resource : intersection) {
			firstUsedBy.addAll(firstModel.usedBy(resource));
		}

		for (String resource : intersection) {
			secondUsedBy.addAll(secondModel.usedBy(resource));
		}

		double structureSimilarity = Similarity.similarity(firstUsedBy, secondUsedBy, similarityImpl);

		System.out.printf("\tSupporting Systems:\t%.4f %.4f\n", semanticSimilarity, structureSimilarity);

		return similarityCoefficients.get(SEMANTIC_COEFF) * semanticSimilarity
				+ similarityCoefficients.get(STRUCTURE_COEFF) * structureSimilarity;
	}

	private double flowObjectsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		// Don't use BPMN-specific start/end events and gateways to measure label
		// similarity.
		Set<String> first = firstModel.extractFlowObjects();
		Set<String> second = secondModel.extractFlowObjects();

		double semanticSimilarity = Similarity.similarity(first, second, similarityImpl);

		first = firstModel.extractFlowObjects();
		second = secondModel.extractFlowObjects();

		Set<String> firstTriggers = new HashSet<String>();
		Set<String> secondTriggers = new HashSet<String>();

		Set<String> intersection = Similarity.intersection(first, second);

		for (String resource : intersection) {
			firstTriggers.addAll(firstModel.triggers(resource));
		}

		for (String resource : intersection) {
			secondTriggers.addAll(secondModel.triggers(resource));
		}

		double structureSimilarity = Similarity.similarity(firstTriggers, secondTriggers, similarityImpl);

		System.out.printf("\tProcess Flow Objects:\t%.4f %.4f\n", semanticSimilarity, structureSimilarity);

		return similarityCoefficients.get(SEMANTIC_COEFF) * semanticSimilarity
				+ similarityCoefficients.get(STRUCTURE_COEFF) * structureSimilarity;
	}

	private double inputsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = firstModel.extractBusinessObjects();
		Set<String> second = secondModel.extractBusinessObjects();

		double semanticSimilarity = Similarity.similarity(first, second, similarityImpl);

		Set<String> firstIsInputFor = new HashSet<String>();
		Set<String> secondIsInputFor = new HashSet<String>();

		Set<String> intersection = Similarity.intersection(first, second);

		for (String resource : intersection) {
			firstIsInputFor.addAll(firstModel.isInputFor(resource));
		}

		for (String resource : intersection) {
			secondIsInputFor.addAll(secondModel.isInputFor(resource));
		}

		double structureSimilarity = Similarity.similarity(firstIsInputFor, secondIsInputFor, similarityImpl);

		return similarityCoefficients.get(SEMANTIC_COEFF) * semanticSimilarity
				+ similarityCoefficients.get(STRUCTURE_COEFF) * structureSimilarity;
	}

	private double outputsSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = firstModel.extractBusinessObjects();
		Set<String> second = secondModel.extractBusinessObjects();

		double semanticSimilarity = Similarity.similarity(first, second, similarityImpl);

		Set<String> firstIsOutputOf = new HashSet<String>();
		Set<String> secondIsOutputOf = new HashSet<String>();

		Set<String> intersection = Similarity.intersection(first, second);

		for (String resource : intersection) {
			firstIsOutputOf.addAll(firstModel.isOutputOf(resource));
		}

		for (String resource : intersection) {
			secondIsOutputOf.addAll(secondModel.isOutputOf(resource));
		}

		double structureSimilarity = Similarity.similarity(firstIsOutputOf, secondIsOutputOf, similarityImpl);

		return similarityCoefficients.get(SEMANTIC_COEFF) * semanticSimilarity
				+ similarityCoefficients.get(STRUCTURE_COEFF) * structureSimilarity;
	}

	private double kpisSimilarity(BPModelRDFGraph firstModel, BPModelRDFGraph secondModel) {
		Set<String> first = firstModel.extractKPIs();
		Set<String> second = secondModel.extractKPIs();

		double semanticSimilarity = Similarity.similarity(first, second, similarityImpl);

		Set<String> firstMeasures = new HashSet<String>();
		Set<String> secondMeasures = new HashSet<String>();

		Set<String> intersection = Similarity.intersection(first, second);

		for (String resource : intersection) {
			firstMeasures.addAll(firstModel.measures(resource));
		}

		for (String resource : intersection) {
			secondMeasures.addAll(secondModel.measures(resource));
		}

		double structureSimilarity = Similarity.similarity(firstMeasures, secondMeasures, similarityImpl);

		return similarityCoefficients.get(SEMANTIC_COEFF) * semanticSimilarity
				+ similarityCoefficients.get(STRUCTURE_COEFF) * structureSimilarity;
	}

	private double calculateCoefficientsSum(Map<String, Double> coefficientsMap) {
		double result = 0;

		for (Map.Entry<String, Double> entry : coefficientsMap.entrySet()) {
			result += entry.getValue();
		}

		return result;
	}

	public double getSimilarityLevel() {
		return similarityLevel;
	}

	public Map<String, Double> getDomainCoefficients() {
		return domainCoefficients;
	}

	public Map<String, Double> getSimilarityCoefficients() {
		return similarityCoefficients;
	}

	public static void setLoggingOn() {
		isLoggingOn = true;
	}

	public static void setLoggingOff() {
		isLoggingOn = false;
	}
}
