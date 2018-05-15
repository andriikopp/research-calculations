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
import main.resources.repository.files.AccessManagement;
import main.resources.repository.files.AccountsPayable;
import main.resources.repository.files.ChangeManagement;
import main.resources.repository.files.ContractManagement;
import main.resources.repository.files.FirstSampleModel;
import main.resources.repository.files.HelpDesk;
import main.resources.repository.files.Offboarding;
import main.resources.repository.files.Onboarding;
import main.resources.repository.files.PurchaseRequest;
import main.resources.repository.files.SecondSampleModel;
import main.resources.repository.files.TravelRequest;
import main.resources.repository.files.VacationsRequest;

public class SemanticSimilarityUtil {
	private static List<Set<String>> synonymsDictionary = new ArrayList<>();

	public static Similarity jaccardSimilarity = (first, second) -> {
		if (Similarity.union(first, second).isEmpty())
			return 1.0;

		return SemanticSimilarity.jaccardDistance(first, second);
	};

	public static void addSynonyms(String... synonyms) {
		Set<String> synonymsSet = new HashSet<>();
		synonymsSet.addAll(Arrays.asList(synonyms));
		synonymsDictionary.add(synonymsSet);
	}

	public static boolean areSynonyms(String first, String second) {
		for (Set<String> synonyms : synonymsDictionary)
			if (synonyms.containsAll(Arrays.asList(first, second)))
				return true;

		return false;
	}

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
		GenericProcessModel[] models = { 
				new FirstSampleModel(),
				new SecondSampleModel(),
				new AccessManagement(),
				new ChangeManagement(),
				new AccountsPayable(),
				new HelpDesk(),
				new ContractManagement(),
				new PurchaseRequest(),
				new TravelRequest(),
				new Onboarding(),
				new Offboarding(),
				new VacationsRequest()
			};

		for (GenericProcessModel first : models)
			for (GenericProcessModel second : models) {
				AcceleratedPSOBPModelsSimilarity modelsSimilarity = new AcceleratedPSOBPModelsSimilarity();
				modelsSimilarity.defineSimilarityMethod(jaccardSimilarity);

				modelsSimilarity.compareBPModelRDFGraphs(normalizeBPModel(first.getModelDescription()),
						normalizeBPModel(second.getModelDescription()));
			}
	}
}
