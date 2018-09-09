package edu.kopp.phd.express.governance;

import edu.kopp.phd.express.governance.plan.ModelFeatures;
import edu.kopp.phd.express.governance.plan.similarity.patterns.DFDSearchPatterns;
import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.DataFlowValidator;
import edu.kopp.phd.express.standards.api.Validator;

public class DFDGovernanceLog extends GovernanceLog {
    private Validator validator;

    public DFDGovernanceLog() {
        this.validator = new DataFlowValidator();
    }

    @Override
    public void analyze() {
        for (Model model : getLandscape()) {
            int size = model.getNodes().size();
            double density = model.density();
            double connectivity = model.connectivity();
            double balance = model.balance();

            int evaluation = (int) validator.validate(model);

            int invalidActivities = validator.countFunctions(model) - validator.countValidDFFunctions(model);
            int invalidDataStores = validator.countDataStores(model) - validator.countValidDataStores(model);
            int invalidExternalEntities = validator.countExternalEntities(model) - validator.countValidExternalEntities(model);
            int invalidDataFlows = validator.countInvalidDataFlows(model);

            System.out.printf("%s\t%d\t%.2f\t%.2f\t%.2f\t" +
                            "%d\t" +
                            "%d\t%d\t%d\t%d\n",
                    model.getName(), size, density, connectivity, balance,
                    evaluation,
                    invalidActivities, invalidDataStores, invalidExternalEntities, invalidDataFlows);

            getPlan().getModelFeaturesList().add(new ModelFeatures(model,
                    new int[]{invalidActivities, invalidDataStores, invalidExternalEntities, invalidDataFlows}));
        }

        getPlan().plan(DFDSearchPatterns.INSTANCE);
    }
}
