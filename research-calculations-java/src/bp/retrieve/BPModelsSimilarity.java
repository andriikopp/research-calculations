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

	private IProcessDAO processDAO;
	private IModelDAO modelDAO;

	private double similarityLevel;
	private Map<String, Double> domainCoefficients;
	private Map<String, Double> similarityCoefficients;

	@SuppressWarnings("serial")
	public BPModelsSimilarity(IProcessDAO processDAO, IModelDAO modelDAO) {
		this.processDAO = processDAO;
		this.modelDAO = modelDAO;

		// Default settings.
		similarityLevel = 0.0;
		domainCoefficients = new HashMap<String, Double>() {
			{
				put("units", 0.0);
				put("systems", 0.0);
				put("flow", 0.0);
				put("inputs", 0.0);
				put("output", 0.0);
				put("kpis", 0.0);
			}
		};
		similarityCoefficients = new HashMap<String, Double>() {
			{
				put("semantic", 0.0);
				put("structure", 0.0);
			}
		};
	}

	/**
	 * Returns tree of similar models grouped by processes.
	 * 
	 * @param modelFile
	 *            - a file of model for which similar ones are retrieved.
	 * @return a tree of similar models grouped by processes.
	 */
	public DefaultMutableTreeNode getSimilarModels(String modelFile) {
		List<Process> processes = processDAO.retrieve();
		List<Model> models = modelDAO.retrieve();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(RETRIEVE_PROCESSES);

		for (Process process : processes) {
			int similarModels = 0;

			DefaultMutableTreeNode processNode = new DefaultMutableTreeNode(process);

			for (Model model : models) {
				if (model.getProcess().equals(process.getId())) {
					String comparedModelFile = model.getFile();

					if (isModelSimilar(modelFile, comparedModelFile)) {
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

	private boolean isModelSimilar(String modelFile, String comparedModelFile) {
		// TODO
		return true;
	}

	public double getSimilarityLevel() {
		return similarityLevel;
	}

	public void setSimilarityLevel(double similarityLevel) {
		this.similarityLevel = similarityLevel;
	}

	public Map<String, Double> getDomainCoefficients() {
		return domainCoefficients;
	}

	public void setDomainCoefficients(Map<String, Double> domainCoefficients) {
		this.domainCoefficients = domainCoefficients;
	}

	public Map<String, Double> getSimilarityCoefficients() {
		return similarityCoefficients;
	}

	public void setSimilarityCoefficients(Map<String, Double> similarityCoefficients) {
		this.similarityCoefficients = similarityCoefficients;
	}
}
