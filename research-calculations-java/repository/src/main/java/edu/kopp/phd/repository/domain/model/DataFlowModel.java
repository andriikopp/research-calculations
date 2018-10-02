package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.Resource;

/**
 * Description of the data flow of a business process model.
 *
 * @author Andrii Kopp
 */
public abstract class DataFlowModel extends GenericModel {

    protected DataFlowModel(String name, String process) {
        super(name, process);
    }

    protected DataFlowModel(String name, String process, String granularity) {
        super(name, process, granularity);
    }

    public DataFlowModel createInput(Resource node, Resource input) {
        getStatements().add(getStatements().createStatement(node, requires, input));
        return this;
    }

    public DataFlowModel createOutput(Resource node, Resource output) {
        getStatements().add(getStatements().createStatement(node, produces, output));
        return this;
    }

    public DataFlowModel createInput(Resource node, Resource... inputs) {
        for (Resource resource : inputs) {
            getStatements().add(getStatements().createStatement(node, requires, resource));
        }

        return this;
    }

    public DataFlowModel createOutput(Resource node, Resource... outputs) {
        for (Resource resource : outputs) {
            getStatements().add(getStatements().createStatement(node, produces, resource));
        }

        return this;
    }
}
