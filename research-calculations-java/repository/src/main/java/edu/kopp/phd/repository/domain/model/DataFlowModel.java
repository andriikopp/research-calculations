package main.java.edu.kopp.phd.repository.domain.model;

import org.apache.jena.rdf.model.Resource;

/**
 * Description of the data flow of a business process model.
 *
 * @author Andrii Kopp
 */
public class DataFlowModel extends GenericModel {

    public DataFlowModel(String name) {
        super(name);
    }

    public DataFlowModel(String name, String granularity) {
        super(name, granularity);
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

    public void createOutputs(Resource node, Resource... outputs) {
        for (Resource resource : outputs) {
            createOutput(node, resource);
        }
    }
}
