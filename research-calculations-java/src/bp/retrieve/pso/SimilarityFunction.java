package bp.retrieve.pso;

public interface SimilarityFunction {

	double measure(double[] weights, double[] similarities);
}
