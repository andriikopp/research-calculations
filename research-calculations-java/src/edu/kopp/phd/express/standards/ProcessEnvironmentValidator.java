package edu.kopp.phd.express.standards;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.Function;
import edu.kopp.phd.express.metamodel.entity.Node;
import edu.kopp.phd.express.standards.api.Validator;

public class ProcessEnvironmentValidator implements Validator {

    @Override
    public int validate(Model model) {
        int result = 0;

        for (Node node : model.getNodes())
            if (node instanceof Function) {
                Function function = (Function) node;
                result += Math.abs(sgn(function.getOrganizationalUnits() - 1)) +
                        (1 - sgn(function.getInputs())) +
                        (1 - sgn(function.getOutputs())) +
                        Math.abs(sgn(function.getApplicationSystems() - 1));
            }

        return result;
    }
}
