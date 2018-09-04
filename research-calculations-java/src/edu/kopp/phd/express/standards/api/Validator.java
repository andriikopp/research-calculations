package edu.kopp.phd.express.standards.api;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.*;

public interface Validator {

    double validate(Model model);

    default void ignoreRegulations() { }

    default int sgn(int value) {
        return value > 0 ? 1 : 0;
    }

    default int countFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                node instanceof Function).count();
    }

    default int countEvents(Model model) {
        return (int) model.getNodes().stream().filter(node -> node instanceof Event).count();
    }

    default int countProcessInterfaces(Model model) {
        return (int) model.getNodes().stream().filter(node -> node instanceof ProcessInterface).count();
    }

    default int countConnectors(Model model) {
        return (int) model.getNodes().stream().filter(node -> node instanceof Connector).count();
    }

    default int countDataStores(Model model) {
        return (int) model.getNodes().stream().filter(node -> node instanceof DataStore).count();
    }

    default int countExternalEntities(Model model) {
        return (int) model.getNodes().stream().filter(node -> node instanceof ExternalEntity).count();
    }

    default int countStartNodes(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Event || node instanceof ProcessInterface) && (node.getPreceding() == 0)).count();
    }

    default int countEndNodes(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Event || node instanceof ProcessInterface) && (node.getSubsequent() == 0)).count();
    }

    default int countValidEvents(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Event) && (node.getPreceding() <= 1 && node.getSubsequent() <= 1)).count();
    }

    default int countValidFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (node.getPreceding() == 1 && node.getSubsequent() == 1)).count();
    }

    default int countValidConnectors(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Connector) && (
                        (node.getPreceding() == 1 && node.getSubsequent() > 1) ||
                                (node.getPreceding() > 1 && node.getSubsequent() == 1)
                )).count();
    }

    default int countValidProcessInterfaces(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof ProcessInterface) && (
                        (node.getPreceding() == 1 && node.getSubsequent() == 0) ||
                                (node.getPreceding() == 0 && node.getSubsequent() == 1)
                )).count();
    }

    default int countEventSequences(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Event) && ((Event) node).isPrecedesEvent()).count();
    }

    default int countEventDecisions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Event) && ((Event) node).isMakesDecision()).count();
    }

    default int countValidDFFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && (((Function) node).getPreceding() >= 1 &&
                        ((Function) node).getSubsequent() >= 1)).count();
    }

    default int countValidDataStores(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof DataStore) && (((DataStore) node).getPreceding() >= 1 &&
                        ((DataStore) node).getSubsequent() >= 1)).count();
    }

    default int countValidExternalEntities(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof ExternalEntity) &&
                        (((ExternalEntity) node).getPreceding() >= 1 ||
                                ((ExternalEntity) node).getSubsequent() >= 1)).count();
    }

    default int countInvalidDataFlows(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof DataFlow) && (((DataFlow) node).getPreceding() != 1 &&
                        ((DataFlow) node).getSubsequent() != 1)).count();
    }

    default int countValidOrgFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && ((Function) node).getOrganizationalUnits() == 1).count();
    }

    default int countValidInFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && ((Function) node).getInputs() >= 1).count();
    }

    default int countValidOutFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && ((Function) node).getOutputs() >= 1).count();
    }

    default int countValidRegFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && ((Function) node).getRegulations() >= 1).count();
    }

    default int countValidAppFunctions(Model model) {
        return (int) model.getNodes().stream().filter(node ->
                (node instanceof Function) && ((Function) node).getApplicationSystems() == 1).count();
    }
}
