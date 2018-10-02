package main.java.edu.kopp.phd.repository.domain.model.notation;

import main.java.edu.kopp.phd.repository.domain.model.BusinessModel;

/**
 * IDEF0 business process model representation.
 */
public class IDEF0Model extends BusinessModel {

    private IDEF0Model(String name, String process) {
        super(name, process);
    }

    private IDEF0Model(String name, String process, String granularity) {
        super(name, process, granularity);
    }

    public static IDEF0Model createModel(String name, String process) {
        return new IDEF0Model(name, process);
    }

    public static IDEF0Model createModel(String name, String process, String granularity) {
        return new IDEF0Model(name, process, granularity);
    }
}
