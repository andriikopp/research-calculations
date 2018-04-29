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
	private double concepts;
	private double synonyms;
	private double equal;

	private Map<String, Set<String>> synonymsMap;

	public SemanticSimilarity() {
		synonymsMap = new HashMap<String, Set<String>>();
	}

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
				double weight = 0;

				// Check for common concepts.
				if (areHaveCommonConcepts(firstConcept, secondConcept)) {
					weight = concepts;
				}

				// Check for synonyms.
				if (areSynonyms(firstConcept, secondConcept)) {
					weight = synonyms;
				}

				// Check for total equality.
				if (firstConcept.equals(secondConcept)) {
					weight = equal;
				}

				sum += weight;
			}
		}

		// Sorencen-Dice index.
		return 2.0 * sum / (double) (first.size() + second.size());

	}

	// Return 'true' if 'a' and 'b' have common concepts. E.g. 'Verify invoice' and
	// 'Verification invoice' have common concept 'invoice'. Thus 'a' and 'b' are
	// close. Otherwise return 'false'.
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
