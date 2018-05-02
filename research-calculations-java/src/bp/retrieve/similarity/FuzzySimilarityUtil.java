package bp.retrieve.similarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.similarity.JaroWinklerDistance;

public class FuzzySimilarityUtil {
	private static JaroWinklerDistance distance = new JaroWinklerDistance();

	/**
	 * Returns the fuzzy set based on the first compared set.
	 * 
	 * @param firstCompared
	 *            - the first compared set;
	 * @param secondCompared
	 *            - the second compared set.
	 * @return the fuzzy set based on the first compared set.
	 */
	public static Map<String, Double> toFuzzySet(Set<String> firstCompared, Set<String> secondCompared) {
		Map<String, Double> fuzzySet = new HashMap<String, Double>();

		for (String value : firstCompared) {
			fuzzySet.put(value, 1.0);
		}

		for (String secondValue : secondCompared) {
			double membership = 0;

			for (String firstValue : firstCompared) {
				double abValuesSimilarity = distance.apply(firstValue, secondValue);

				if (abValuesSimilarity > membership)
					membership = abValuesSimilarity;
			}

			fuzzySet.put(secondValue, membership);
		}

		return fuzzySet;
	}

	/**
	 * Fuzzy intersection of two sets.
	 * 
	 * @param first
	 *            - the first set;
	 * @param second
	 *            - the second set.
	 * @return the fuzzy intersection of two sets.
	 */
	public static Map<String, Double> fuzzyIntersection(Set<String> first, Set<String> second) {
		Map<String, Double> firstFuzzy = toFuzzySet(first, second);
		Map<String, Double> secondFuzzy = toFuzzySet(second, first);

		Map<String, Double> result = new HashMap<String, Double>();

		for (String concept : firstFuzzy.keySet()) {
			double membership = Math.min(firstFuzzy.get(concept), secondFuzzy.get(concept));

			result.put(concept, membership);
		}

		return result;
	}

	/**
	 * Fuzzy union of two sets.
	 * 
	 * @param first
	 *            - the first set;
	 * @param second
	 *            - the second set.
	 * @return the fuzzy union of two sets.
	 */
	public static Map<String, Double> fuzzyUnion(Set<String> first, Set<String> second) {
		Map<String, Double> firstFuzzy = toFuzzySet(first, second);
		Map<String, Double> secondFuzzy = toFuzzySet(second, first);

		Map<String, Double> result = new HashMap<String, Double>();

		for (String concept : firstFuzzy.keySet()) {
			double membership = Math.max(firstFuzzy.get(concept), secondFuzzy.get(concept));

			result.put(concept, membership);
		}

		return result;
	}

	/**
	 * Fuzzy Jaccard distance of two sets.
	 * 
	 * @param first
	 *            - the first set;
	 * @param second
	 *            - the second set.
	 * @return the fuzzy Jaccard distance of two sets.
	 */
	public static double fuzzyJaccardDistance(Set<String> first, Set<String> second) {
		// If both sets are empty consider them as totally similar.
		if (first.isEmpty() && second.isEmpty())
			return 1.0;

		return sumOfMembershipFunctions(fuzzyIntersection(first, second))
				/ sumOfMembershipFunctions(fuzzyUnion(first, second));
	}

	// Returns sum of memberships of the fuzzy set.
	private static double sumOfMembershipFunctions(Map<String, Double> fuzzySet) {
		double result = 0;

		for (Double membership : fuzzySet.values())
			result += membership;

		return result;
	}

	// Sample calculations.
	public static void main(String[] args) {
		Set<String> a = new HashSet<String>();
		a.addAll(Arrays.asList("Order", "Receive goods", "Verify invoice", "Store goods"));

		Set<String> b = new HashSet<String>();
		b.addAll(Arrays.asList("Order", "Verification invoice"));

		System.out.println(fuzzyIntersection(a, b));
		System.out.println(fuzzyUnion(a, b));
		System.out.println(fuzzyJaccardDistance(a, b));
	}
}
