package edu.bpmanalysis.description.tools;

import java.util.UUID;

public class Node {
    private String label;

    private int input;
    private int output;
    private int control;
    private int mechanism;

    private NodeType nodeType;

    public enum NodeType {
        FUNCTION, EVENT, XOR_CONNECTOR, OR_CONNECTOR, AND_CONNECTOR,
        DATA_STORE, EXTERNAL_ENTITY,
        INTERFACE_ARC_INPUT, INTERFACE_ARC_OUTPUT, INTERFACE_ARC_CONTROL, INTERFACE_ARC_MECHANISM
    }

    public interface ArcType {
        int get(Node node);
    }

    public static ArcType[] arcTypes = {
            node -> node.getInput(),
            node -> node.getOutput(),
            node -> node.getControl(),
            node -> node.getMechanism()
    };

    private Node(String label, int input, int output, int control, int mechanism, NodeType nodeType) {
        if (label == null) {
            label = UUID.randomUUID().toString();
        }

        this.label = label;
        this.input = input;
        this.output = output;
        this.control = control;
        this.mechanism = mechanism;
        this.nodeType = nodeType;
    }

    public static Node createFunction(int input, int output, int control, int mechanism) {
        return new Node(null, input, output, control, mechanism, NodeType.FUNCTION);
    }

    public static Node createEvent(int input, int output) {
        return new Node(null, input, output, 0, 0, NodeType.EVENT);
    }

    public static Node createXORConnector(int input, int output) {
        return new Node(null, input, output, 0, 0, NodeType.XOR_CONNECTOR);
    }

    public static Node createORConnector(int input, int output) {
        return new Node(null, input, output, 0, 0, NodeType.OR_CONNECTOR);
    }

    public static Node createANDConnector(int input, int output) {
        return new Node(null, input, output, 0, 0, NodeType.AND_CONNECTOR);
    }

    public static Node createFunction(String name, int input, int output, int control, int mechanism) {
        return new Node(name, input, output, control, mechanism, NodeType.FUNCTION);
    }

    public static Node createEvent(String name, int input, int output) {
        return new Node(name, input, output, 0, 0, NodeType.EVENT);
    }

    public static Node createXORConnector(String name, int input, int output) {
        return new Node(name, input, output, 0, 0, NodeType.XOR_CONNECTOR);
    }

    public static Node createORConnector(String name, int input, int output) {
        return new Node(name, input, output, 0, 0, NodeType.OR_CONNECTOR);
    }

    public static Node createANDConnector(String name, int input, int output) {
        return new Node(name, input, output, 0, 0, NodeType.AND_CONNECTOR);
    }

    public static Node createDataStore(int input, int output) {
        return new Node(null, input, output, 0, 0, NodeType.DATA_STORE);
    }

    public static Node createDataStore(String name, int input, int output) {
        return new Node(name, input, output, 0, 0, NodeType.DATA_STORE);
    }

    public static Node createExternalEntity(int input, int output) {
        return new Node(null, input, output, 0, 0, NodeType.EXTERNAL_ENTITY);
    }

    public static Node createExternalEntity(String name, int input, int output) {
        return new Node(name, input, output, 0, 0, NodeType.EXTERNAL_ENTITY);
    }

    public static Node createInterfaceArcInput(int output) {
        return new Node(null, 0, output, 0, 0, NodeType.INTERFACE_ARC_INPUT);
    }

    public static Node createInterfaceArcInput(String name, int output) {
        return new Node(name, 0, output, 0, 0, NodeType.INTERFACE_ARC_INPUT);
    }

    public static Node createInterfaceArcOutput(int input) {
        return new Node(null, input, 0, 0, 0, NodeType.INTERFACE_ARC_OUTPUT);
    }

    public static Node createInterfaceArcOutput(String name, int input) {
        return new Node(name, input, 0, 0, 0, NodeType.INTERFACE_ARC_OUTPUT);
    }

    public static Node createInterfaceArcControl(int output) {
        return new Node(null, 0, output, 0, 0, NodeType.INTERFACE_ARC_CONTROL);
    }

    public static Node createInterfaceArcControl(String name, int output) {
        return new Node(name, 0, output, 0, 0, NodeType.INTERFACE_ARC_CONTROL);
    }

    public static Node createInterfaceArcMechanism(int output) {
        return new Node(null, 0, output, 0, 0, NodeType.INTERFACE_ARC_MECHANISM);
    }

    public static Node createInterfaceArcMechanism(String name, int output) {
        return new Node(name, 0, output, 0, 0, NodeType.INTERFACE_ARC_MECHANISM);
    }

    public String getLabel() {
        return label;
    }

    public int getInput() {
        return input;
    }

    public int getOutput() {
        return output;
    }

    public int getControl() {
        return control;
    }

    public int getMechanism() {
        return mechanism;
    }

    public double[] getArcs() {
        return new double[]{input, control, output, mechanism};
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getDescription() {
        return String.format("<%s,%d,%d,%d,%d>",
                nodeType.toString(),
                input, control, output, mechanism);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (input != node.input) return false;
        if (output != node.output) return false;
        if (control != node.control) return false;
        if (mechanism != node.mechanism) return false;
        if (label != null ? !label.equals(node.label) : node.label != null) return false;
        return nodeType == node.nodeType;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + input;
        result = 31 * result + output;
        result = 31 * result + control;
        result = 31 * result + mechanism;
        result = 31 * result + (nodeType != null ? nodeType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                ", input=" + input +
                ", output=" + output +
                ", control=" + control +
                ", mechanism=" + mechanism +
                ", nodeType=" + nodeType +
                '}';
    }
}
