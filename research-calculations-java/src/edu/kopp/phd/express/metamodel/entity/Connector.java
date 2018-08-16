package edu.kopp.phd.express.metamodel.entity;

public class Connector extends Node {
    private String logic;

    public static final String AND = "and";
    public static final String OR = "or";
    public static final String XOR = "xor";

    public Connector(String label, int preceding, int subsequent, String logic) {
        super(label, preceding, subsequent);
        this.logic = logic;
    }

    public Connector(int preceding, int subsequent, String logic) {
        super("", preceding, subsequent);
        this.logic = logic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Connector connector = (Connector) o;

        return logic != null ? logic.equals(connector.logic) : connector.logic == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (logic != null ? logic.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Connector{" +
                "logic='" + logic + '\'' +
                '}';
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }
}
