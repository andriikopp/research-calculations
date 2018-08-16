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

            System.out.printf("%s\t%d\t%d\n", model.getName(), size, evaluation);
        }
    }
}
