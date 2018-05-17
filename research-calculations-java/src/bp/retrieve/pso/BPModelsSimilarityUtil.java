package bp.retrieve.pso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import main.resources.repository.BPModelsCollection;

public class BPModelsSimilarityUtil {
	private static final double PREFERENCE_LEVEL = 0.5;
	private static final double GOLDEN_SECTION_COEFF = 0.618;

	private static final int SWARM_SIZE = 20;

	private static final double LOWER_BOUND = 0;
	private static final double UPPER_BOUND = 1;

	private static final int MAX_ITERATIONS = 1000;

	private static final double ALPHA = 0.2;
	private static final double BETA = 0.4;

	public static SimilarityFunction similarity = (weights, similarities) -> {
		double measure = 0;

		for (int i = 0; i < weights.length; i++)
			measure += weights[i] * similarities[i];

		return measure;
	};

	private interface FishbernFunction {
		double function(double index, double n);
	}

	public static double[] extractSimilarities(BPModelRDFGraph first, BPModelRDFGraph second) {
		SimilarityUtil.setSimilarity(SemanticSimilarityUtil.jaccardSimilarity);

		List<Double> consideredComponents = new ArrayList<>();

		final int consideredDomains = 3;
		boolean[] considerStructure = new boolean[consideredDomains];

		if (!first.extractOrganizationalUnits().isEmpty() && !second.extractOrganizationalUnits().isEmpty()) {
			consideredComponents.add(SimilarityUtil.getOrganizationalUnitsSimilarity(first, second));
			considerStructure[0] = true;
		}

		if (!first.extractSupportingSystems().isEmpty() && !second.extractSupportingSystems().isEmpty()) {
			consideredComponents.add(SimilarityUtil.getSupportingSystemSimilarity(first, second));
			considerStructure[1] = true;
		}

		if (!first.extractFlowObjects().isEmpty() && !second.extractFlowObjects().isEmpty()) {
			consideredComponents.add(SimilarityUtil.getProcessFlowObjectsSimilarity(first, second));
			considerStructure[2] = true;
		}

		if (considerStructure[0])
			consideredComponents.add(SimilarityUtil.getExecutesSimilarity(first, second));

		if (considerStructure[1])
			consideredComponents.add(SimilarityUtil.getUsedBySimilarity(first, second));

		if (considerStructure[2])
			consideredComponents.add(SimilarityUtil.getTriggersSimilarity(first, second));

		double[] similarities = new double[consideredComponents.size()];

		for (int i = 0; i < consideredComponents.size(); i++)
			similarities[i] = consideredComponents.get(i);

		return similarities;
	}

	public static double directEstimation(double[] similarities, SimilarityFunction similarity) {
		double[] weights = new double[similarities.length];

		for (int i = 0; i < similarities.length; i++)
			weights[i] = harrington(similarities[i]);

		norm(weights);

		return similarity.measure(weights, similarities);
	}

	public static double ranking(double[] similarities, SimilarityFunction similarity) {
		double[] weights = new double[similarities.length];

		List<Double> orderedSimilarities = new ArrayList<>();

		for (int i = 0; i < similarities.length; i++)
			if (!orderedSimilarities.contains(similarities[i]))
				orderedSimilarities.add(similarities[i]);

		orderedSimilarities.sort((a, b) -> {
			return Double.compare(b, a);
		});

		double rank = orderedSimilarities.size();

		for (double value : orderedSimilarities) {
			for (int i = 0; i < similarities.length; i++)
				if (Double.compare(value, similarities[i]) == 0)
					weights[i] = rank;
			rank--;
		}

		norm(weights);

		return similarity.measure(weights, similarities);
	}

	public static double fishbernEstimation(double[] similarities, SimilarityFunction similarity,
			FishbernFunction function) {
		double[] weights = new double[similarities.length];

		List<Double> orderedSimilarities = new ArrayList<>();

		for (int i = 0; i < similarities.length; i++)
			if (!orderedSimilarities.contains(similarities[i]))
				orderedSimilarities.add(similarities[i]);

		orderedSimilarities.sort((a, b) -> {
			return Double.compare(b, a);
		});

		double n = orderedSimilarities.size();
		double index = 1;

		for (double value : orderedSimilarities) {
			for (int i = 0; i < similarities.length; i++)
				if (Double.compare(value, similarities[i]) == 0)
					weights[i] = function.function(index, n);
			index++;
		}

		norm(weights);

		return similarity.measure(weights, similarities);
	}

