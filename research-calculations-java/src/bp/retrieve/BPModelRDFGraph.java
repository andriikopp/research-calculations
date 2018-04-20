package bp.retrieve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import bp.storing.BPModelValidator;

/**
 * Describes an RDF graph or a set of RDF statements.
 * 
 * @author Andrii Kopp
 */
public class BPModelRDFGraph {
	private static final String RDF_TYPE = "type";

	private String name;
	private List<BPModelRDFStatement> statements;

	public BPModelRDFGraph(String name) {
		this.name = name;
		this.statements = new ArrayList<BPModelRDFStatement>();
	}

	public BPModelRDFGraph(String name, Model model) {
		this.name = name;
		this.statements = new ArrayList<BPModelRDFStatement>();

		for (StmtIterator iterator = model.listStatements(); iterator.hasNext();) {
			Statement statement = iterator.nextStatement();

			String subject = statement.asTriple().getSubject().getLocalName();
			String predicate = statement.asTriple().getPredicate().getLocalName();
			String object = statement.asTriple().getObject().getLocalName();

			statements.add(new BPModelRDFStatement(subject, predicate, object));
		}
	}

	public BPModelRDFGraph(String name, BPModelRDFGraph bpModel) {
		this.name = name;
		this.statements = new ArrayList<BPModelRDFStatement>();

		for (BPModelRDFStatement statement : bpModel.getStatements()) {
			statements.add(
					new BPModelRDFStatement(statement.getSubject(), statement.getPredicate(), statement.getObject()));
		}
	}

	/**
	 * Allows to add RDF statement. Don't use it on a production, only for testing!
	 * 
	 * @param subject
	 *            - a subject of RDF statement;
	 * @param predicate
	 *            - a predicate of RDF statement;
	 * @param object
	 *            - an object of RDF statement.
	 */
	public void addStatement(String subject, String predicate, String object) {
		statements.add(new BPModelRDFStatement(subject, predicate, object));
	}

