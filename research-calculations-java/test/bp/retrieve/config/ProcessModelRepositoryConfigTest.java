package bp.retrieve.config;

import static bp.retrieve.config.ProcessModelRepositoryConfig.config;
import static bp.retrieve.generator.ProcessModelGenerator.model;
import static bp.retrieve.generator.ProcessModelGenerator.name;
import static bp.retrieve.generator.flow.Event.endEvent;
import static bp.retrieve.generator.flow.Event.startEvent;
import static bp.retrieve.generator.flow.Gateway.and;
import static bp.retrieve.generator.flow.Task.task;

import org.junit.Test;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.collection.GenericProcessModel;
import bp.retrieve.collection.ProcessModelRepository;

public class ProcessModelRepositoryConfigTest {

	@Test
	public void test() {
		ProcessModelRepository repository = config()
			.apso()
			.threshold(0.6)
			.semantic()
			.tableContainer()
			.collection(
				new FirstProcessModel(),
				new SecondProcessModel()
			);
		
		for (GenericProcessModel model : repository.retrieveAll()) {
			System.out.println(model);
		}
		
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
