package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.Function;

public class RelaxedImprovement {

    public static void improve(Model model, String type) {
        double[] attributes = RelaxedValidation.ATTRIBUTES.get(type);

        boolean isRegulationsConsidered = type.equals("IDEF0");

        System.out.println(model.getName());

        System.out.printf("Before ND\t%.2f\n", RelaxedValidation.validate(model, type, false));
        System.out.printf("Before WB\t%.2f\n", WeightedBalanceAnalysis.analyze(model, type));

        for (Function function : model.getFunctions()) {
            int dPre = (int) (Math.max(attributes[0], attributes[1]) * (1 - function.getPreceding()));
            int dSub = (int) (Math.max(attributes[0], attributes[1]) * (1 - function.getSubsequent()));

            int dOrg = (int) (attributes[2] * (1 - function.getOrganizationalUnits()));

            int dIn = (int) (attributes[3] * (1 - function.getInputs()));
            int dReg = isRegulationsConsidered ? (int) (attributes[3] * (1 - function.getRegulations())) : 0;
            int dOut = (int) (attributes[3] * (1 - function.getOutputs()));

            int dApp = (int) (attributes[4] * (1 - function.getApplicationSystems()));

            function.setPreceding(function.getPreceding() + dPre);
            function.setSubsequent(function.getSubsequent() + dSub);

            function.setOrganizationalUnits(function.getOrganizationalUnits() + dOrg);

            function.setInputs(function.getInputs() + dIn);
            function.setRegulations(function.getRegulations() + dReg);
            function.setOutputs(function.getOutputs() + dOut);

            function.setApplicationSystems(function.getApplicationSystems() + dApp);

            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                    function.getLabel(),
                    dPre, dSub,
                    dOrg,
                    dIn, dReg, dOut,
                    dApp);
        }

        System.out.printf("After ND\t%.2f\n", RelaxedValidation.validate(model, type, false));
        System.out.printf("After WB\t%.2f\n", WeightedBalanceAnalysis.analyze(model, type));
    }
}
