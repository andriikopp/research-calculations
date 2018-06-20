package edu.kopp.phd.model;

import org.apache.jena.rdf.model.Resource;

public class Node {
    private Resource resource;

    public Node(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return resource != null ? resource.equals(node.resource) : node.resource == null;
    }

    @Override
    public int hashCode() {
        return resource != null ? resource.hashCode() : 0;
    }

    @Override
    public String toString() {
        return resource.getLocalName();
    }
}
