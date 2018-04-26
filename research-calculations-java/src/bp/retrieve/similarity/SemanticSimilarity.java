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
	private Map<String, Set<String>> synonymsMap;

	public SemanticSimilarity() {
		synonymsMap = new HashMap<String, Set<String>>();
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
				if (firstConcept.equals(secondConcept)) {
					sum += 1.0;
				} else {
					if (areSynonyms(firstConcept, secondConcept)) {
						sum += 1.0;
					}
				}
			}
		}

		// Sorencen-Dice index.
		return 2.0 * sum / (double) (first.size() + second.size());
	}

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
