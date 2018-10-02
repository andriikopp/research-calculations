package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.Resource;

/**
 * Description of the control flow of a business process model.
 *
 * @author Andrii Kopp
 */
public abstract class ControlFlowModel extends GenericModel {

    protected ControlFlowModel(String name, String process) {
        super(name, process);
    }

    protected ControlFlowModel(String name, String process, String granularity) {
        super(name, process, granularity);
    }

    public ControlFlowModel createSequenceFlow(Resource predecessor, Resource node) {
        getStatements().add(getStatements().createStatement(predecessor, isPredecessorOf, node));
        return this;
    }

    public ControlFlowModel createSequenceFlow(Resource predecessor, Resource... nodes) {
        for (Resource resource : nodes) {
            getStatements().add(getStatements().createStatement(predecessor, isPredecessorOf, resource));
        }

        return this;
    }
}
