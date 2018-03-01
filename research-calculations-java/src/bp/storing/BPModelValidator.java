package bp.storing;

import java.util.Map;

import bp.AppProperties;

/**
 * Provides validation for RDF statements according to the RDF Schema.
 * 
 * @author Andrii Kopp
 */
public class BPModelValidator {
	public static final String PR_TRIGGERS = "triggers";
	public static final String PR_USED_BY = "usedBy";
	public static final String PR_EXECUTES = "executes";
	public static final String PR_IS_INPUT_FOR = "isInputFor";
	public static final String PR_IS_OUTPUT_OF = "isOutputOf";
	public static final String PR_MEASURES = "measures";

	public static final String RES_FUNCTION = "Function";
	public static final String RES_PROCESS = "Process";
	public static final String RES_GATEWAY = "Gateway";
	public static final String RES_EVENT = "Event";
	public static final String RES_ROLE = "Role";
	public static final String RES_DEPARTMENT = "Department";
	public static final String RES_SUPPORTING_SYSTEM = "SupportingSystem";
	public static final String RES_BUSINESS_OBJECT = "BusinessObject";
	public static final String RES_KPI = "KPI";

	private static final String INVALID_STATEMENT = AppProperties.INSTANCE.getProperty("errInvalidStatement");

	private Map<String, String> declaredResources;

	public BPModelValidator(Map<String, String> declaredResources) {
		this.declaredResources = declaredResources;
	}

	/**
	 * Throws runtime exception if a property for a specified subject is not
	 * valid.
	 * 
	 * @param subject
	 *            - a declared resource of the RDF statements set;
	 * @param property
	 *            - a property used with the subject.
	 */
	public void validateSubject(String subject, String property) {
		String resourceDeclaration = declaredResources.get(subject);

		if (resourceDeclaration.equals(RES_FUNCTION) || resourceDeclaration.equals(RES_PROCESS)
				|| resourceDeclaration.equals(RES_GATEWAY) || resourceDeclaration.equals(RES_EVENT)) {
			if (!property.equals(PR_TRIGGERS)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		}

		if (resourceDeclaration.equals(RES_ROLE) || resourceDeclaration.equals(RES_DEPARTMENT)) {
			if (!property.equals(PR_EXECUTES)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		}

		if (resourceDeclaration.equals(RES_SUPPORTING_SYSTEM)) {
			if (!property.equals(PR_USED_BY)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		}

		if (resourceDeclaration.equals(RES_BUSINESS_OBJECT)) {
			if (!property.equals(PR_IS_INPUT_FOR) && !property.equals(PR_IS_OUTPUT_OF)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		}

		if (resourceDeclaration.equals(RES_KPI)) {
			if (!property.equals(PR_MEASURES)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		}
	}

	/**
	 * Throws runtime exception if a property for a specified object is not
	 * valid.
	 * 
	 * @param object
	 *            - a declared resource of the RDF statements set;
	 * @param property
	 *            - a property used with the object.
	 */
	public void validateObject(String object, String property) {
		String resourceDeclaration = declaredResources.get(object);

		if (resourceDeclaration.equals(RES_FUNCTION) || resourceDeclaration.equals(RES_PROCESS)
				|| resourceDeclaration.equals(RES_GATEWAY) || resourceDeclaration.equals(RES_EVENT)) {
			if (!property.equals(PR_TRIGGERS)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		} else {
			throw new RuntimeException(INVALID_STATEMENT);
		}
	}
}
