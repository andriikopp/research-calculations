package bp.retrieve.generator;

import java.util.HashSet;
import java.util.Set;

import bp.retrieve.BPModelRDFGraph;
import bp.retrieve.generator.flow.Event;
import bp.retrieve.generator.flow.Gateway;
import bp.retrieve.generator.flow.ProcessFlowObject;
import bp.retrieve.generator.flow.Task;
import bp.retrieve.generator.organization.Department;
import bp.retrieve.generator.organization.OrganizationalUnit;
import bp.retrieve.generator.organization.Role;
import bp.retrieve.generator.system.SupportingSystem;
import bp.storing.BPModelValidator;

/**
 * Provides an easy way to generate a Business Process Model as a set of RDF
 * statements. Includes more options comparing to the older generator
 * {@link BPMNModelGenerator} of simile BPMN models.
 * 
 * @author Andrii Kopp
 */
public class ProcessModelGenerator {
	private BPModelRDFGraph rdfGraph;

	private Set<ProcessFlowObject> processFlowObjects;
	private Set<OrganizationalUnit> organizationalUnits;
	private Set<SupportingSystem> supportingSystems;

	/**
	 * Represents a Business Process Model as an RDF graph.
	 * 
	 * @param generator
	 *            - the model generator used to describe process model.
	 * @return a description of BP model in the form of RDF graph.
	 */
	public static BPModelRDFGraph model(ProcessModelGenerator generator) {
		return generator.rdfGraph;
	}

	/**
	 * Creates a Business Process Model with the specified name.
	 * 
	 * @param name
	 *            - the name of model.
	 * @return the instance of model generator.
	 */
	public static ProcessModelGenerator name(String name) {
		return new ProcessModelGenerator(name);
	}

	// Process flow objects.

