package bp.retrieve.similarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.similarity.JaroWinklerDistance;

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

	// Use Apache Commons Text library (Jaro-Winkler distance measure between
	// strings).
	private JaroWinklerDistance distance;

	// Strings similarity threshold.
	private double similarityThreshold;

	private Map<String, Set<String>> synonymsMap;

	public SemanticSimilarity(double similarityThreshold) {
		synonymsMap = new HashMap<String, Set<String>>();
		distance = new JaroWinklerDistance();

		this.similarityThreshold = similarityThreshold;
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
		if (Similarity.union(first, second).isEmpty()) {
			return 1.0;
		}

		double sum = 0;

		for (String firstConcept : first) {
			for (String secondConcept : second) {
				// Labels similarity.
				if (similarity(firstConcept, secondConcept))
					sum += 1.0;
			}
		}

		// Sorencen-Dice index.
		return 2.0 * sum / (double) (first.size() + second.size());
	}

	// Calculates the similarity between two strings as the number within [0, 1]
	// range.
	private boolean similarity(String a, String b) {
		// Consider that strings are similar if distance is greater or equal to the
		// threshold.
		return distance.apply(a, b) >= similarityThreshold;
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
