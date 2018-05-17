package bp.retrieve.pso;

/**
 * Functional interface of the integrated similarity measure.
 * 
 * @author Andrii Kopp
 */
public interface SimilarityFunction {

	/**
	 * Returns value of integrated similarity measure based on the weights and
	 * similarities vectors.
	 * 
	 * @param weights
	 *            - the vector of similarities weights;
	 * @param similarities
	 *            - the vector of similarities values.
	 * @return - the value of integrated similarity measure.
	 */
	double measure(double[] weights, double[] similarities);
}
