package edu.kopp.phd.express.metamodel.entity;

public class DataFlow extends Node {
    private boolean connectsStoreEntity;

    public DataFlow(String label, boolean connectsStoreEntity) {
        super(label, 0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DataFlow dataFlow = (DataFlow) o;

        return connectsStoreEntity == dataFlow.connectsStoreEntity;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (connectsStoreEntity ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataFlow{" +
                "connectsStoreEntity=" + connectsStoreEntity +
                '}';
    }

    public boolean isConnectsStoreEntity() {
        return connectsStoreEntity;
    }

    public void setConnectsStoreEntity(boolean connectsStoreEntity) {
        this.connectsStoreEntity = connectsStoreEntity;
    }
}
