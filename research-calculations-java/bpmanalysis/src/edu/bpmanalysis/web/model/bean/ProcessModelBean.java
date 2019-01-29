package edu.bpmanalysis.web.model.bean;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "processModels", schemaVersion = "1.0")
public class ProcessModelBean {
    @Id
    private String id;
    private String timeStamp;
    private String name;
    private String notation;
    private String level;
    private ProcessModelGraphBean graph;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessModelBean that = (ProcessModelBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (timeStamp != null ? !timeStamp.equals(that.timeStamp) : that.timeStamp != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (notation != null ? !notation.equals(that.notation) : that.notation != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (graph != null ? !graph.equals(that.graph) : that.graph != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (notation != null ? notation.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (graph != null ? graph.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessModelBean{" +
                "id='" + id + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", name='" + name + '\'' +
                ", notation='" + notation + '\'' +
                ", level='" + level + '\'' +
                ", graph=" + graph +
                ", description='" + description + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ProcessModelGraphBean getGraph() {
        return graph;
    }

    public void setGraph(ProcessModelGraphBean graph) {
        this.graph = graph;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
