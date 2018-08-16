package edu.kopp.phd.express.metamodel.entity;

public class DataStore extends Node {
    private int functionInputFlow;
    private int functionOutputFlow;

    public DataStore(String label, int preceding, int subsequent) {
        super(label, preceding, subsequent);
    }

    public DataStore(String label, int preceding, int subsequent, int functionInputFlow, int functionOutputFlow) {
        super(label, preceding, subsequent);
        this.functionInputFlow = functionInputFlow;
        this.functionOutputFlow = functionOutputFlow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DataStore dataStore = (DataStore) o;

        if (functionInputFlow != dataStore.functionInputFlow) return false;
        return functionOutputFlow == dataStore.functionOutputFlow;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + functionInputFlow;
        result = 31 * result + functionOutputFlow;
        return result;
    }

    @Override
    public String toString() {
        return "DataStore{" +
                "functionInputFlow=" + functionInputFlow +
                ", functionOutputFlow=" + functionOutputFlow +
                '}';
    }

    public int getFunctionInputFlow() {
        return functionInputFlow;
    }

    public void setFunctionInputFlow(int functionInputFlow) {
        this.functionInputFlow = functionInputFlow;
    }

    public int getFunctionOutputFlow() {
        return functionOutputFlow;
    }

    public void setFunctionOutputFlow(int functionOutputFlow) {
        this.functionOutputFlow = functionOutputFlow;
    }
}
