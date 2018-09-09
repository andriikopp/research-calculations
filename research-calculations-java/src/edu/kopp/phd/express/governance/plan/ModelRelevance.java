package edu.kopp.phd.express.governance.plan;

import edu.kopp.phd.express.metamodel.Model;
import org.jetbrains.annotations.NotNull;

public class ModelRelevance {
    private Model model;
    private double relevance;

    public ModelRelevance(Model model, double relevance) {
        this.model = model;
        this.relevance = relevance;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    @Override
    public String toString() {
        return String.format("%s\t%.2f", model.getName(), relevance);
    }
}
