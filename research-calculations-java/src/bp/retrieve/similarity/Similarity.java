package bp.retrieve.similarity;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides functional interface for a sets similarity.
 * 
 * @author Andrii Kopp
 *
 */
public interface Similarity {
	/**
	 * Returns a value of similarity between two sets.
	 * 
	 * @param first
	 *            - a fist set;
	 * @param second
	 *            - a second set;
	 * @return a value of similarity between two sets.
	 */
	double coefficient(Set<String> first, Set<String> second);

	/**
	 * Returns an intersection of two sets.
	 * 
	 * @param first
	 *            - a fist set;
	 * @param second
	 *            - a second set;
	 * @return an intersection of two sets.
	 */
	static Set<String> intersection(Set<String> first, Set<String> second) {
		Set<String> result = new HashSet<String>(first);
		result.retainAll(second);
		return result;
	}

	/**
	 * Returns a union of two sets.
	 * 
	 * @param first
	 *            - a fist set;
	 * @param second
	 *            - a second set;
	 * @return a union of two sets.
	 */
	static Set<String> union(Set<String> first, Set<String> second) {
		Set<String> result = new HashSet<String>(first);
		result.addAll(second);
		return result;
	}

	/**
	 * Returns a value of similarity between two sets according to a certain
	 * similarity coefficient.
	 * 
	 * @param first
	 *            - a fist set;
	 * @param second
	 *            - a second set;
	 * @param similarity
	 *            - a certain similarity coefficient implementation.
	 * @return a value of similarity between two sets according to a certain
	 *         similarity coefficient.
	 */
	static double similarity(Set<String> first, Set<String> second, Similarity similarity) {
		return similarity.coefficient(first, second);
	}
}
