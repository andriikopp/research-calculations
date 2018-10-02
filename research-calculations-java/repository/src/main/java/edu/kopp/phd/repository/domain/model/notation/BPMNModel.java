package main.java.edu.kopp.phd.repository.domain.model.notation;

import main.java.edu.kopp.phd.repository.domain.model.ControlFlowModel;

/**
 * BPMN business process model description.
 *
 * @author Andrii Kopp
 */
public class BPMNModel extends ControlFlowModel {
    public static final String NOTATION = "BPMN";

    private BPMNModel(String name, String process) {
        super(name, process);
    }

    private BPMNModel(String name, String process, String granularity) {
        super(name, process, granularity);
    }

    @Override
    public BPMNModel start() {
        setupNotation(NOTATION);
        return this;
    }

    public static BPMNModel createModel(String name, String process) {
        return new BPMNModel(name, process);
    }

    public static BPMNModel createModel(String name, String process, String granularity) {
        return new BPMNModel(name, process, granularity);
    }
}
