package main.java.edu.kopp.phd.repository.domain.model.notation;

import main.java.edu.kopp.phd.repository.domain.model.DataFlowModel;

/**
 * DFD business process model representation.
 */
public class DFDModel extends DataFlowModel {
    public static final String NOTATION = "DFD";

    private DFDModel(String name, String process) {
        super(name, process);
    }

    private DFDModel(String name, String process, String granularity) {
        super(name, process, granularity);
    }

    @Override
    public DFDModel start() {
        setupNotation(NOTATION);
        return this;
    }

    public static DFDModel createModel(String name, String process) {
        return new DFDModel(name, process);
    }

    public static DFDModel createModel(String name, String process, String granularity) {
        return new DFDModel(name, process, granularity);
    }
}
