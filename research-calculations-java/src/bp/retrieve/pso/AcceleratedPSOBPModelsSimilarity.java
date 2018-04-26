package bp.retrieve.pso;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.CustomizableBPModelsSimilarity;

/**
 * This extension is based on Accelerated Particle Swarm Optimization (APSO)
 * algorithm that use artificial swarm intelligence to define business process
 * model similarity coeffs (both domain and closeness).
 * 
 * @author Andrii Kopp
 */
public class AcceleratedPSOBPModelsSimilarity extends CustomizableBPModelsSimilarity {
	// Default swarm size.
	private static final int SWARM_SIZE = 20;

	/**
	 * Returns similarity of two RDF graphs that represent business process models.
	 * This extended version uses Accelerated Particle Swarm Optimization (APSO)
	 * algorithm to define similarity (domain and closeness) coefficients based on
	 * artificial swarm intelligence.
	 * 
	 * @param first
	 *            - first RDF graph;
	 * @param second
	 *            - second RDF graph.
	 * @return - similarity of two RDF graphs.
	 */
	@Override
	public double compareBPModelRDFGraphs(BPModelRDFGraph first, BPModelRDFGraph second) {
		BPModelsSimilarityPSOptimization pso = new BPModelsSimilarityPSOptimization(SWARM_SIZE);

		// Use current similarity index.
		SimilarityUtil.setSimilarity(similarityImpl);

		// Calculate and initialize similarities.
		pso.setOrganizationalUnitsSimilarity(SimilarityUtil.getOrganizationalUnitsSimilarity(first, second));
		pso.setSupportingSystemSimilarity(SimilarityUtil.getSupportingSystemSimilarity(first, second));
		pso.setProcessFlowObjectsSimilarity(SimilarityUtil.getProcessFlowObjectsSimilarity(first, second));
		pso.setExecutesSimilarity(SimilarityUtil.getExecutesSimilarity(first, second));
		pso.setUsedBySimilarity(SimilarityUtil.getUsedBySimilarity(first, second));
		pso.setTriggersSimilarity(SimilarityUtil.getTriggersSimilarity(first, second));

		double similarity = pso.similarity();

		System.out.printf("LOG: %s <> %s : %.4f\n", first.getName(), second.getName(), similarity);

		// Show similarity coefficient values.
		for (double coeff : pso.getSimilarityCoeffs())
			System.out.printf("\t%.4f\n", coeff);

		return similarity;
	}
}
