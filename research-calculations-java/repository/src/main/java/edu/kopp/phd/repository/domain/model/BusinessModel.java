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
