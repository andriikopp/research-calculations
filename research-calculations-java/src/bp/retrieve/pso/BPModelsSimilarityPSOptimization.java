package bp.retrieve.pso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of Accelerated Particle Swarm Optimization (APSO) used to
 * define optimal values of the business process models similarity coeffs (alpha
 * [1-3] and beta [label, structure]).
 * 
 * @author Andrii Kopp
 */
public class BPModelsSimilarityPSOptimization {
	private int dimension = 5;
	private int similaritiesCount = 6;

	// Allowable range of values.
	private static final double LOWER_BOUND = 0;
	private static final double UPPER_BOUND = 1;

	// APSO constant velocity scoefficients.
	private static final double ALPHA = 0.2;
	private static final double BETA = 0.4;

	private static final int MAX_ITERATIONS = 1000;

	// Similarity values of business process models by various aspects.
	private double[] similarities;

	private int swarmSize;
	private List<double[]> swarm;
	private double[] global;

	/**
	 * The number of particles within the swarm must be set.
	 * 
	 * @param swarmSize
	 *            - the number of particles within the swarm.
	 */
	public BPModelsSimilarityPSOptimization(int swarmSize) {
		this.similarities = new double[similaritiesCount];
		this.swarmSize = swarmSize;
		this.swarm = new ArrayList<double[]>();
	}

	/**
	 * Returns the similarity value of two business process models. This approach
	 * uses optimal similarity coeffs obtained by application of the Accelerated
	 * Particle Swarm Optimization (APSO) algorithm.
	 * 
	 * @return the similarity value of two business process models.
	 */
	public double similarity() {
		// Preparation stage.
		for (int i = 0; i < swarmSize; i++) {
			double[] particle = new double[dimension];

			// Define initial particle values.
			for (int j = 0; j < dimension; j++)
				particle[j] = rand(LOWER_BOUND, UPPER_BOUND);

			norm(particle); // Normalize obtained values.
			global = particle;
			swarm.add(particle);
		}

		for (int counter = 1; counter <= MAX_ITERATIONS; counter++)
			for (int i = 0; i < swarmSize; i++) {
				double[] particle = new double[dimension];

				// Move particle to the next position.
				for (int j = 0; j < dimension; j++)
					particle[j] = (1 - BETA) * swarm.get(i)[j] + BETA * global[j]
							+ ALPHA * rand(LOWER_BOUND, UPPER_BOUND);

				norm(particle); // Normalize position coordinates.

				// Use '>' to maximize or '<' to minimize similarity.
				if (measure(particle) > measure(global))
					global = particle; // Update global optimal value.

				swarm.set(i, particle);
			}

		return measure(global);
	}

	// Performs normalization of alpha (domain closeness) and beta (label/similarity
	// closeness.
	private void norm(double[] coeff) {
		double aSum = coeff[0] + coeff[1] + coeff[2];
		double bSum = coeff[3] + coeff[4];

		for (int i = 0; i < dimension - 2; i++)
			coeff[i] /= aSum;
		for (int i = dimension - 2; i < dimension; i++)
			coeff[i] /= bSum;
	}

	// Generates random values between min and max values.
	private double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	/**
	 * Similarity measure of business process models.
	 * 
	 * @param coeff
	 *            - the array of similarity coefficients.
	 * @return the value of similarity between two business process models.
	 */
	public double measure(double[] coeff) {
		return coeff[0] * (coeff[3] * similarities[0] + coeff[4] * similarities[3])
				+ coeff[1] * (coeff[3] * similarities[1] + coeff[4] * similarities[4])
				+ coeff[2] * (coeff[3] * similarities[2] + coeff[4] * similarities[5]);
	}

	/**
	 * Set the dimension of the optimization problem. Default is 5.
	 * 
	 * @param dimension
	 *            - the dimension of the optimization problem.
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/**
	 * Set the number of similarities used to measure similarity of business process
	 * models.
	 * 
	 * @param similaritiesCount
	 *            - the number of similarities.
	 */
	public void setSimilaritiesCount(int similaritiesCount) {
		this.similaritiesCount = similaritiesCount;
	}

	/**
	 * Initialize a similarity of organizational unit labels.
	 * 
	 * @param value
	 *            - a similarity of organizational unit labels.
	 */
	public void setOrganizationalUnitsSimilarity(double value) {
		similarities[0] = value;
	}

	/**
	 * Initialize a similarity of supporting system labels.
	 * 
	 * @param value
	 *            - a similarity of supporting system labels.
	 */
	public void setSupportingSystemSimilarity(double value) {
		similarities[1] = value;
	}

	/**
	 * Initialize a similarity of process flow object labels.
	 * 
	 * @param value
	 *            - a similarity of process flow object labels.
	 */
	public void setProcessFlowObjectsSimilarity(double value) {
		similarities[2] = value;
	}

	/**
	 * Initialize a similarity of 'executes' relations.
	 * 
	 * @param value
	 *            - a similarity of execution relations.
	 */
	public void setExecutesSimilarity(double value) {
		similarities[3] = value;
	}

	/**
	 * Initialize a similarity of 'used by' relations.
	 * 
	 * @param value
	 *            - a similarity of 'used by' relations.
	 */
	public void setUsedBySimilarity(double value) {
		similarities[4] = value;
	}

	/**
	 * Initialize a similarity of 'triggers' relations.
	 * 
	 * @param value
	 *            - a similarity of 'triggers' relations.
	 */
	public void setTriggersSimilarity(double value) {
		similarities[5] = value;
	}

	/**
	 * Returns all distance values.
	 * 
	 * @return all distance values.
	 */
	public double[] getSimilarities() {
		return similarities;
	}

	/**
	 * Returns obtained similarity coeffs.
	 * 
	 * @return obtained similarity coeffs.
	 */
	public double[] getSimilarityCoeffs() {
		return global;
	}

	// Simple example.
	public static void main(String[] args) {
		final int MAX_SWARM_SIZE = 20; // Max. size of the swarm.

		for (int swarmSize = 1; swarmSize <= MAX_SWARM_SIZE; swarmSize++) {
			BPModelsSimilarityPSOptimization pso = new BPModelsSimilarityPSOptimization(swarmSize);

			// Initialize business process model similarity coefficients.
			pso.setOrganizationalUnitsSimilarity(1);
			pso.setSupportingSystemSimilarity(1);
			pso.setProcessFlowObjectsSimilarity(0.5);
			pso.setExecutesSimilarity(0.6);
			pso.setUsedBySimilarity(0.67);
			pso.setTriggersSimilarity(0.38);

			System.out.printf("%d : %.4f\n", swarmSize, pso.similarity());

			for (double coeff : pso.getSimilarityCoeffs())
				System.out.printf("\t%.4f\n", coeff);
		}
	}
}
