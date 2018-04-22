package bp.retrieve.generator.flow;

import bp.storing.BPModelValidator;

/**
 * Describes a Gateway within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class Gateway extends ProcessFlowObject {

	/**
	 * Creates the AND-split/join gateway.
	 * 
	 * @return the AND-split/join gateway.
	 */
	public static Gateway and() {
		return new Gateway(BPModelValidator.AND_GATEWAY);
	}

	/**
	 * Creates the OR-split/join gateway.
	 * 
	 * @return the OR-split/join gateway.
	 */
	public static Gateway or() {
		return new Gateway(BPModelValidator.OR_GATEWAY);
	}

	/**
	 * Creates the XOR-split/join gateway.
	 * 
	 * @return the XOR-split/join gateway.
	 */
	public static Gateway xor() {
		return new Gateway(BPModelValidator.XOR_GATEWAY);
	}

	private Gateway(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Gateway [getURI()=" + getURI() + "]";
	}
}
