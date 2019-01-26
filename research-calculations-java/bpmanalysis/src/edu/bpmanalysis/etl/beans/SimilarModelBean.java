package edu.bpmanalysis.etl.beans;

public class SimilarModelBean {
    private String id;
    private int analyzedModelId;
    private int storedModelId;

    @Override
    public String toString() {
        return "SimilarModelBean{" +
                "id='" + id + '\'' +
                ", analyzedModelId=" + analyzedModelId +
                ", storedModelId=" + storedModelId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimilarModelBean that = (SimilarModelBean) o;

        if (analyzedModelId != that.analyzedModelId) return false;
        if (storedModelId != that.storedModelId) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + analyzedModelId;
        result = 31 * result + storedModelId;
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAnalyzedModelId() {
        return analyzedModelId;
    }

    public void setAnalyzedModelId(int analyzedModelId) {
        this.analyzedModelId = analyzedModelId;
    }

    public int getStoredModelId() {
        return storedModelId;
    }

    public void setStoredModelId(int storedModelId) {
        this.storedModelId = storedModelId;
    }
}
