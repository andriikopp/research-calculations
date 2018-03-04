package bp.retrieve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import bp.AppProperties;
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

	public static final String UNITS_COEFF = "units";
	public static final String SYSTEMS_COEFF = "systems";
	public static final String FLOW_OBJECTS_COEFF = "flow";
	public static final String INPUTS_COEFF = "inputs";
	public static final String OUTPUTS_COEFF = "outputs";
	public static final String KPIS_COEFF = "kpis";

	public static final String SEMANTIC_COEFF = "semantic";
	public static final String STRUCTURE_COEFF = "structure";

	private IProcessDAO processDAO;
	private IModelDAO modelDAO;

	private double similarityLevel;
	private Map<String, Double> domainCoefficients;
	private Map<String, Double> similarityCoefficients;

	public BPModelsSimilarity(IProcessDAO processDAO, IModelDAO modelDAO) {
		this.processDAO = processDAO;
		this.modelDAO = modelDAO;

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
	 *            - a similarity coefficient name, either
	 *            {@link #SEMANTIC_COEFF} or {@link #STRUCTURE_COEFF};
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

	private boolean isModelSimilar(String modelFile, Model comparedModel) {
		// TODO
		double similarity = 100;

		comparedModel.setFile(String.format("%s - %.2f%s", comparedModel.getFile(), similarity, "%"));

		return true;
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
}