	/**
	 * Add function to model's description.
	 * 
	 * @param resource
	 *            - function URI.
	 */
	public void addFunction(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_FUNCTION);
	}

	/**
	 * Add process to model's description.
	 * 
	 * @param resource
	 *            - process URI.
	 */
	public void addProcess(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_PROCESS);
	}

	/**
	 * Add event to model's description.
	 * 
	 * @param resource
	 *            - event URI.
	 */
	public void addEvent(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_EVENT);
	}

	/**
	 * Add role to model's description.
	 * 
	 * @param resource
	 *            - role URI.
	 */
	public void addRole(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_ROLE);
	}

	/**
	 * Add department to model's description.
	 * 
	 * @param resource
	 *            - department URI.
	 */
	public void addDepartment(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_DEPARTMENT);
	}

	/**
	 * Add supporting system to model's description.
	 * 
	 * @param resource
	 *            - supporting system URI.
	 */
	public void addSupportingSystem(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_SUPPORTING_SYSTEM);
	}

	/**
	 * Add business object to model's description.
	 * 
	 * @param resource
	 *            - business object URI.
	 */
	public void addBusinessObject(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_BUSINESS_OBJECT);
	}

	/**
	 * Add KPI to model's description.
	 * 
	 * @param resource
	 *            - indicator URI.
	 */
	public void addKpi(String resource) {
		addStatement(resource, BPModelValidator.PR_TYPE, BPModelValidator.RES_KPI);
	}

	/**
	 * Add start event to BPMN-based model's description.
	 */
	public void addBPMNStartEvent() {
		addStatement(BPModelValidator.BPMN_START_EVENT, BPModelValidator.PR_TYPE, BPModelValidator.RES_EVENT);
	}

	/**
	 * Add end event to BPMN-based model's description.
	 */
	public void addBPMNEndEvent() {
		addStatement(BPModelValidator.BPMN_END_EVENT, BPModelValidator.PR_TYPE, BPModelValidator.RES_EVENT);
	}

	/**
	 * Use Sequence pattern to connect BPMN-specific start event.
	 * 
	 * @param resource
	 *            - subsequent flow object.
	 */
	public void addStartEventSequence(String resource) {
		addStatement(BPModelValidator.BPMN_START_EVENT, BPModelValidator.PR_TRIGGERS, resource);
	}

	/**
	 * Use Sequence pattern to connect BPMN-specific end event.
	 * 
	 * @param resource
	 *            - preceding flow object.
	 */
	public void addEndEventSequence(String resource) {
		addStatement(resource, BPModelValidator.PR_TRIGGERS, BPModelValidator.BPMN_END_EVENT);
	}

	/**
	 * Add 'AND' gateway usage to model's description.
	 */
	public void addAndGateway() {
		addStatement(BPModelValidator.AND_GATEWAY, BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
	}

	/**
	 * Add 'OR' gateway usage to model's description.
	 */
	public void addOrGateway() {
		addStatement(BPModelValidator.OR_GATEWAY, BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
	}

	/**
	 * Add 'XOR' gateway usage to model's description.
	 */
	public void addXorGateway() {
		addStatement(BPModelValidator.XOR_GATEWAY, BPModelValidator.PR_TYPE, BPModelValidator.RES_GATEWAY);
	}

	/**
	 * Use Parallel Split pattern to describe process flow.
	 * 
	 * @param preceding
	 *            - preceding flow object;
	 * @param subsequent
	 *            - subsequent flow objects.
	 */
	public void addAndSplit(String preceding, String... subsequent) {
		addStatement(preceding, BPModelValidator.PR_TRIGGERS, BPModelValidator.AND_GATEWAY);

		for (String object : subsequent) {
			addStatement(BPModelValidator.AND_GATEWAY, BPModelValidator.PR_TRIGGERS, object);
		}
	}

	/**
	 * Use Synchronization pattern to describe process flow.
	 * 
	 * @param subsequent
	 *            - subsequent flow object;
	 * @param preceding
	 *            - preceding flow objects.
	 */
	public void addAndJoin(String subsequent, String... preceding) {
		for (String object : preceding) {
			addStatement(object, BPModelValidator.PR_TRIGGERS, BPModelValidator.AND_GATEWAY);
		}

		addStatement(BPModelValidator.AND_GATEWAY, BPModelValidator.PR_TRIGGERS, subsequent);
	}

	/**
	 * Use Exclusive Choice pattern to describe process flow.
	 * 
	 * @param preceding
	 *            - preceding flow object;
	 * @param subsequent
	 *            - subsequent flow objects.
	 */
	public void addXorSplit(String preceding, String... subsequent) {
		addStatement(preceding, BPModelValidator.PR_TRIGGERS, BPModelValidator.XOR_GATEWAY);

		for (String object : subsequent) {
			addStatement(BPModelValidator.XOR_GATEWAY, BPModelValidator.PR_TRIGGERS, object);
		}
	}

	/**
	 * Use Simple Merge pattern to describe process flow.
	 * 
	 * @param subsequent
	 *            - subsequent flow object;
	 * @param preceding
	 *            - preceding flow objects.
	 */
	public void addXorJoin(String subsequent, String... preceding) {
		for (String object : preceding) {
			addStatement(object, BPModelValidator.PR_TRIGGERS, BPModelValidator.XOR_GATEWAY);
		}

		addStatement(BPModelValidator.XOR_GATEWAY, BPModelValidator.PR_TRIGGERS, subsequent);
	}

	/**
	 * Use OR-split to describe process flow.
	 * 
	 * @param preceding
	 *            - preceding flow object;
	 * @param subsequent
	 *            - subsequent flow objects.
	 */
	public void addOrSplit(String preceding, String... subsequent) {
		addStatement(preceding, BPModelValidator.PR_TRIGGERS, BPModelValidator.OR_GATEWAY);

		for (String object : subsequent) {
			addStatement(BPModelValidator.OR_GATEWAY, BPModelValidator.PR_TRIGGERS, object);
		}
	}

	/**
	 * Use OR-join to describe process flow.
	 * 
	 * @param subsequent
	 *            - subsequent flow object;
	 * @param preceding
	 *            - preceding flow objects.
	 */
	public void addOrJoin(String subsequent, String... preceding) {
		for (String object : preceding) {
			addStatement(object, BPModelValidator.PR_TRIGGERS, BPModelValidator.OR_GATEWAY);
		}

		addStatement(BPModelValidator.OR_GATEWAY, BPModelValidator.PR_TRIGGERS, subsequent);
	}

	/**
	 * Use Sequence pattern to describe process flow.
	 * 
	 * @param sequence
	 *            - array of process flow objects that should be considered as a
	 *            sequence.
	 */
	public void addSequence(String... sequence) {
		for (int i = 0; i < sequence.length - 1; i++) {
			addStatement(sequence[i], BPModelValidator.PR_TRIGGERS, sequence[i + 1]);
		}
	}

	/**
	 * Assign supporting system to one or more functions/processes.
	 * 
	 * @param supportingSystem
	 *            - assigned supporting system;
	 * @param functions
	 *            - functions or processes to which supporting system is assigned.
	 */
	public void usedBy(String supportingSystem, String... functions) {
		for (String function : functions) {
			addStatement(supportingSystem, BPModelValidator.PR_USED_BY, function);
		}
	}

	/**
	 * Assign organizational unit to one or more functions/processes.
	 * 
	 * @param organizationalUnit
	 *            - assigned organizational unit;
	 * @param functions
	 *            - functions or processes to which organizational unit is assigned.
	 */
	public void executes(String organizationalUnit, String... functions) {
		for (String function : functions) {
			addStatement(organizationalUnit, BPModelValidator.PR_EXECUTES, function);
		}
	}

	/**
	 * Assign inputs to a functions/process.
	 * 
	 * @param function
	 *            - considered function/process.
	 * @param businessObjects
	 *            - assigned inputs.
	 */
	public void isInputFor(String function, String... businessObjects) {
		for (String object : businessObjects) {
			addStatement(object, BPModelValidator.PR_IS_INPUT_FOR, function);
		}
	}

	/**
	 * Assign outputs to a functions/process.
	 * 
	 * @param function
	 *            - considered function/process.
	 * @param businessObjects
	 *            - assigned outputs.
	 */
	public void isOutputOf(String function, String... businessObjects) {
		for (String object : businessObjects) {
			addStatement(object, BPModelValidator.PR_IS_OUTPUT_OF, function);
		}
	}

	/**
	 * Assign indicators to a functions/process.
	 * 
	 * @param function
	 *            - considered function/process.
	 * @param indicators
	 *            - assigned indicators.
	 */
	public void measures(String function, String... indicators) {
		for (String indicator : indicators) {
			addStatement(indicator, BPModelValidator.PR_MEASURES, function);
		}
	}

	/**
	 * Returns a set of organizational units.
	 * 
	 * @return a set of organizational units.
	 */
	public Set<String> extractOrganizationalUnits() {
		Set<String> result = extractSubjectsByType(BPModelValidator.RES_ROLE);
		result.addAll(extractSubjectsByType(BPModelValidator.RES_DEPARTMENT));
		return result;
	}

	/**
	 * Returns a set of supporting systems.
	 * 
	 * @return a set of supporting systems.
	 */
	public Set<String> extractSupportingSystems() {
		return extractSubjectsByType(BPModelValidator.RES_SUPPORTING_SYSTEM);
	}

	/**
	 * Returns a set of flow objects.
	 * 
	 * @return a set of flow objects.
	 */
	public Set<String> extractFlowObjects() {
		Set<String> result = extractSubjectsByType(BPModelValidator.RES_FUNCTION);
		result.addAll(extractSubjectsByType(BPModelValidator.RES_PROCESS));
		result.addAll(extractSubjectsByType(BPModelValidator.RES_EVENT));
		result.addAll(extractSubjectsByType(BPModelValidator.RES_GATEWAY));
		return result;
	}

	/**
	 * Returns a set of flow objects.
	 * 
	 * @param includeGateways
	 *            - set true if gateways should be included.
	 * @return a set of flow objects.
	 */
	@Deprecated
	public Set<String> extractFlowObjects(boolean includeGateways) {
		Set<String> result = extractSubjectsByType(BPModelValidator.RES_FUNCTION);
		result.addAll(extractSubjectsByType(BPModelValidator.RES_PROCESS));
		result.addAll(extractSubjectsByType(BPModelValidator.RES_EVENT));
		if (includeGateways)
			result.addAll(extractSubjectsByType(BPModelValidator.RES_GATEWAY));
		return result;
	}

	/**
	 * Returns a set of flow objects except gateways.
	 * 
	 * @return a set of flow objects except gateways.
	 */
	public Set<String> extractFlowObjectsExceptGateways() {
		return extractFlowObjects(false);
	}

	/**
	 * Returns a set of flow objects except BPMN-specific start/end events.
	 * 
	 * @return a set of flow objects except BPMN-specific start/end events.
	 */
	public Set<String> extractFlowObjectsExceptBPMNStartEndEvents() {
		Set<String> result = extractFlowObjects();
		result.removeAll(Arrays.asList(BPModelValidator.BPMN_START_EVENT, BPModelValidator.BPMN_END_EVENT));
		return result;
	}

	/**
	 * Returns a set of flow objects except gateways and BPMN-specific start/end
	 * events.
	 * 
	 * @return a set of flow objects except gateways and BPMN-specific start/end
	 *         events.
	 */
	public Set<String> extractFlowObjectsExceptGatewaysAndBPMNStartEndEvents() {
		Set<String> result = extractFlowObjectsExceptGateways();
		result.removeAll(Arrays.asList(BPModelValidator.BPMN_START_EVENT, BPModelValidator.BPMN_END_EVENT));
		return result;
	}

	/**
	 * Returns a set of business objects.
	 * 
	 * @return a set of flow objects.
	 */
	public Set<String> extractBusinessObjects() {
		return extractSubjectsByType(BPModelValidator.RES_BUSINESS_OBJECT);
	}

	/**
	 * Returns a set of KPIs.
	 * 
	 * @return a set of KPIs.
	 */
	public Set<String> extractKPIs() {
		return extractSubjectsByType(BPModelValidator.RES_KPI);
	}

	/**
	 * Returns a set of processes that an organizational unit executes.
	 * 
	 * @param organizationalUnit
	 *            - an organizational unit.
	 * @return a set of processes that an organizational unit executes.
	 */
	public Set<String> executes(String organizationalUnit) {
		return extractObjectsByPredicate(organizationalUnit, BPModelValidator.PR_EXECUTES);
	}

	/**
	 * Returns a set of processes that a supporting system is used by.
	 * 
	 * @param organizationalUnit
	 *            - a supporting system.
	 * @return a set of processes that a supporting system is used by.
	 */
	public Set<String> usedBy(String supportingSystem) {
		return extractObjectsByPredicate(supportingSystem, BPModelValidator.PR_USED_BY);
	}

	/**
	 * Returns a set of flow objects that a flow object triggers.
	 * 
	 * @param flowObject
	 *            - a flow object.
	 * @return a set of flow objects that a flow object triggers.
	 */
	public Set<String> triggers(String flowObject) {
		return extractObjectsByPredicate(flowObject, BPModelValidator.PR_TRIGGERS);
	}

	/**
	 * Returns a set of processes that a business object is input for.
	 * 
	 * @param businessObject
	 *            - a business object.
	 * @return a set of processes that a business object is input for.
	 */
	public Set<String> isInputFor(String businessObject) {
		return extractObjectsByPredicate(businessObject, BPModelValidator.PR_IS_INPUT_FOR);
	}

	/**
	 * Returns a set of processes that a business object is output of.
	 * 
	 * @param businessObject
	 *            - a business object.
	 * @return a set of processes that a business object is output of.
	 */
	public Set<String> isOutputOf(String businessObject) {
		return extractObjectsByPredicate(businessObject, BPModelValidator.PR_IS_OUTPUT_OF);
	}

	/**
	 * Returns a set of processes measured by a KPI.
	 * 
	 * @param kpi
	 *            - a KPI.
	 * @return a set of processes measured by a KPI.
	 */
	public Set<String> measures(String kpi) {
		return extractObjectsByPredicate(kpi, BPModelValidator.PR_MEASURES);
	}

	/**
	 * Returns a set of subjects by a certain predicate and object if exits.
	 * 
	 * @param predicate
	 *            - a certain predicate;
	 * @param object
	 *            - a certain object.
	 * @return a set of subjects by a certain predicate and object if exits.
	 */
	public Set<String> extractSubjectsByPredicateAndObject(String predicate, String object) {
		Set<String> subjects = new HashSet<String>();

		for (BPModelRDFStatement statement : statements) {
			if (statement.getPredicate().equals(predicate) && statement.getObject().equals(object)) {
				subjects.add(statement.getSubject());
			}
		}

		return subjects;
	}

	private Set<String> extractSubjectsByType(String object) {
		Set<String> subjects = new HashSet<String>();

		for (BPModelRDFStatement statement : statements) {
			if (statement.getPredicate().equals(RDF_TYPE) && statement.getObject().equals(object)) {
				subjects.add(statement.getSubject());
			}
		}

		return subjects;
	}

	private Set<String> extractObjectsByPredicate(String subject, String predicate) {
		Set<String> objects = new HashSet<String>();

		for (BPModelRDFStatement statement : statements) {
			if (statement.getPredicate().equals(predicate) && statement.getSubject().equals(subject)) {
				objects.add(statement.getObject());
			}
		}

		return objects;
	}

	/**
	 * Describes an RDF statement.
	 * 
	 * @author Andrii Kopp
	 */
	public class BPModelRDFStatement {
		private String subject;
		private String predicate;
		private String object;

		public BPModelRDFStatement(String subject, String predicate, String object) {
			this.subject = subject;
			this.predicate = predicate;
			this.object = object;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
			result = prime * result + ((subject == null) ? 0 : subject.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BPModelRDFStatement other = (BPModelRDFStatement) obj;
			if (object == null) {
				if (other.object != null)
					return false;
			} else if (!object.equals(other.object))
				return false;
			if (predicate == null) {
				if (other.predicate != null)
					return false;
			} else if (!predicate.equals(other.predicate))
				return false;
			if (subject == null) {
				if (other.subject != null)
					return false;
			} else if (!subject.equals(other.subject))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "\n\t[" + subject + " " + predicate + " " + object + "]";
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getPredicate() {
			return predicate;
		}

		public void setPredicate(String predicate) {
			this.predicate = predicate;
		}

		public String getObject() {
			return object;
		}

		public void setObject(String object) {
			this.object = object;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((statements == null) ? 0 : statements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BPModelRDFGraph other = (BPModelRDFGraph) obj;
		if (statements == null) {
			if (other.statements != null)
				return false;
		} else if (!statements.equals(other.statements))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + statements;
	}

	public String getName() {
		return name;
	}

	public List<BPModelRDFStatement> getStatements() {
		return statements;
	}
}
