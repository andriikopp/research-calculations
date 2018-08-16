package edu.kopp.phd.express.metamodel.entity;

public class Node {
    private String label;
    private int preceding;
    private int subsequent;

    public Node(String label, int preceding, int subsequent) {
        this.label = label;
        this.preceding = preceding;
        this.subsequent = subsequent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (preceding != node.preceding) return false;
        if (subsequent != node.subsequent) return false;
        return label != null ? label.equals(node.label) : node.label == null;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + preceding;
        result = 31 * result + subsequent;
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                ", preceding=" + preceding +
                ", subsequent=" + subsequent +
                '}';
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPreceding() {
        return preceding;
    }

    public void setPreceding(int preceding) {
        this.preceding = preceding;
    }

    public int getSubsequent() {
        return subsequent;
    }

    public void setSubsequent(int subsequent) {
        this.subsequent = subsequent;
    }
}
