package edu.kopp.phd.express.standards;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.api.Validator;

public class DataFlowValidator implements Validator {

    @Override
    public int validate(Model model) {
        return (countFunctions(model) - countValidDFFunctions(model)) +
                (countDataStores(model) - countValidDataStores(model)) +
                (countExternalEntities(model) - countValidExternalEntities(model)) +
                countInvalidDataFlows(model);
    }
}
