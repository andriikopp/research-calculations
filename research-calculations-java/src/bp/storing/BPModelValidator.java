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
	public static final String PR_USED_BY = "used_by";
	public static final String PR_EXECUTES = "executes";
	public static final String PR_IS_INPUT_FOR = "is_input_for";
	public static final String PR_IS_OUTPUT_OF = "is_output_of";
	public static final String PR_MEASURES = "measures";
	public static final String PR_TYPE = "type";
    public static final String PR_IS_REGULATION_OF = "is_regulation_of";
    public static final String PR_CONTAINS = "contains";


    public static final String RES_FUNCTION = "function";
	public static final String RES_PROCESS = "process";
	public static final String RES_GATEWAY = "gateway";
	public static final String RES_EVENT = "event";
	public static final String RES_ROLE = "role";
	public static final String RES_DEPARTMENT = "department";
	public static final String RES_SUPPORTING_SYSTEM = "supporting_system";
	public static final String RES_BUSINESS_OBJECT = "business_object";
	public static final String RES_KPI = "kpi";
    public static final String RES_RESOURCE = "resource";
    public static final String RES_MATERIAL = "material";
    public static final String RES_INFORMATION = "information";


    /**
	 * This event might be used only as BPMN-based model component 'Start Event'.
	 */
	public static final String BPMN_START_EVENT = "start_event";

	/**
	 * This event might be used only as BPMN-based model component 'End Event'.
	 */
	public static final String BPMN_END_EVENT = "end_event";

	/**
	 * This gateway might be used only as 'AND' gateway.
	 */
	public static final String AND_GATEWAY = "and_gateway";

	/**
	 * This gateway might be used only as 'OR' gateway.
	 */
	public static final String OR_GATEWAY = "or_gateway";

	/**
	 * This gateway might be used only as 'XOR' gateway.
	 */
	public static final String XOR_GATEWAY = "xor_gateway";

	private static final String INVALID_STATEMENT = AppProperties.INSTANCE.getProperty("errInvalidStatement");

	private Map<String, String> declaredResources;

	public BPModelValidator(Map<String, String> declaredResources) {
		this.declaredResources = declaredResources;
	}

	/**
	 * Throws runtime exception if a property for a specified subject is not valid.
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
	 * Throws runtime exception if a property for a specified object is not valid.
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
			if (!property.equals(PR_TRIGGERS) && !property.equals(PR_EXECUTES) && !property.equals(PR_USED_BY)
					&& !property.equals(PR_IS_INPUT_FOR) && !property.equals(PR_IS_OUTPUT_OF)
					&& !property.equals(PR_MEASURES)) {
				throw new RuntimeException(INVALID_STATEMENT);
			}
		} else {
			throw new RuntimeException(INVALID_STATEMENT);
		}
	}
}
