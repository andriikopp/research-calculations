package bp.retrieve.collection;

import static bp.retrieve.generator.ProcessModelGenerator.model;
import static bp.retrieve.generator.ProcessModelGenerator.name;
import static bp.retrieve.generator.flow.Event.endEvent;
import static bp.retrieve.generator.flow.Event.startEvent;
import static bp.retrieve.generator.flow.Gateway.and;
import static bp.retrieve.generator.flow.Task.task;

import java.util.Arrays;

import org.junit.Test;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.CustomizableBPModelsSimilarity;
import bp.retrieve.container.TableBPModelsContainer;
import bp.retrieve.container.api.BPModelsContainer;
import bp.retrieve.similarity.SemanticSimilarity;

public class ProcessModelRepositoryTest {
	private ProcessModelRepository repository = ProcessModelRepository.INSTANCE;
	
	@Test
	public void test() {
		CustomizableBPModelsSimilarity modelsSimilarity = new CustomizableBPModelsSimilarity();
		SemanticSimilarity similarity = new SemanticSimilarity();
		modelsSimilarity.defineSimilarityMethod(similarity);

		BPModelsContainer container = new TableBPModelsContainer(modelsSimilarity);
		
		repository.setProcessModelsContainer(container);
		
		repository.addAll(Arrays.asList(
			new FirstProcessModel(),
			new SecondProcessModel()
		));
		
		System.out.println(repository.find(new SecondProcessModel()));
	}
	
	private static class FirstProcessModel extends GenericProcessModel {

		public FirstProcessModel() {
			super("First sample model", "first.bpmn", "first.png");
		}

		@Override
		public BPModelRDFGraph getModelDescription() {
			return model(
				name("Model 01")
				.sequence(startEvent(), task("Order"), task("Receive goods"), and())
				.split(and(), task("Verify invoice"), task("Store goods"))
				.join(and(), task("Verify invoice"), task("Store goods"))
				.sequence(and(), endEvent())
			);
		}
	}
	
	private static class SecondProcessModel extends GenericProcessModel {
		
		public SecondProcessModel() {
			super("Second sample model", "second.bpmn", "second.png");
		}

		@Override
		public BPModelRDFGraph getModelDescription() {
			return model(
				name("Model 02")
				.sequence(startEvent(), task("Order"), task("Verification invoice"), endEvent())
			);
		}
	}
}
