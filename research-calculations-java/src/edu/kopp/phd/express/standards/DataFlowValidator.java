package edu.kopp.phd.express.standards;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.api.Validator;

public class DataFlowValidator implements Validator {

    @Override
    public int validate(Model model) {
        return (1 - sgn(countFunctions(model))) +
                (countFunctions(model) - countValidFunctionDataFlows(model)) +
                (countDataStores(model) - countValidDataStoreFlows(model)) +
                (countFunctions(model) - countValidDataFunctions(model)) +
                (countExternalEntities(model) - countValidExternalEntities(model)) +
                countStoreEntityDataFlows(model);
    }
}
