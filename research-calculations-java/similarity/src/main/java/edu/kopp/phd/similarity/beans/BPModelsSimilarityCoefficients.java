package main.java.edu.kopp.phd.similarity.beans;

public class BPModelsSimilarityCoefficients {
    private double resourceIdentifiers;
    private double flowObjects;
    private double organizationalUnits;
    private double applicationSystems;
    private double inputsAndOutputs;
    private double regulations;

    public BPModelsSimilarityCoefficients() {
    }

    public BPModelsSimilarityCoefficients(double resourceIdentifiers, double flowObjects, double organizationalUnits, double applicationSystems, double inputsAndOutputs, double regulations) {
        this.resourceIdentifiers = resourceIdentifiers;
        this.flowObjects = flowObjects;
        this.organizationalUnits = organizationalUnits;
        this.applicationSystems = applicationSystems;
        this.inputsAndOutputs = inputsAndOutputs;
        this.regulations = regulations;
    }

    public double getResourceIdentifiers() {
        return resourceIdentifiers;
    }

    public void setResourceIdentifiers(double resourceIdentifiers) {
        this.resourceIdentifiers = resourceIdentifiers;
    }

    public double getFlowObjects() {
        return flowObjects;
    }

    public void setFlowObjects(double flowObjects) {
        this.flowObjects = flowObjects;
    }

    public double getOrganizationalUnits() {
        return organizationalUnits;
    }

    public void setOrganizationalUnits(double organizationalUnits) {
        this.organizationalUnits = organizationalUnits;
    }

    public double getApplicationSystems() {
        return applicationSystems;
    }

    public void setApplicationSystems(double applicationSystems) {
        this.applicationSystems = applicationSystems;
    }

    public double getInputsAndOutputs() {
        return inputsAndOutputs;
    }

    public void setInputsAndOutputs(double inputsAndOutputs) {
        this.inputsAndOutputs = inputsAndOutputs;
    }

    public double getRegulations() {
        return regulations;
    }

    public void setRegulations(double regulations) {
        this.regulations = regulations;
    }
}
