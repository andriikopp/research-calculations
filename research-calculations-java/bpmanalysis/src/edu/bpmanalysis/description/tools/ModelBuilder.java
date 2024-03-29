package edu.bpmanalysis.description.tools;

public class ModelBuilder {
    private Model model;

    private ModelBuilder(Model model) {
        this.model = model;
    }

    public static ModelBuilder selectModel(Model model) {
        return new ModelBuilder(model);
    }

    public ModelBuilder addNode(Node node) {
        model.getNodesList().add(node);
        return this;
    }

    public Model finish() {
        return model;
    }
}
