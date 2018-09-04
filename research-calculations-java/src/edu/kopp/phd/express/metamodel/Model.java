package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.Function;
import edu.kopp.phd.express.metamodel.entity.Node;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String name;

    private List<Node> nodes;

    private boolean isEnvironmentEnabled;

    public Model(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
    }

    public String nextNodeLabel() {
        return "Node-" + String.valueOf(nodes.size() + 1);
    }

    public void enableEnvironment() {
        isEnvironmentEnabled = true;
    }

    public void ignoreEnvironment() {
        isEnvironmentEnabled = false;
    }

    public double density() {
        double size = countNodes();
        double arcs = countArcs();

        return arcs / (size * (size - 1));
    }

    public double connectivity() {
        double size = countNodes();
        double arcs = countArcs();

        return arcs / size;
    }

    public double balance() {
        double size = countNodes();

        double sum = 0;
        double max = 0;

        for (Node node : nodes) {
            if (node instanceof Function) {
                Function function = (Function) node;

                double arcs = 0;

                if (isEnvironmentEnabled) {
                    arcs = function.getOrganizationalUnits() + function.getInputs() +
                            function.getRegulations() + function.getOutputs() +
                            function.getApplicationSystems();
                } else {
                    arcs = function.getPreceding() + function.getSubsequent();
                }

                if (arcs > max) {
                    max = arcs;
                }

                sum += arcs;
            }
        }

        return Math.abs(sum / size - max);
    }

    public double countNodes() {
        if (isEnvironmentEnabled) {
            return nodes.stream().filter(node -> node instanceof Function).count();
        }

        return nodes.size();
    }

    public double countArcs() {
        double arcs = 0;

        for (Node node : nodes) {
            if (isEnvironmentEnabled && node instanceof Function) {
                Function function = (Function) node;

                arcs += function.getOrganizationalUnits() + function.getInputs() +
                        function.getRegulations() + function.getOutputs() +
                        function.getApplicationSystems();
            } else {
                arcs += node.getPreceding() + node.getSubsequent();
            }
        }

        return arcs;
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
