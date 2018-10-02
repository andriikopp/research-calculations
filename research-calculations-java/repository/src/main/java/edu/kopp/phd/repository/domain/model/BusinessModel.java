package main.java.edu.kopp.phd.repository.domain.model;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;
import main.java.edu.kopp.phd.repository.domain.process.GenericProcess;
import org.apache.jena.rdf.model.Resource;

/**
 * Describes the operational environment of a business process model.
 *
 * @author Andrii Kopp
 */
public abstract class BusinessModel extends GenericModel {

    protected BusinessModel(String name, GenericProcess process) {
        super(name, process);
    }

    protected BusinessModel(String name, GenericProcess process, Granularity granularity) {
        super(name, process, granularity);
    }

    public BusinessModel createSequenceFlow(Resource predecessor, Resource node) {
        getStatements().add(getStatements().createStatement(predecessor, isPredecessorOf, node));
        return this;
    }

    public BusinessModel createSequenceFlow(Resource predecessor, Resource... nodes) {
        for (Resource resource : nodes) {
            getStatements().add(getStatements().createStatement(predecessor, isPredecessorOf, resource));
        }

        return this;
    }

    public BusinessModel createInput(Resource node, Resource input) {
        getStatements().add(getStatements().createStatement(node, requires, input));
        return this;
    }

    public BusinessModel createOutput(Resource node, Resource output) {
        getStatements().add(getStatements().createStatement(node, produces, output));
        return this;
    }

    public BusinessModel createInput(Resource node, Resource... inputs) {
        for (Resource resource : inputs) {
            getStatements().add(getStatements().createStatement(node, requires, resource));
        }

        return this;
    }

    public BusinessModel createOutput(Resource node, Resource... outputs) {
        for (Resource resource : outputs) {
            getStatements().add(getStatements().createStatement(node, produces, resource));
        }

        return this;
    }

    public BusinessModel createResponsible(Resource function, Resource unit) {
        getStatements().add(getStatements().createStatement(function, isPerformedBy, unit));
        return this;
    }

    public BusinessModel createSupport(Resource function, Resource system) {
        getStatements().add(getStatements().createStatement(function, isSupportedBy, system));
        return this;
    }

    public BusinessModel createRule(Resource function, Resource regulation) {
        getStatements().add(getStatements().createStatement(function, isRegulatedBy, regulation));
        return this;
    }

    public BusinessModel createResponsible(Resource function, Resource... units) {
        for (Resource resource : units) {
            getStatements().add(getStatements().createStatement(function, isPerformedBy, resource));
        }

        return this;
    }

    public BusinessModel createSupport(Resource function, Resource... systems) {
        for (Resource resource : systems) {
            getStatements().add(getStatements().createStatement(function, isSupportedBy, resource));
        }

        return this;
    }

    public BusinessModel createRule(Resource function, Resource... regulations) {
        for (Resource resource : regulations) {
            getStatements().add(getStatements().createStatement(function, isRegulatedBy, resource));
        }

        return this;
    }
}
