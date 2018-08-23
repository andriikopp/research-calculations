package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.*;

public class ModelBuilder {
    private Model model;

    public static ModelBuilder model(String name) {
        ModelBuilder builder = new ModelBuilder();

        builder.setModel(new Model(name));

        return builder;
    }

    public ModelBuilder event(int pre, int sub) {
        model.getNodes().add(new Event(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder event(String label, int pre, int sub) {
        model.getNodes().add(new Event(label, pre, sub));

        return this;
    }

    public ModelBuilder event(String label, int pre, int sub, boolean sequence, boolean decision) {
        model.getNodes().add(new Event(label, pre, sub, sequence, decision));

        return this;
    }

    public ModelBuilder function(int pre, int sub) {
        model.getNodes().add(new Function(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder function(String label, int pre, int sub) {
        model.getNodes().add(new Function(label, pre, sub));

        return this;
    }

    public ModelBuilder function(String label, int pre, int sub, int org, int in, int out, int app) {
        model.getNodes().add(new Function(label, pre, sub, org, in, out, app));

        return this;
    }

    public ModelBuilder pinterface(int pre, int sub) {
        model.getNodes().add(new ProcessInterface(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder pinterface(String label, int pre, int sub) {
        model.getNodes().add(new ProcessInterface(label, pre, sub));

        return this;
    }

    public ModelBuilder and(String label, int pre, int sub) {
        model.getNodes().add(new Connector(label, pre, sub, Connector.AND));

        return this;
    }

    public ModelBuilder or(String label, int pre, int sub) {
        model.getNodes().add(new Connector(label, pre, sub, Connector.OR));

        return this;
    }

    public ModelBuilder xor(String label, int pre, int sub) {
        model.getNodes().add(new Connector(label, pre, sub, Connector.XOR));

        return this;
    }

    public ModelBuilder and(int pre, int sub) {
        model.getNodes().add(new Connector(pre, sub, Connector.AND));

        return this;
    }

    public ModelBuilder or(int pre, int sub) {
        model.getNodes().add(new Connector(pre, sub, Connector.OR));

        return this;
    }

    public ModelBuilder xor(int pre, int sub) {
        model.getNodes().add(new Connector(pre, sub, Connector.XOR));

        return this;
    }

    public ModelBuilder dstore(int pre, int sub) {
        model.getNodes().add(new DataStore(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder dstore(String label, int pre, int sub) {
        model.getNodes().add(new DataStore(label, pre, sub));

        return this;
    }

    public ModelBuilder xentity(int pre, int sub) {
        model.getNodes().add(new ExternalEntity(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder xentity(String label, int pre, int sub) {
        model.getNodes().add(new ExternalEntity(label, pre, sub));

        return this;
    }

    public ModelBuilder dflow(boolean fromActivity, boolean toActivity) {
        int from = fromActivity ? 1 : 0;
        int to = toActivity ? 1 : 0;

        model.getNodes().add(new DataFlow(model.nextNodeLabel(), from, to));

        return this;
    }

    public ModelBuilder dflow(String label, boolean fromActivity, boolean toActivity) {
        int from = fromActivity ? 1 : 0;
        int to = toActivity ? 1 : 0;

        model.getNodes().add(new DataFlow(label, from, to));

        return this;
    }

    public Model finish() {
        return model;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
