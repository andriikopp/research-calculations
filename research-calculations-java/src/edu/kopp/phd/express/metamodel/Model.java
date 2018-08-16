package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.Node;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String name;

    private List<Node> nodes;

    public Model(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
