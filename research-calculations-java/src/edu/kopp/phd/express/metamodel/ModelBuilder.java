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
        model.getNodes().add(new Event(pre, sub));

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

    public ModelBuilder function(String label, int pre, int sub) {
        model.getNodes().add(new Function(label, pre, sub));

        return this;
    }

    public ModelBuilder function(String label, int pre, int sub, int org, int in, int out, int app) {
        model.getNodes().add(new Function(label, pre, sub, org, in, out, app));

        return this;
    }

    public ModelBuilder function(String label, int pre, int sub, int in, int out) {
        model.getNodes().add(new Function(label, pre, sub, in, out));

        return this;
    }

    public ModelBuilder processInterface(String label, int pre, int sub) {
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

    public ModelBuilder dataStore(String label, int in, int out) {
        model.getNodes().add(new DataStore(label, in, out));

        return this;
    }

    public ModelBuilder externalEntity(String label, int pre, int sub) {
        model.getNodes().add(new ExternalEntity(label, pre, sub));

        return this;
    }

    public ModelBuilder dataFlow(String label, boolean connectsStoreEntity) {
        model.getNodes().add(new DataFlow(label, connectsStoreEntity));

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
