package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.*;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String name;

    private List<Node> nodes;

    private boolean isEnvironmentEnabled;

    private boolean hasIssues;

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

    public Model issues() {
        hasIssues = true;
        return this;
    }

    public boolean hasIssues() {
        return this.hasIssues;
    }

    public List<Function> getFunctions() {
        List<Function> functions = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Function) {
                functions.add((Function) node);
            }
        }

        return functions;
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

                double arcs = function.getPreceding() + function.getSubsequent();

                if (isEnvironmentEnabled) {
                    arcs += function.getOrganizationalUnits() + function.getInputs() +
                            function.getRegulations() + function.getOutputs() +
                            function.getApplicationSystems();
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
            return nodes.stream().filter(node -> node instanceof Function ||
                node instanceof ProcessInterface ||
                node instanceof DataStore ||
                node instanceof ExternalEntity).count();
        }

        return nodes.size();
    }

    public double countArcs() {
        double arcs = 0;

        for (Node node : nodes) {
            if (isEnvironmentEnabled) {
                if (node instanceof Function) {
                    Function function = (Function) node;

                    arcs += node.getPreceding() + node.getSubsequent();

                    arcs += function.getOrganizationalUnits() + function.getInputs() +
                            function.getRegulations() + function.getOutputs() +
                            function.getApplicationSystems();
                }
            } else {
                arcs += node.getPreceding() + node.getSubsequent();
            }
        }

        return arcs / 2.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (isEnvironmentEnabled != model.isEnvironmentEnabled) return false;
        if (name != null ? !name.equals(model.name) : model.name != null) return false;
        return nodes != null ? nodes.equals(model.nodes) : model.nodes == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        result = 31 * result + (isEnvironmentEnabled ? 1 : 0);
        return result;
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