	public static double pairwiseComparison(double[] similarities, SimilarityFunction similarity) {
		double[] weights = new double[similarities.length];

		for (int i = 0; i < weights.length; i++)
			weights[i] = 1.0;

		for (int i = 0; i < similarities.length; i++)
			for (int j = 0; j < similarities.length; j++)
				if (similarities[i] > similarities[j])
					weights[i] *= 1.0 + PREFERENCE_LEVEL;
				else if (similarities[i] < similarities[j])
					weights[i] *= 1.0 - PREFERENCE_LEVEL;
				else
					weights[i] *= 1.0;

		for (int i = 0; i < weights.length; i++)
			weights[i] = Math.pow(weights[i], 1.0 / (double) weights.length);

		norm(weights);

		return similarity.measure(weights, similarities);
	}

	public static double acceleratedPSO(double[] similarities, SimilarityFunction similarity) {
		List<double[]> swarm = new ArrayList<>();
		double[] global = null;

		for (int i = 0; i < SWARM_SIZE; i++) {
			double[] particle = new double[similarities.length];

			for (int j = 0; j < similarities.length; j++)
				particle[j] = rand(LOWER_BOUND, UPPER_BOUND);

			norm(particle);

			if (global == null)
				global = particle;
			else if (similarity.measure(particle, similarities) > similarity.measure(global, similarities))
				global = particle;

			swarm.add(particle);
		}

		for (int counter = 1; counter <= MAX_ITERATIONS; counter++)
			for (int i = 0; i < SWARM_SIZE; i++) {
				double[] particle = new double[similarities.length];

				for (int j = 0; j < similarities.length; j++)
					particle[j] = (1 - BETA) * swarm.get(i)[j] + BETA * global[j]
							+ ALPHA * rand(LOWER_BOUND, UPPER_BOUND);

				norm(particle);

				if (similarity.measure(particle, similarities) > similarity.measure(global, similarities))
					global = particle;

				swarm.set(i, particle);
			}

		return similarity.measure(global, similarities);
	}

	private static double fishbernFirstEquation(double index, double n) {
		return 2.0 * (n - index + 1.0) / (n * (n + 1.0));
	}

	private static double fishbernSecondEquation(double index, double n) {
		return Math.pow(GOLDEN_SECTION_COEFF, index + 1.0) / (1.0 - Math.pow(GOLDEN_SECTION_COEFF, n));
	}
	
	private static double harrington(double value) {
		if (value > 0.8) return 9;
		if (value > 0.63) return 7;
		if (value > 0.37) return 5;
		if (value > 0.2) return 3;
		return 1;
	}

	private static void norm(double[] array) {
		double sum = 0;

		for (double value : array)
			sum += value;

		for (int i = 0; i < array.length; i++)
			array[i] /= sum;
	}

	private static double rand(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	@Test
	public void test() {
		for (GenericProcessModel first : BPModelsCollection.models)
			for (GenericProcessModel second : BPModelsCollection.models) {
				System.out.printf("%s <> %s\t", first.getName(), second.getName());

				BPModelRDFGraph a = SemanticSimilarityUtil.normalizeBPModel(first.getModelDescription());
				BPModelRDFGraph b = SemanticSimilarityUtil.normalizeBPModel(second.getModelDescription());

				double[] similarities = extractSimilarities(a, b);

				System.out.printf("%.2f\t", directEstimation(similarities, similarity));
				System.out.printf("%.2f\t", ranking(similarities, similarity));
				System.out.printf("%.2f\t", fishbernEstimation(similarities, similarity, (index, n) -> {
					return fishbernFirstEquation(index, n);
				}));
				System.out.printf("%.2f\t", fishbernEstimation(similarities, similarity, (index, n) -> {
					return fishbernSecondEquation(index, n);
				}));
				System.out.printf("%.2f\t", pairwiseComparison(similarities, similarity));
				System.out.printf("%.2f\n", acceleratedPSO(similarities, similarity));
			}
	}
}
