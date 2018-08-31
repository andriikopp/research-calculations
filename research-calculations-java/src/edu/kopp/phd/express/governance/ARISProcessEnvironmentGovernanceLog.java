package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.ProcessEnvironmentValidator;
import edu.kopp.phd.express.standards.api.Validator;

public class ARISProcessEnvironmentGovernanceLog extends GovernanceLog {
    private Validator validator;

    public ARISProcessEnvironmentGovernanceLog() {
        this.validator = new ProcessEnvironmentValidator();
    }

    @Override
    public void analyze() {
        validator.ignoreRegulations();

        for (Model model : getLandscape()) {
            int size = model.getNodes().size();
            double evaluation = validator.validate(model);

            int invalidOrg = validator.countFunctions(model) - validator.countValidOrgFunctions(model);
            int invalidIn = validator.countFunctions(model) - validator.countValidInFunctions(model);
            int invalidOut = validator.countFunctions(model) - validator.countValidOutFunctions(model);
            int invalidApp = validator.countFunctions(model) - validator.countValidAppFunctions(model);

            System.out.printf("%s\t%.2f\t%d\t%d\t%d\t%d\n",
                    model.getName(), size, evaluation,
                    invalidOrg, invalidIn, invalidOut, invalidApp);
        }
    }
}
