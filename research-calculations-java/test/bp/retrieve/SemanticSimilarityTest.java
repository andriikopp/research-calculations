package bp.retrieve;

import static bp.retrieve.generator.BPMNModelGenerator.model;

import org.junit.Test;

import bp.retrieve.similarity.SemanticSimilarity;

public class SemanticSimilarityTest {
	private BPModelRDFGraph firstModel;
	private BPModelRDFGraph secondModel;

	private SemanticSimilarity similarity;
	private CustomizableBPModelsSimilarity bpModelsSimilarity;

	@Test
	public void testSimilarity() {
		similarity.addSynonyms("Verification invoice", "Verify invoice");
		
		bpModelsSimilarity.defineSimilarityMethod(similarity);

		bpModelsSimilarity.compareByFlowObjects();
		bpModelsSimilarity.compareByLabels();

		bpModelsSimilarity.compareBPModelRDFGraphs(firstModel, secondModel);
	}

	public SemanticSimilarityTest() {
		similarity = new SemanticSimilarity();
		bpModelsSimilarity = new CustomizableBPModelsSimilarity();

		firstModel = model("Model 1")
				.start()
				.task("Order")
				.task("Receive goods")
				.parallel("Verify invoice", "Store goods")
				.end();
				
		secondModel = model("Model 2")
				.start()
				.task("Order")
				.task("Verification invoice")
				.end();
	}
}
