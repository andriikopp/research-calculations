package main.java.edu.kopp.phd.repository.domain.model;

import main.java.edu.kopp.phd.repository.domain.granularity.api.Granularity;
import main.java.edu.kopp.phd.repository.domain.process.GenericProcess;
import org.apache.jena.rdf.model.Resource;

/**
 * Description of the control flow of a business process model.
 *
 * @author Andrii Kopp
 */
public abstract class ControlFlowModel extends GenericModel {

    protected ControlFlowModel(String name, GenericProcess process) {
        super(name, process);
    }

    protected ControlFlowModel(String name, GenericProcess process, Granularity granularity) {
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
