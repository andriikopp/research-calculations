package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.ProcessFlowValidator;
import edu.kopp.phd.express.standards.api.Validator;

public class ProcessFlowGovernanceLog extends GovernanceLog {
    private Validator validator;

    public ProcessFlowGovernanceLog() {
        this.validator = new ProcessFlowValidator();
    }

    @Override
    public void analyze() {
        for (Model model : getLandscape()) {
            int size = model.getNodes().size();
            int evaluation = validator.validate(model);

            int missingFunctions = validator.countFunctions(model) < 1 ? 1 : 0;
            int missingStartNodes = validator.countStartNodes(model) < 1 ? 1 : 0;
            int missingEndNodes = validator.countEndNodes(model) < 1 ? 1 : 0;
            int invalidEvents = validator.countEvents(model) - validator.countValidEvents(model);
            int invalidFunctions = validator.countFunctions(model) - validator.countValidFunctions(model);
            int invalidConnectors = validator.countConnectors(model) - validator.countValidConnectors(model);

            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                    model.getName(), size, evaluation,
                    missingFunctions, missingStartNodes, missingEndNodes, invalidEvents, invalidFunctions, invalidConnectors);
        }
    }
}
