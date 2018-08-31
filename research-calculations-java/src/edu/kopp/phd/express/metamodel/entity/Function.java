package edu.kopp.phd.express.metamodel.entity;

public class Function extends Node {
    private int organizationalUnits;
    private int inputs;
    private int regulations;
    private int outputs;
    private int applicationSystems;

    public Function(String label, int preceding, int subsequent) {
        super(label, preceding, subsequent);
    }

    public Function(String label, int preceding, int subsequent, int organizationalUnits, int inputs, int regulations, int outputs, int applicationSystems) {
        super(label, preceding, subsequent);
        this.organizationalUnits = organizationalUnits;
        this.inputs = inputs;
        this.regulations = regulations;
        this.outputs = outputs;
        this.applicationSystems = applicationSystems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Function function = (Function) o;

        if (organizationalUnits != function.organizationalUnits) return false;
        if (inputs != function.inputs) return false;
        if (regulations != function.regulations) return false;
        if (outputs != function.outputs) return false;
        return applicationSystems == function.applicationSystems;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + organizationalUnits;
        result = 31 * result + inputs;
        result = 31 * result + regulations;
        result = 31 * result + outputs;
        result = 31 * result + applicationSystems;
        return result;
    }

    @Override
    public String toString() {
        return "Function{" +
                "organizationalUnits=" + organizationalUnits +
                ", inputs=" + inputs +
                ", regulations=" + regulations +
                ", outputs=" + outputs +
                ", applicationSystems=" + applicationSystems +
                '}';
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
