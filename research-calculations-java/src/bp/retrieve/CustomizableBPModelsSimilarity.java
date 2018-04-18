package bp.retrieve;

import bp.retrieve.similarity.Similarity;

/**
 * This extension allow users to customize similarity method and probably other
 * features of the superclass in future.
 * 
 * @author Andrii Kopp
 */
public class CustomizableBPModelsSimilarity extends BPModelsSimilarity {

	/**
	 * Allows to define specific implementation of the similarity measure between
	 * two sets of strings passed there as arguments.
	 * 
	 * @param similarity
	 */
	public void defineSimilarityMethod(Similarity similarity) {
		similarityImpl = similarity;
	}
}
