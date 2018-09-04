package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.metamodel.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class GovernanceLog {
    private List<Model> landscape;

    public GovernanceLog() {
        this.landscape = new ArrayList<>();
    }

    public abstract void analyze();

    public GovernanceLog processEnvironment() {
        // STUB
        return this;
    }

    public GovernanceLog processFlow() {
        // STUB
        return this;
    }

    public GovernanceLog dataFlow() {
        // STUB
        return this;
    }

    public List<Model> getLandscape() {
        return landscape;
    }

    public void setLandscape(List<Model> landscape) {
        this.landscape = landscape;
    }
}
