package main.java.edu.kopp.phd.repository.domain.model.notation;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;
import main.java.edu.kopp.phd.repository.domain.model.BusinessModel;
import main.java.edu.kopp.phd.repository.domain.process.GenericProcess;

/**
 * ARIS eEPC business process model representation.
 */
public class ARISeEPCModel extends BusinessModel {
    public static final String NOTATION = "ARISeEPC";

    private ARISeEPCModel(String name, GenericProcess process) {
        super(name, process);
    }

    private ARISeEPCModel(String name, GenericProcess process, Granularity granularity) {
        super(name, process, granularity);
    }

    @Override
    public ARISeEPCModel start() {
        setupNotation(NOTATION);
        return this;
    }

    public static ARISeEPCModel createModel(String name, GenericProcess process) {
        return new ARISeEPCModel(name, process);
    }

    public static ARISeEPCModel createModel(String name, GenericProcess process, Granularity granularity) {
        return new ARISeEPCModel(name, process, granularity);
    }
}
