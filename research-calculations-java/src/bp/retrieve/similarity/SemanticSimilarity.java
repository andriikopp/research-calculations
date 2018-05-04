package bp.retrieve.similarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Simple semantic similarity measure of concepts. Based on a dictionary of
 * synonyms that might be set manually.
 * 
 * @author Andrii Kopp
 */
public class SemanticSimilarity implements Similarity {
	// Semantic similarity weight coefficients.
	@SuppressWarnings("unused")
	private double concepts;
	@SuppressWarnings("unused")
	private double synonyms;
	@SuppressWarnings("unused")
	private double equal;

	private Map<String, Set<String>> synonymsMap;

	public SemanticSimilarity() {
		synonymsMap = new HashMap<String, Set<String>>();
	}

	@Deprecated
	public SemanticSimilarity(double concepts, double synonyms, double equal) {
		synonymsMap = new HashMap<String, Set<String>>();

		this.concepts = concepts;
		this.synonyms = synonyms;
		this.equal = equal;
	}

	/**
	 * Set the concept and one or several synonyms of this concept.
	 * 
	 * @param label
	 *            - the concept;
	 * @param synonyms
	 *            - one or several synonyms of this concept.
	 */
	@Deprecated
	public void addSynonyms(String label, String... synonyms) {
		Set<String> synonymsSet = new HashSet<String>();
		synonymsSet.addAll(Arrays.asList(synonyms));
		synonymsMap.put(label, synonymsSet);
	}

	@Override
	public double coefficient(Set<String> first, Set<String> second) {
		// Use Jaccard distance.
		return jaccardDistance(first, second);
	}

	/**
	 * Jaccard distance between two sets. Highest value means highest similarity.
	 * 
	 * @param first
	 *            - the first set;
	 * @param second
	 *            - the second set.
	 * @return the value of similarity between two sets.
	 */
	public static double jaccardDistance(Set<String> first, Set<String> second) {
		// Models should be considered as similar if they both doesn't
		// contain common resources of a certain domain.
		if (Similarity.union(first, second).isEmpty())
			return 1.0;

		double aSize = first.size();
		double bSize = second.size();

		double cSize = Similarity.intersection(first, second).size();

		// Jaccard coefficient.
		return cSize / (aSize + bSize - cSize);
	}

	// Return 'true' if 'a' and 'b' have common concepts. E.g. 'Verify invoice' and
	// 'Verification invoice' have common concept 'invoice'. Thus 'a' and 'b' are
	// close. Otherwise return 'false'.
	@SuppressWarnings("unused")
	private boolean areHaveCommonConcepts(String a, String b) {
		Set<String> aConcepts = new HashSet<String>();
		aConcepts.addAll(Arrays.asList(a.split("\\s+")));

		Set<String> bConcepts = new HashSet<String>();
		bConcepts.addAll(Arrays.asList(b.split("\\s+")));

		return !Similarity.intersection(aConcepts, bConcepts).isEmpty();
	}

	// Return 'true' if 'a' and 'b' are synonyms. E.g. 'Verify invoice' and
	// 'Verification invoice' are synonyms. Thus 'a' and 'b' are close. Otherwise
	// return 'false'.
	@SuppressWarnings("unused")
	private boolean areSynonyms(String a, String b) {
		if (!synonymsMap.containsKey(a) && !synonymsMap.containsKey(b)) {
			return false;
		}

		Set<String> aSynonyms = synonymsMap.get(a);
		Set<String> bSynonyms = synonymsMap.get(b);

		boolean aContainsB = false;

		if (aSynonyms != null) {
			aContainsB = aSynonyms.contains(b);

			if (aContainsB)
				return true;
		}

		if (bSynonyms != null) {
			return bSynonyms.contains(a);
		}

		return false;
	}
}
