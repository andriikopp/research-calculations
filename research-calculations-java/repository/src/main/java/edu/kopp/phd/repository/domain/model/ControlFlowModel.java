package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.Resource;

/**
 * Description of the control flow of a business process model.
 *
 * @author Andrii Kopp
 */
public class ControlFlowModel extends GenericModel {

    public ControlFlowModel(String name) {
        super(name);
    }

    public ControlFlowModel(String name, String granularity) {
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
}
