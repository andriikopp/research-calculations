package bp.retrieve.generator;

import static bp.retrieve.generator.ProcessModelGenerator.model;
import static bp.retrieve.generator.ProcessModelGenerator.name;
import static bp.retrieve.generator.flow.Event.endEvent;
import static bp.retrieve.generator.flow.Event.startEvent;
import static bp.retrieve.generator.flow.Gateway.and;
import static bp.retrieve.generator.flow.Task.task;
import static bp.retrieve.generator.organization.Department.department;
import static bp.retrieve.generator.system.SupportingSystem.system;

import org.junit.Test;

import bp.retrieve.BPModelRDFGraph;

public class ProcessModelGeneratorTest {

	@Test
	public void test() {
		BPModelRDFGraph firstModel = model(
			name("Model 01")
			.sequence(startEvent(), task("Order"), task("Receive goods"), and())
			.split(and(), task("Verify invoice"), task("Store goods"))
			.join(and(), task("Verify invoice"), task("Store goods"))
			.sequence(and(), endEvent())
		);
		
		BPModelRDFGraph secondModel = model(
			name("Model 02")
			.sequence(startEvent(), task("Order"), task("Verification invoice"), endEvent())
			.responsibility(department("Supply dept."), task("Order"), task("Verification invoice"))
			.support(system("SRM"), task("Order"))
		);
		
		System.out.println(firstModel);
		System.out.println(secondModel);
	}
}
