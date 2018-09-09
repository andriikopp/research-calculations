package edu.kopp.phd.express.governance.plan;

import edu.kopp.phd.express.metamodel.Model;

public class ModelFeatures {
    private Model model;
    private int[] features;

    public ModelFeatures(Model model, int[] features) {
        this.model = model;
        this.features = features;
    }

    public int[] getBinaryFeatures() {
        int[] binary = new int[features.length];

        for (int i = 0; i < features.length; i++) {
            binary[i] = features[i] > 0 ? 1 : 0;
        }

        return binary;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int[] getFeatures() {
        return features;
    }

    public void setFeatures(int[] features) {
        this.features = features;
    }
}
