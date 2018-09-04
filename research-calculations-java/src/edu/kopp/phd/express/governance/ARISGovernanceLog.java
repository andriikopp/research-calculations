package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.Function;
import edu.kopp.phd.express.standards.ProcessEnvironmentValidator;
import edu.kopp.phd.express.standards.api.Validator;

public class ARISGovernanceLog extends ProcessFlowGovernanceLog {
    private Validator validator;

    private boolean isEnvironmentEnabled;

    public ARISGovernanceLog() {
        this.validator = new ProcessEnvironmentValidator();
    }

    @Override
    public void analyze() {
        validator.ignoreRegulations();

        for (Model model : getLandscape()) {
            if (isEnvironmentEnabled) {
                model.enableEnvironment();
            }

            int size = (int) model.countNodes();
            double density = model.density();
            double connectivity = model.connectivity();
            double balance = model.balance();

            double processFlowEvaluation = getValidator().validate(model);
            double processEnvironmentEvaluation = validator.validate(model);

            int missingFunctions = getValidator().countFunctions(model) < 1 ? 1 : 0;
            int missingStartNodes = getValidator().countStartNodes(model) < 1 ? 1 : 0;
            int missingEndNodes = getValidator().countEndNodes(model) < 1 ? 1 : 0;
            int invalidEvents = getValidator().countEvents(model) - getValidator().countValidEvents(model);
            int invalidFunctions = getValidator().countFunctions(model) - getValidator().countValidFunctions(model);
            int invalidConnectors = getValidator().countConnectors(model) - getValidator().countValidConnectors(model);
            int invalidProcessInterfaces = getValidator().countProcessInterfaces(model) - getValidator().countValidProcessInterfaces(model);
            int eventsMakeDecisions = getValidator().countEventDecisions(model);
            int sequencesOfEvents = getValidator().countEventSequences(model);

            int undefinedResponsibilities = countUndefinedResponsibilities(model);
            int organizationalGaps = countOrganizationalGaps(model);
            int undefinedInputs = countUndefinedInputs(model);
            int undefinedOutputs = countUndefinedOutputs(model);
            int redundantInputs = countRedundantInputs(model);
            int redundantOutputs = countRedundantOutputs(model);
            int unautomatedActivities = countUnautomatedActivities(model);
            int informationalGaps = countInformationalGaps(model);

            System.out.printf("%s\t%d\t%.2f\t%.2f\t%.2f\t" +
                            "%.2f\t%.2f\t" +
                            "%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t" +
                            "%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                    model.getName(), size, density, connectivity, balance,
                    processFlowEvaluation, processEnvironmentEvaluation,
                    missingFunctions, missingStartNodes, missingEndNodes, invalidEvents, invalidFunctions, invalidConnectors,
                        invalidProcessInterfaces, eventsMakeDecisions, sequencesOfEvents,
                    undefinedResponsibilities, organizationalGaps, undefinedInputs, undefinedOutputs,
                        redundantInputs, redundantOutputs, unautomatedActivities, informationalGaps);
        }
    }

    @Override
    public GovernanceLog processEnvironment() {
        isEnvironmentEnabled = true;

        return this;
    }

    private int countUndefinedResponsibilities(Model model) {
        return (int) model.getNodes().stream().filter(node ->
            (node instanceof Function) && (((Function) node).getOrganizationalUnits() < 1)
        ).count();
    }

    private int countOrganizationalGaps(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getOrganizationalUnits() > 1)
        ).count();
    }

    private int countUndefinedInputs(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getInputs() < 1)
        ).count();
    }

    private int countUndefinedOutputs(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getOutputs() < 1)
        ).count();
    }

    private int countRedundantInputs(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getInputs() > 1)
        ).count();
    }

    private int countRedundantOutputs(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getOutputs() > 1)
        ).count();
    }

    private int countUnautomatedActivities(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getApplicationSystems() < 1)
        ).count();
    }

    private int countInformationalGaps(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getApplicationSystems() > 1)
        ).count();
    }
}
