package edu.bpmanalysis.web.model.bean;

import java.util.List;

public class ProcessModelGraphBean {
    private List<ProcessModelGraphNodeBean> nodes;
    private List<ProcessModelGraphEdgeBean> edges;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessModelGraphBean that = (ProcessModelGraphBean) o;

        if (nodes != null ? !nodes.equals(that.nodes) : that.nodes != null) return false;
        return edges != null ? edges.equals(that.edges) : that.edges == null;
    }

    @Override
    public int hashCode() {
        int result = nodes != null ? nodes.hashCode() : 0;
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessModelGraphBean{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    public List<ProcessModelGraphNodeBean> getNodes() {
        return nodes;
    }

    public void setNodes(List<ProcessModelGraphNodeBean> nodes) {
        this.nodes = nodes;
    }

    public List<ProcessModelGraphEdgeBean> getEdges() {
        return edges;
    }

    public void setEdges(List<ProcessModelGraphEdgeBean> edges) {
        this.edges = edges;
    }
}
