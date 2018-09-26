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

    @Deprecated
    public ModelBuilder event(String label, int pre, int sub, boolean sequence, boolean decision) {
        model.getNodes().add(new Event(label, pre, sub, sequence, decision));

        return this;
    }

    public ModelBuilder eventMakesDecision(String label, int pre, int sub) {
        model.getNodes().add(new Event(label, pre, sub, false, true));

        return this;
    }

    public ModelBuilder eventMakesDecision(int pre, int sub) {
        model.getNodes().add(new Event(model.nextNodeLabel(), pre, sub, false, true));

        return this;
    }

    public ModelBuilder eventPrecedesEvent(String label, int pre, int sub) {
        model.getNodes().add(new Event(label, pre, sub, true, false));

        return this;
    }

    public ModelBuilder eventPrecedesEvent(int pre, int sub) {
        model.getNodes().add(new Event(model.nextNodeLabel(), pre, sub, true, false));

        return this;
    }

    @Deprecated
    public ModelBuilder event(int pre, int sub, boolean sequence, boolean decision) {
        model.getNodes().add(new Event(model.nextNodeLabel(), pre, sub, sequence, decision));

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

    public ModelBuilder function(int pre, int sub, ProcessEnvironment environment) {
        model.getNodes().add(new Function(model.nextNodeLabel(), pre, sub,
                environment.getOrganizationalUnits(),
                environment.getInputs(),
                environment.getRegulations(),
                environment.getOutputs(),
                environment.getApplicationSystems()));

        return this;
    }

    public ModelBuilder function(String label, int pre, int sub, ProcessEnvironment environment) {
        model.getNodes().add(new Function(label, pre, sub,
                environment.getOrganizationalUnits(),
                environment.getInputs(),
                environment.getRegulations(),
                environment.getOutputs(),
                environment.getApplicationSystems()));

        return this;
    }

    public static ProcessEnvironment environment(int org, int in, int reg, int out, int app) {
        return new ProcessEnvironment(org, in, reg, out, app);
    }

    @Deprecated
    public ModelBuilder function(String label, int pre, int sub, int org, int in, int reg, int out, int app) {
        model.getNodes().add(new Function(label, pre, sub, org, in, reg, out, app));

        return this;
    }

    @Deprecated
    public ModelBuilder function(int pre, int sub, int org, int in, int reg, int out, int app) {
        model.getNodes().add(new Function(model.nextNodeLabel(), pre, sub, org, in, reg, out, app));

        return this;
    }

    public ModelBuilder processInterface(int pre, int sub) {
        model.getNodes().add(new ProcessInterface(model.nextNodeLabel(), pre, sub));

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

    public ModelBuilder dataStore(int pre, int sub) {
        model.getNodes().add(new DataStore(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder dataStore(String label, int pre, int sub) {
        model.getNodes().add(new DataStore(label, pre, sub));

        return this;
    }

    public ModelBuilder externalEntity(int pre, int sub) {
        model.getNodes().add(new ExternalEntity(model.nextNodeLabel(), pre, sub));

        return this;
    }

    public ModelBuilder externalEntity(String label, int pre, int sub) {
        model.getNodes().add(new ExternalEntity(label, pre, sub));

        return this;
    }

    public ModelBuilder dataFlowFromActivity() {
        model.getNodes().add(new DataFlow(model.nextNodeLabel(), 1, 0));

        return this;
    }

    public ModelBuilder dataFlowToActivity() {
        model.getNodes().add(new DataFlow(model.nextNodeLabel(), 0, 1));

        return this;
    }

    public ModelBuilder dataFlowBetweenActivities() {
        model.getNodes().add(new DataFlow(model.nextNodeLabel(), 1, 1));

        return this;
    }

    public ModelBuilder dataFlowBetweenOtherEntities() {
        model.getNodes().add(new DataFlow(model.nextNodeLabel(), 0, 0));

        return this;
    }

    public ModelBuilder dataFlowFromActivity(String label) {
        model.getNodes().add(new DataFlow(label, 1, 0));

        return this;
    }

    public ModelBuilder dataFlowToActivity(String label) {
        model.getNodes().add(new DataFlow(label, 0, 1));

        return this;
    }

    public ModelBuilder dataFlowBetweenActivities(String label) {
        model.getNodes().add(new DataFlow(label, 1, 1));

        return this;
    }

    public ModelBuilder dataFlowBetweenOtherEntities(String label) {
        model.getNodes().add(new DataFlow(label, 0, 0));

        return this;
    }

    @Deprecated
    public ModelBuilder dataFlow(boolean fromActivity, boolean toActivity) {
        int from = fromActivity ? 1 : 0;
        int to = toActivity ? 1 : 0;

        model.getNodes().add(new DataFlow(model.nextNodeLabel(), from, to));

        return this;
    }

    @Deprecated
    public ModelBuilder dataFlow(String label, boolean fromActivity, boolean toActivity) {
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
