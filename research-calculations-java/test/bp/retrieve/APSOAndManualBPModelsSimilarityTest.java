package bp.retrieve;

import static bp.retrieve.config.ProcessModelRepositoryConfig.config;
import static bp.retrieve.generator.ProcessModelGenerator.model;
import static bp.retrieve.generator.ProcessModelGenerator.name;
import static bp.retrieve.generator.flow.Event.endEvent;
import static bp.retrieve.generator.flow.Event.startEvent;
import static bp.retrieve.generator.flow.Gateway.and;
import static bp.retrieve.generator.flow.Task.task;

import org.junit.Test;

import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.collection.ProcessModelRepository;

public class APSOAndManualBPModelsSimilarityTest {
	private static final double SIMILARITY_THRESHOLD = 0.6;
	
	@Test
	public void test() {
		ProcessModelRepository repository = config()
			.domain(0.33, 0.33, 0.34)
			.closeness(0.5, 0.5)
			.threshold(SIMILARITY_THRESHOLD)
			.semantic()
			.listContainer()
			.collection(
				new FirstProcessModel(),
				new SecondProcessModel()
			);
		
		System.out.println("Manual similarity coefficients setup:");
		repository.find(new SecondProcessModel());
		
		repository.removeAll();
		
		repository = config()
			.apso()
			.threshold(SIMILARITY_THRESHOLD)
			.semantic()
			.listContainer()
			.collection(
				new FirstProcessModel(),
				new SecondProcessModel()
			);
			
		System.out.println("\nAPSO-based similarity coefficients setup:");
		repository.find(new SecondProcessModel());
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
