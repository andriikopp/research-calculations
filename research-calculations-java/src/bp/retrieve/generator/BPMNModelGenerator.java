package bp.retrieve.generator;

import bp.retrieve.BPModelRDFGraph;
import bp.storing.BPModelValidator;

/**
 * Provides an easy way to generate BPMN model as a set of RDF statements.
 * 
 * @author Andrii Kopp
 */
public class BPMNModelGenerator {
	// The collection of RDF statements (RDF-graph).
	private BPModelRDFGraph rdfGraph;

	// Preceding flow object.
	private String preceding;
	// Preceding flow objects are running parallel.
	private String[] parallelPrecedings;
	// Preceding flow objects are running exclusive.
	private String[] exclusivePrecedings;

	private BPMNModelGenerator(String name) {
		rdfGraph = new BPModelRDFGraph(name);
	}

	/**
	 * Creates BPMN model with the specified name.
	 * 
	 * @param name
	 *            - the name of model.
	 * @return the instance of BPMN model generator.
	 */
	public static BPMNModelGenerator model(String name) {
		return new BPMNModelGenerator(name);
	}

	/**
	 * Creates start event of the model.
	 * 
	 * @return modified instance with the start event.
	 */
	public BPMNModelGenerator start() {
		rdfGraph.addBPMNStartEvent();
		preceding = BPModelValidator.BPMN_START_EVENT;
		return this;
	}

	/**
	 * Creates task of the model.
	 * 
	 * @param name
	 *            - the name of the created task.
	 * @return modified instance with the task.
	 */
	public BPMNModelGenerator task(String name) {
		if (preceding == null) {
			throw new IllegalStateException();
		}

		rdfGraph.addFunction(name);
		sequence(name);
		return this;
	}

	/**
	 * Creates intermediate event of the model.
	 * 
	 * @param name
	 *            - the name of the created event.
	 * @return modified instance with the event.
	 */
	public BPMNModelGenerator event(String name) {
		if (preceding == null) {
			throw new IllegalStateException();
		}

		rdfGraph.addEvent(name);
		sequence(name);
		return this;
	}

	/**
	 * Creates parallel tasks using AND-split/join.
	 * 
	 * @param tasks
	 *            - the names of tasks which run parallel.
	 * @return modified instance with the parallel tasks.
	 */
	public BPMNModelGenerator parallel(String... tasks) {
		if (preceding == null) {
			throw new IllegalStateException();
		}

		for (String task : tasks) {
			rdfGraph.addFunction(task);
		}

		rdfGraph.addAndGateway();
		rdfGraph.addAndSplit(preceding, tasks);

		parallelPrecedings = tasks;
		preceding = "";

		return this;
	}

	/**
	 * Creates exclusive tasks using XOR-split/join.
	 * 
	 * @param tasks
	 *            - the names of tasks which run parallel.
	 * @return modified instance with the exclusive tasks.
	 */
	public BPMNModelGenerator exclusive(String... tasks) {
		if (preceding == null) {
			throw new IllegalStateException();
		}

		for (String task : tasks) {
			rdfGraph.addFunction(task);
		}

		rdfGraph.addXorGateway();
		rdfGraph.addXorSplit(preceding, tasks);

		exclusivePrecedings = tasks;
		preceding = "";

		return this;
	}

	/**
	 * Creates end event of the model.
	 * 
	 * @return modified instance with the end event.
	 */
	public BPModelRDFGraph end() {
		if (preceding == null) {
			throw new IllegalStateException();
		}

		rdfGraph.addBPMNEndEvent();
		sequence(BPModelValidator.BPMN_END_EVENT);
		preceding = null;
		return rdfGraph;
	}

	// Manages the sequence of process flow objects.
	private void sequence(String subsequent) {
		// Finish AND-split/join if the preceding is a set of parallel tasks.
		if (parallelPrecedings != null) {
			rdfGraph.addAndJoin(subsequent, parallelPrecedings);
			parallelPrecedings = null;
		}

		// Finish XOR-split/join if the preceding is a set of exclusive tasks.
		if (exclusivePrecedings != null) {
			rdfGraph.addXorJoin(subsequent, exclusivePrecedings);
			exclusivePrecedings = null;
		}

		// Add subsequent flow object if preceding object exists.
		if (!preceding.isEmpty()) {
			rdfGraph.addSequence(preceding, subsequent);
		}

		preceding = subsequent;
	}
}
