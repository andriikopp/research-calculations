package main.java.edu.kopp.phd.repository.domain.model.notation;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;
import main.java.edu.kopp.phd.repository.domain.model.BusinessModel;
import main.java.edu.kopp.phd.repository.domain.process.GenericProcess;

/**
 * IDEF0 business process model representation.
 */
public class IDEF0Model extends BusinessModel {
    public static final String NOTATION = "IDEF0";

    private IDEF0Model(String name, GenericProcess process) {
        super(name, process);
    }

    private IDEF0Model(String name, GenericProcess process, Granularity granularity) {
        super(name, process, granularity);
    }

    @Override
    public IDEF0Model start() {
        setupNotation(NOTATION);
        return this;
    }

    public static IDEF0Model createModel(String name, GenericProcess process) {
        return new IDEF0Model(name, process);
    }

    public static IDEF0Model createModel(String name, GenericProcess process, Granularity granularity) {
        return new IDEF0Model(name, process, granularity);
    }
}
