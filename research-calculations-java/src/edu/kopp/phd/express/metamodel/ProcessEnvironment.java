package edu.kopp.phd.express.metamodel;

public class ProcessEnvironment {
    private int organizationalUnits;
    private int inputs;
    private int regulations;
    private int outputs;
    private int applicationSystems;

    public ProcessEnvironment(int organizationalUnits, int inputs, int regulations, int outputs, int applicationSystems) {
        this.organizationalUnits = organizationalUnits;
        this.inputs = inputs;
        this.regulations = regulations;
        this.outputs = outputs;
        this.applicationSystems = applicationSystems;
    }

    public int getOrganizationalUnits() {
        return organizationalUnits;
    }

    public void setOrganizationalUnits(int organizationalUnits) {
        this.organizationalUnits = organizationalUnits;
    }

    public int getInputs() {
        return inputs;
    }

    public void setInputs(int inputs) {
        this.inputs = inputs;
    }

    public int getRegulations() {
        return regulations;
    }

    public void setRegulations(int regulations) {
        this.regulations = regulations;
    }

    public int getOutputs() {
        return outputs;
    }

    public void setOutputs(int outputs) {
        this.outputs = outputs;
    }

    public int getApplicationSystems() {
        return applicationSystems;
    }

    public void setApplicationSystems(int applicationSystems) {
        this.applicationSystems = applicationSystems;
    }
}
