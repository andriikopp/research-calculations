package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.Function;
import edu.kopp.phd.express.standards.ProcessEnvironmentValidator;
import edu.kopp.phd.express.standards.ProcessFlowValidator;
import edu.kopp.phd.express.standards.api.Validator;

public class ARISGovernanceLog extends GovernanceLog {
    private Validator processEnvironmentValidator;
    private Validator processFlowValidator;

    public ARISGovernanceLog() {
        this.processEnvironmentValidator = new ProcessEnvironmentValidator();
        this.processFlowValidator = new ProcessFlowValidator();
    }

    @Override
    public void analyze() {
        processEnvironmentValidator.ignoreRegulations();

        for (Model model : getLandscape()) {
            int size = model.getNodes().size();
            double density = model.density();
            double connectivity = model.connectivity();
            double balance = model.balance();

            double processFlowEvaluation = processFlowValidator.validate(model);
            double processEnvironmentEvaluation = processEnvironmentValidator.validate(model);

            int missingFunctions = processFlowValidator.countFunctions(model) < 1 ? 1 : 0;
            int missingStartNodes = processFlowValidator.countStartNodes(model) < 1 ? 1 : 0;
            int missingEndNodes = processFlowValidator.countEndNodes(model) < 1 ? 1 : 0;
            int invalidEvents = processFlowValidator.countEvents(model) - processFlowValidator.countValidEvents(model);
            int invalidFunctions = processFlowValidator.countFunctions(model) - processFlowValidator.countValidFunctions(model);
            int invalidConnectors = processFlowValidator.countConnectors(model) - processFlowValidator.countValidConnectors(model);
            int invalidProcessInterfaces = processFlowValidator.countProcessInterfaces(model) - processFlowValidator.countValidProcessInterfaces(model);
            int eventsMakeDecisions = processFlowValidator.countEventDecisions(model);
            int sequencesOfEvents = processFlowValidator.countEventSequences(model);

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
