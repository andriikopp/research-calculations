package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.api.NodesSubset;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.metamodel.Node;

import java.util.List;
import java.util.stream.Collectors;

public class NodesSubsetsUtil {

    public static List<Node> getEvents(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.EVENT)))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getStartEvents(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.EVENT) &&
                        (node.getInput() == 0 && node.getOutput() > 0)))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getEndEvents(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.EVENT) &&
                        (node.getInput() > 0 && node.getOutput() == 0)))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getConnectors(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                        node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                        node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getSplitConnectors(Model model, Node.NodeType nodeType) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(nodeType)) &&
                        (node.getInput() == 1 && node.getOutput() > 1))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getJoinConnectors(Model model, Node.NodeType nodeType) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(nodeType)) &&
                        (node.getInput() > 1 && node.getOutput() == 1))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getORRoutingElements(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.OR_CONNECTOR)))
                .collect(Collectors.toList()))
                .getSubset(model);
    }

    public static List<Node> getFunctions(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.FUNCTION)))
                .collect(Collectors.toList()))
                .getSubset(model);
    }
}