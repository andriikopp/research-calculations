package bp.retrieve.pso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.similarity.SemanticSimilarity;
import bp.retrieve.similarity.Similarity;
import main.resources.repository.BPModelsCollection;

/**
 * Synonyms similarity utils. Synchronization and normalization of business
 * process models labels.
 * 
 * @author Andrii Kopp
 */
public class SemanticSimilarityUtil {
	private static List<Set<String>> synonymsDictionary = new ArrayList<>();

	/**
	 * Implementation of the Jaccard's similarity measure.
	 */
	public static Similarity jaccardSimilarity = (first, second) -> {
		if (Similarity.union(first, second).isEmpty())
			return 1.0;

		return SemanticSimilarity.jaccardDistance(first, second);
	};

	/**
	 * Add a set of synonyms to the dictionary.
	 * 
	 * @param synonyms
	 *            - a set of synonyms.
	 */
	public static void addSynonyms(String... synonyms) {
		Set<String> synonymsSet = new HashSet<>();
		synonymsSet.addAll(Arrays.asList(synonyms));
		synonymsDictionary.add(synonymsSet);
	}

	/**
	 * Check if the pair of words are synonyms according to the dictionary.
	 * 
	 * @param first
	 *            - a first word;
	 * @param second
	 *            - a second word.
	 * @return true if the pair of words are synonyms.
	 */
	public static boolean areSynonyms(String first, String second) {
		for (Set<String> synonyms : synonymsDictionary)
			if (synonyms.containsAll(Arrays.asList(first, second)))
				return true;

		return false;
	}

	/**
	 * Replace labels in two business process models to theirs synonyms from
	 * dictionary.
	 * 
	 * @param firstModelGraph
	 *            - a first source model;
	 * @param synchronizedFirstModelGraph
	 *            - a first modified model;
	 * @param secondModelGraph
	 *            - a second source model;
	 * @param synchronizedSecondModelGraph
	 *            - a second modified model.
	 */
	public static void synchronizeBPModels(BPModelRDFGraph firstModelGraph, BPModelRDFGraph synchronizedFirstModelGraph,
			BPModelRDFGraph secondModelGraph, BPModelRDFGraph synchronizedSecondModelGraph) {
		for (BPModelRDFGraph.BPModelRDFStatement rdfStatement : firstModelGraph.getStatements())
			synchronizedFirstModelGraph.addStatement(rdfStatement.getSubject(), rdfStatement.getPredicate(),
					rdfStatement.getObject());

		for (BPModelRDFGraph.BPModelRDFStatement rdfStatement : secondModelGraph.getStatements())
			synchronizedSecondModelGraph.addStatement(rdfStatement.getSubject(), rdfStatement.getPredicate(),
					rdfStatement.getObject());

		for (BPModelRDFGraph.BPModelRDFStatement firstRdfStatement : synchronizedFirstModelGraph.getStatements())
			for (BPModelRDFGraph.BPModelRDFStatement secondRdfStatement : synchronizedSecondModelGraph
					.getStatements()) {
				if (areSynonyms(firstRdfStatement.getSubject(), secondRdfStatement.getSubject()))
					secondRdfStatement.setSubject(firstRdfStatement.getSubject());

				if (areSynonyms(firstRdfStatement.getObject(), secondRdfStatement.getObject()))
					secondRdfStatement.setObject(firstRdfStatement.getObject());
			}
	}

	/**
	 * Apply lower case transformations to all labels of a business process model.
	 * Replace spaces with underscores.
	 * 
	 * @param modelGraph
	 *            - a business process model to be modified.
	 * @return a modified business process model.
	 */
	public static BPModelRDFGraph normalizeBPModel(BPModelRDFGraph modelGraph) {
		BPModelRDFGraph normalizedModelGraph = new BPModelRDFGraph(modelGraph.getName());

		for (BPModelRDFGraph.BPModelRDFStatement rdfStatement : modelGraph.getStatements()) {
			String subject = rdfStatement.getSubject().toLowerCase().replaceAll("\\s+", "_");
			String predicate = rdfStatement.getPredicate().toLowerCase().replaceAll("\\s+", "_");
			String object = rdfStatement.getObject().toLowerCase().replaceAll("\\s+", "_");

			normalizedModelGraph.addStatement(subject, predicate, object);
		}

		return normalizedModelGraph;
	}

	@Test
	public void test() {
		for (GenericProcessModel first : BPModelsCollection.models)
			for (GenericProcessModel second : BPModelsCollection.models) {
				AcceleratedPSOBPModelsSimilarity modelsSimilarity = new AcceleratedPSOBPModelsSimilarity();
				modelsSimilarity.defineSimilarityMethod(jaccardSimilarity);

				modelsSimilarity.compareBPModelRDFGraphs(normalizeBPModel(first.getModelDescription()),
						normalizeBPModel(second.getModelDescription()));
			}
	}
}
