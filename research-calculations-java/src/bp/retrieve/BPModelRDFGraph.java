package bp.retrieve;

import java.util.ArrayList;
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
