package edu.bpmanalysis.etl.beans;

public class StoredProcessModelBean {
    private String id;
    private String modelId;
    private int typeId;
    private String label;

    private int inputArcs;
    private int controlArcs;
    private int outputArcs;
    private int mechanismArcs;

    private int connectorArcsChanges;

    private int inputArcsChanges;
    private int controlArcsChanges;
    private int outputArcsChanges;
    private int mechanismArcsChanges;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoredProcessModelBean that = (StoredProcessModelBean) o;

        if (typeId != that.typeId) return false;
        if (inputArcs != that.inputArcs) return false;
        if (controlArcs != that.controlArcs) return false;
        if (outputArcs != that.outputArcs) return false;
        if (mechanismArcs != that.mechanismArcs) return false;
        if (connectorArcsChanges != that.connectorArcsChanges) return false;
        if (inputArcsChanges != that.inputArcsChanges) return false;
        if (controlArcsChanges != that.controlArcsChanges) return false;
        if (outputArcsChanges != that.outputArcsChanges) return false;
        if (mechanismArcsChanges != that.mechanismArcsChanges) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (modelId != null ? !modelId.equals(that.modelId) : that.modelId != null) return false;
        return label != null ? label.equals(that.label) : that.label == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (modelId != null ? modelId.hashCode() : 0);
        result = 31 * result + typeId;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + inputArcs;
        result = 31 * result + controlArcs;
        result = 31 * result + outputArcs;
        result = 31 * result + mechanismArcs;
        result = 31 * result + connectorArcsChanges;
        result = 31 * result + inputArcsChanges;
        result = 31 * result + controlArcsChanges;
        result = 31 * result + outputArcsChanges;
        result = 31 * result + mechanismArcsChanges;
        return result;
    }

    @Override
    public String toString() {
        return "StoredProcessModelBean{" +
                "id='" + id + '\'' +
                ", modelId='" + modelId + '\'' +
                ", typeId=" + typeId +
                ", label='" + label + '\'' +
                ", inputArcs=" + inputArcs +
                ", controlArcs=" + controlArcs +
                ", outputArcs=" + outputArcs +
                ", mechanismArcs=" + mechanismArcs +
                ", connectorArcsChanges=" + connectorArcsChanges +
                ", inputArcsChanges=" + inputArcsChanges +
                ", controlArcsChanges=" + controlArcsChanges +
                ", outputArcsChanges=" + outputArcsChanges +
                ", mechanismArcsChanges=" + mechanismArcsChanges +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getInputArcs() {
        return inputArcs;
    }

    public void setInputArcs(int inputArcs) {
        this.inputArcs = inputArcs;
    }

    public int getControlArcs() {
        return controlArcs;
    }

    public void setControlArcs(int controlArcs) {
        this.controlArcs = controlArcs;
    }

    public int getOutputArcs() {
        return outputArcs;
    }

    public void setOutputArcs(int outputArcs) {
        this.outputArcs = outputArcs;
    }

    public int getMechanismArcs() {
        return mechanismArcs;
    }

    public void setMechanismArcs(int mechanismArcs) {
        this.mechanismArcs = mechanismArcs;
    }

    public int getConnectorArcsChanges() {
        return connectorArcsChanges;
    }

    public void setConnectorArcsChanges(int connectorArcsChanges) {
        this.connectorArcsChanges = connectorArcsChanges;
    }

    public int getInputArcsChanges() {
        return inputArcsChanges;
    }

    public void setInputArcsChanges(int inputArcsChanges) {
        this.inputArcsChanges = inputArcsChanges;
    }

    public int getControlArcsChanges() {
        return controlArcsChanges;
    }

    public void setControlArcsChanges(int controlArcsChanges) {
        this.controlArcsChanges = controlArcsChanges;
    }

    public int getOutputArcsChanges() {
        return outputArcsChanges;
    }

    public void setOutputArcsChanges(int outputArcsChanges) {
        this.outputArcsChanges = outputArcsChanges;
    }

    public int getMechanismArcsChanges() {
        return mechanismArcsChanges;
    }

    public void setMechanismArcsChanges(int mechanismArcsChanges) {
        this.mechanismArcsChanges = mechanismArcsChanges;
    }
}