	/**
	 * Creates a sequence of two tasks.
	 * 
	 * @param preceding
	 *            - preceding task;
	 * @param subsequent
	 *            - subsequent task.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Task preceding, Task subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addFunction(preceding.getURI());
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addFunction(subsequent.getURI());
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of the event and task.
	 * 
	 * @param preceding
	 *            - preceding event;
	 * @param subsequent
	 *            - subsequent task.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Event preceding, Task subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addEvent(preceding.getURI());
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addFunction(subsequent.getURI());
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of the task end event.
	 * 
	 * @param preceding
	 *            - preceding task;
	 * @param subsequent
	 *            - subsequent event.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Task preceding, Event subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addFunction(preceding.getURI());
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addEvent(subsequent.getURI());
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of two events.
	 * 
	 * @param preceding
	 *            - preceding event;
	 * @param subsequent
	 *            - subsequent event.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Event preceding, Event subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addEvent(preceding.getURI());
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addEvent(subsequent.getURI());
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of the task and gateway.
	 * 
	 * @param preceding
	 *            - preceding task;
	 * @param subsequent
	 *            - subsequent gateway.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Task preceding, Gateway subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addFunction(preceding.getURI());
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addStatement(subsequent.getURI(), BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of the gateway and task.
	 * 
	 * @param preceding
	 *            - preceding gateway;
	 * @param subsequent
	 *            - subsequent task.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Gateway preceding, Task subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addStatement(preceding.getURI(), BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addFunction(subsequent.getURI());
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of the event and gateway.
	 * 
	 * @param preceding
	 *            - preceding event;
	 * @param subsequent
	 *            - subsequent gateway.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Event preceding, Gateway subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addEvent(preceding.getURI());
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addStatement(subsequent.getURI(), BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of the gateway and event.
	 * 
	 * @param preceding
	 *            - preceding gateway;
	 * @param subsequent
	 *            - subsequent event.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Gateway preceding, Event subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addStatement(preceding.getURI(), BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addEvent(subsequent.getURI());
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Creates a sequence of two gateways.
	 * 
	 * @param preceding
	 *            - preceding gateway;
	 * @param subsequent
	 *            - subsequent gateway.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(Gateway preceding, Gateway subsequent) {
		if (!processFlowObjects.contains(preceding)) {
			rdfGraph.addStatement(preceding.getURI(), BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
			processFlowObjects.add(preceding);
		}

		if (!processFlowObjects.contains(subsequent)) {
			rdfGraph.addStatement(subsequent.getURI(), BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
			processFlowObjects.add(subsequent);
		}

		rdfGraph.addSequence(preceding.getURI(), subsequent.getURI());
		return this;
	}

	/**
	 * Created a split gateway.
	 * 
	 * @param preceding
	 *            - the gateway;
	 * @param subsequent
	 *            - subsequent splitted process flow objects.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator split(Gateway preceding, ProcessFlowObject... subsequent) {
		for (ProcessFlowObject object : subsequent) {
			if (object instanceof Task) {
				Task task = (Task) object;
				sequence(preceding, task);
			}

			if (object instanceof Event) {
				Event event = (Event) object;
				sequence(preceding, event);
			}

			if (object instanceof Gateway) {
				Gateway gateway = (Gateway) object;
				sequence(preceding, gateway);
			}
		}

		return this;
	}

	/**
	 * Creates a join gateway.
	 * 
	 * @param subsequent
	 *            - the gateway;
	 * @param preceding
	 *            - preceding joined process flow objects.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator join(Gateway subsequent, ProcessFlowObject... preceding) {
		for (ProcessFlowObject object : preceding) {
			if (object instanceof Task) {
				Task task = (Task) object;
				sequence(task, subsequent);
			}

			if (object instanceof Event) {
				Event event = (Event) object;
				sequence(event, subsequent);
			}

			if (object instanceof Gateway) {
				Gateway gateway = (Gateway) object;
				sequence(gateway, subsequent);
			}
		}

		return this;
	}

	/**
	 * Creates a sequence of process flow objects.
	 * 
	 * @param objects
	 *            - process flow objects.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator sequence(ProcessFlowObject... objects) {
		for (int i = 0; i < objects.length - 1; i++) {
			// Task triggers objects.
			if (objects[i] instanceof Task && objects[i + 1] instanceof Task) {
				Task preceding = (Task) objects[i];
				Task subsequent = (Task) objects[i + 1];
				sequence(preceding, subsequent);
			}

			if (objects[i] instanceof Task && objects[i + 1] instanceof Event) {
				Task preceding = (Task) objects[i];
				Event subsequent = (Event) objects[i + 1];
				sequence(preceding, subsequent);
			}

			if (objects[i] instanceof Task && objects[i + 1] instanceof Gateway) {
				Task preceding = (Task) objects[i];
				Gateway subsequent = (Gateway) objects[i + 1];
				sequence(preceding, subsequent);
			}

			// Event triggers objects.
			if (objects[i] instanceof Event && objects[i + 1] instanceof Task) {
				Event preceding = (Event) objects[i];
				Task subsequent = (Task) objects[i + 1];
				sequence(preceding, subsequent);
			}

			if (objects[i] instanceof Event && objects[i + 1] instanceof Event) {
				Event preceding = (Event) objects[i];
				Event subsequent = (Event) objects[i + 1];
				sequence(preceding, subsequent);
			}

			if (objects[i] instanceof Event && objects[i + 1] instanceof Gateway) {
				Event preceding = (Event) objects[i];
				Gateway subsequent = (Gateway) objects[i + 1];
				sequence(preceding, subsequent);
			}

			// Gateway triggers objects.
			if (objects[i] instanceof Gateway && objects[i + 1] instanceof Task) {
				Gateway preceding = (Gateway) objects[i];
				Task subsequent = (Task) objects[i + 1];
				sequence(preceding, subsequent);
			}

			if (objects[i] instanceof Gateway && objects[i + 1] instanceof Event) {
				Gateway preceding = (Gateway) objects[i];
				Event subsequent = (Event) objects[i + 1];
				sequence(preceding, subsequent);
			}

			if (objects[i] instanceof Gateway && objects[i + 1] instanceof Gateway) {
				Gateway preceding = (Gateway) objects[i];
				Gateway subsequent = (Gateway) objects[i + 1];
				sequence(preceding, subsequent);
			}
		}

		return this;
	}

	// Organizational units.

	/**
	 * Assigns responsible department to the task.
	 * 
	 * @param responsible
	 *            - assigned department;
	 * @param task
	 *            - the task.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator responsibility(Department responsible, Task task) {
		if (!organizationalUnits.contains(responsible)) {
			rdfGraph.addDepartment(responsible.getURI());
			organizationalUnits.add(responsible);
		}

		if (!processFlowObjects.contains(task)) {
			rdfGraph.addFunction(task.getURI());
			processFlowObjects.add(task);
		}

		rdfGraph.executes(responsible.getURI(), task.getURI());
		return this;
	}

	/**
	 * Assigns responsible role for to task.
	 * 
	 * @param role
	 *            - assigned role;
	 * @param task
	 *            - the task.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator responsibility(Role role, Task task) {
		if (!organizationalUnits.contains(role)) {
			rdfGraph.addRole(role.getURI());
			organizationalUnits.add(role);
		}

		if (!processFlowObjects.contains(task)) {
			rdfGraph.addFunction(task.getURI());
			processFlowObjects.add(task);
		}

		rdfGraph.executes(role.getURI(), task.getURI());
		return this;
	}

	/**
	 * Assigns responsible unit to multiple tasks.
	 * 
	 * @param unit
	 *            - responsible unit;
	 * @param tasks
	 *            - a set of tasks.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator responsibility(OrganizationalUnit unit, Task... tasks) {
		for (Task task : tasks) {
			if (unit instanceof Department) {
				Department department = (Department) unit;
				responsibility(department, task);
			}

			if (unit instanceof Role) {
				Role role = (Role) unit;
				responsibility(role, task);
			}
		}

		return this;
	}

	// Supporting systems.

	/**
	 * Assigns the IT-system to support the tasks.
	 * 
	 * @param system
	 *            - assigned IT-system;
	 * @param task
	 *            - the task.
	 * @return the instance of model generator.
	 */
	public ProcessModelGenerator support(SupportingSystem system, Task... tasks) {
		if (!supportingSystems.contains(system)) {
			rdfGraph.addSupportingSystem(system.getURI());
			supportingSystems.add(system);
		}

		for (Task task : tasks) {
			if (!processFlowObjects.contains(task)) {
				rdfGraph.addFunction(task.getURI());
				processFlowObjects.add(task);
			}

			rdfGraph.usedBy(system.getURI(), task.getURI());
		}
		return this;
	}

	private ProcessModelGenerator(String name) {
		this.rdfGraph = new BPModelRDFGraph(name);

		this.processFlowObjects = new HashSet<ProcessFlowObject>();
		this.organizationalUnits = new HashSet<OrganizationalUnit>();
		this.supportingSystems = new HashSet<SupportingSystem>();
	}
}
