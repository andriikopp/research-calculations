package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.Resource;

/**
 * Describes the operational environment of a business process model.
 *
 * @author Andrii Kopp
 */
public class BusinessModel extends GenericModel {

    public BusinessModel(String name) {
        super(name);
    }

    public BusinessModel(String name, String granularity) {
        super(name, granularity);
    }

    public void createSequenceFlow(Resource predecessor, Resource node) {
        getStatements().createStatement(predecessor, isPredecessorOf, node);
    }

    public void createSequenceFlow(Resource predecessor, Resource... nodes) {
        for (Resource resource : nodes) {
            createSequenceFlow(predecessor, resource);
        }
    }

    public void createInput(Resource node, Resource input) {
        getStatements().createStatement(node, requires, input);
    }

    public void createOutput(Resource node, Resource output) {
        getStatements().createStatement(node, produces, output);
    }

    public void createInput(Resource node, Resource... inputs) {
        for (Resource resource : inputs) {
            createInput(node, resource);
        }
    }

    public void createOutput(Resource node, Resource... outputs) {
        for (Resource resource : outputs) {
            createOutput(node, resource);
        }
    }

    public void createResponsible(Resource function, Resource unit) {
        getStatements().createStatement(function, isPerformedBy, unit);
    }

    public void createSupport(Resource function, Resource system) {
        getStatements().createStatement(function, isSupportedBy, system);
    }

    public void createRule(Resource function, Resource regulation) {
        getStatements().createStatement(function, isRegulatedBy, regulation);
    }

    public void createResponsible(Resource function, Resource... units) {
        for (Resource resource : units) {
            createResponsible(function, resource);
        }
    }

    public void createSupport(Resource function, Resource... systems) {
        for (Resource resource : systems) {
            createSupport(function, resource);
        }
    }

    public void createRule(Resource function, Resource... regulations) {
        for (Resource resource : regulations) {
            createRule(function, resource);
        }
    }
}
