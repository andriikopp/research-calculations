package edu.kopp.phd.express.standards;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.api.Validator;

public class ProcessFlowValidator implements Validator {

    @Override
    public int validate(Model model) {
        return (1 - sgn(countFunctions(model))) +
                (1 - sgn(countStartNodes(model))) + (1 - sgn(countEndNodes(model))) +
                (countEvents(model) - countValidEvents(model)) +
                (countFunctions(model) - countValidFunctions(model)) +
                (countConnectors(model) - countValidConnectors(model)) +
                (countProcessInterfaces(model) - countValidProcessInterfaces(model)) +
                countEventSequences(model) + countEventDecisions(model);
    }
}
