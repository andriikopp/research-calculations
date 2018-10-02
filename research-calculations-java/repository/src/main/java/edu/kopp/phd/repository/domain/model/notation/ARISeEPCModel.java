package main.java.edu.kopp.phd.repository.domain.model.notation;

import main.java.edu.kopp.phd.repository.domain.model.BusinessModel;

/**
 * ARIS eEPC business process model representation.
 */
public class ARISeEPCModel extends BusinessModel {

    private ARISeEPCModel(String name, String process) {
        super(name, process);
    }

    private ARISeEPCModel(String name, String process, String granularity) {
        super(name, process, granularity);
    }

    public static ARISeEPCModel createModel(String name, String process) {
        return new ARISeEPCModel(name, process);
    }

    public static ARISeEPCModel createModel(String name, String process, String granularity) {
        return new ARISeEPCModel(name, process, granularity);
    }
}
