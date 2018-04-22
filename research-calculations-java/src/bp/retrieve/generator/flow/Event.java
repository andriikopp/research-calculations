package bp.retrieve.generator.flow;

import bp.storing.BPModelValidator;

/**
 * Describes an Event within a Business Process Model.
 * 
 * @author Andrii Kopp
 */
public class Event extends ProcessFlowObject {

	/**
	 * Creates an Event with the specified name.
	 * 
	 * @param name
	 *            - the name of event.
	 * @return created event.
	 */
	public static Event event(String name) {
		return new Event(name);
	}

	/**
	 * Creates BPMN-specific Start Event.
	 * 
	 * @return created start event.
	 */
	public static Event startEvent() {
		return new Event(BPModelValidator.BPMN_START_EVENT);
	}

	/**
	 * Creates BPMN-specific End Event.
	 * 
	 * @return created end event.
	 */
	public static Event endEvent() {
		return new Event(BPModelValidator.BPMN_END_EVENT);
	}

	private Event(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Event [getURI()=" + getURI() + "]";
	}
}
