package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.governance.plan.GovernancePlan;
import edu.kopp.phd.express.metamodel.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class GovernanceLog {
    private List<Model> landscape;
    private GovernancePlan plan;

    public GovernanceLog() {
        this.landscape = new ArrayList<>();
        this.plan = new GovernancePlan(new ArrayList<>());
    }

    public abstract void analyze();

    public GovernanceLog ignoreRegulations() {
        // STUB
        return this;
    }

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

    public GovernancePlan getPlan() {
        return plan;
    }

    public void setPlan(GovernancePlan plan) {
        this.plan = plan;
    }
}
