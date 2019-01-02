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

    public List<Connector> getConnectors() {
        List<Connector> connectors = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Connector) {
                connectors.add((Connector) node);
            }
        }

        return connectors;
    }

    public List<Connector> getSplitConnectors(String logic) {
        List<Connector> connectors = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Connector) {
                if (((Connector) node).getLogic().equals(logic) &&
                        (node.getPreceding() == 1 && node.getSubsequent() > 1)) {
                    connectors.add((Connector) node);
                }
            }
        }

        return connectors;
    }

    public List<Connector> getJoinConnectors(String logic) {
        List<Connector> connectors = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Connector) {
                if (((Connector) node).getLogic().equals(logic) &&
                        (node.getPreceding() > 1 && node.getSubsequent() == 1)) {
                    connectors.add((Connector) node);
                }
            }
        }

        return connectors;
    }

    public List<Event> getStartEvents() {
        List<Event> events = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Event) {
                if (node.getPreceding() == 0) {
                    events.add((Event) node);
                }
            }
        }

        return events;
    }

    public List<Event> getEndEvents() {
        List<Event> events = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Event) {
                if (node.getSubsequent() == 0) {
                    events.add((Event) node);
                }
            }
        }

        return events;
    }

    public List<Event> getInternalEvents() {
        List<Event> events = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Event) {
                if (node.getPreceding() > 0 && node.getSubsequent() > 0) {
                    events.add((Event) node);
                }
            }
        }

        return events;
    }

    public double complexityXor() {
        double result = 0;

        for (Connector connector : getConnectors()) {
            if (connector.getLogic().equals(Connector.XOR)) {
                result += connector.getSubsequent();
            }
        }

        return result;
    }

    public double complexityOr() {
        double result = 0;

        for (Connector connector : getConnectors()) {
            if (connector.getLogic().equals(Connector.OR)) {
                result += Math.pow(2, connector.getSubsequent()) - 1;
            }
        }

        return result;
    }

    public double complexityAnd() {
        try {
            return getConnectors().stream().filter(connector -> connector.getLogic().equals(Connector.AND)).count();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public double complexitySplitXor() {
        double result = 0;

        for (Connector connector : getConnectors()) {
            if (connector.getLogic().equals(Connector.XOR) &&
                    (connector.getPreceding() == 1 && connector.getSubsequent() > 1)) {
                result += connector.getSubsequent();
            }
        }

        return result;
    }

    public double complexitySplitOr() {
        double result = 0;

        for (Connector connector : getConnectors()) {
            if (connector.getLogic().equals(Connector.OR) &&
                    (connector.getPreceding() == 1 && connector.getSubsequent() > 1)) {
                result += Math.pow(2, connector.getSubsequent()) - 1;
            }
        }

        return result;
    }

    public double complexitySplitAnd() {
        try {
            return getConnectors().stream().filter(connector -> connector.getLogic().equals(Connector.AND) &&
                    (connector.getPreceding() == 1 && connector.getSubsequent() > 1)).count();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public double complexityJoinXor() {
        double result = 0;

        for (Connector connector : getConnectors()) {
            if (connector.getLogic().equals(Connector.XOR) &&
                    (connector.getPreceding() > 1 && connector.getSubsequent() == 1)) {
                result += connector.getSubsequent();
            }
        }

        return result;
    }

    public double complexityJoinOr() {
        double result = 0;

        for (Connector connector : getConnectors()) {
            if (connector.getLogic().equals(Connector.OR) &&
                    (connector.getPreceding() > 1 && connector.getSubsequent() == 1)) {
                result += Math.pow(2, connector.getSubsequent()) - 1;
            }
        }

        return result;
    }

    public double complexityJoinAnd() {
        try {
            return getConnectors().stream().filter(connector -> connector.getLogic().equals(Connector.AND) &&
                    (connector.getPreceding() > 1 && connector.getSubsequent() == 1)).count();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public double splitComplexity() {
        return complexitySplitXor() + complexitySplitOr() + complexitySplitAnd();
    }

    public double joinComplexity() {
        return complexityJoinXor() + complexityJoinOr() + complexityJoinAnd();
    }

    public double numberOfInputs() {
        double count = 0;

        for (Node node : nodes) {
            if (node instanceof Function) {
                count += node.getPreceding();
            }
        }

        return count;
    }

    public double numberOfOutputs() {
        double count = 0;

        for (Node node : nodes) {
            if (node instanceof Function) {
                count += node.getSubsequent();
            }
        }

        return count;
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        for (Node node : nodes) {
            if (node instanceof Event) {
                events.add((Event) node);
            }
        }

        return events;
    }

    public double density() {
        double size = countNodes();
        double arcs = countArcs();

        if (size <= 1) {
            return 0;
        }

        return arcs / (size * (size - 1));
    }

    public double connectivity() {
        double size = countNodes();
        double arcs = countArcs();

        if (size < 1) {
            return 0;
        }

        return arcs / size;
    }

    public double balance() {
        double size = getFunctions().size();

        if (size < 1) {
            return 0;
        }

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
        return nodes.size();
    }

    public double countArcs() {
        return getSumOfArcsRelatedToNodes() / 2.0;
    }

    private double getSumOfArcsRelatedToNodes() {
        double arcs = 0;

        for (Node node : nodes) {
            arcs += node.getPreceding() + node.getSubsequent();
        }

        return arcs;
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
