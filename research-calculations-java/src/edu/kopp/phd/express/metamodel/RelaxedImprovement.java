package edu.kopp.phd.express.metamodel;

import edu.kopp.phd.express.metamodel.entity.Function;

public class RelaxedImprovement {

    public static double improve(Model model, String type, boolean showDetails) {
        double[] attributes = RelaxedValidation.ATTRIBUTES.get(type);

        boolean isRegulationsConsidered = type.equals("IDEF0");

        double faultActivities = 0;

        if (showDetails) {
            System.out.println(model.getName());
        }

        for (Function function : model.getFunctions()) {
            int dPre = (int) attributes[0] * min(
                    x -> (int) Math.pow((function.getPreceding() + x) - 1, 2),
                    -function.getPreceding(), 1
            );

            int dSub = (int) attributes[0] * min(
                    x -> (int) Math.pow((function.getSubsequent() + x) - 1, 2),
                    -function.getSubsequent(), 1
            );

            int dDin = (int) attributes[1] * min(
                    x -> (int) Math.pow((function.getPreceding() + x) - Math.max(function.getPreceding(), 1), 2),
                    -function.getPreceding(), 1
            );

            int dDout = (int) attributes[1] * min(
                    x -> (int) Math.pow((function.getSubsequent() + x) - Math.max(Math.min(function.getPreceding(),
                            function.getSubsequent()),1), 2),
                    -function.getSubsequent(), 1
            );

            int dOrg = (int) attributes[2] * min(
                    x -> (int) Math.pow((function.getOrganizationalUnits() + x) -
                            Math.max(function.getOrganizationalUnits(), 1), 2),
                    -function.getOrganizationalUnits(), 1
            );

            int dIn = (int) attributes[2] * min(
                    x -> (int) Math.pow((function.getInputs() + x) -
                            Math.max(function.getInputs(), 1), 2),
                    -function.getInputs(), 1
            );

            int dReg = isRegulationsConsidered ? (int) attributes[2] * min(
                    x -> (int) Math.pow((function.getRegulations() + x) -
                            Math.max(function.getRegulations(), 1), 2),
                    -function.getRegulations(), 1
            ) : 0;

            int dOut = (int) attributes[2] * min(
                    x -> (int) Math.pow((function.getOutputs() + x) -
                            Math.max(function.getOutputs(), 1), 2),
                    -function.getOutputs(), 1
            );

            int dApp = (int) attributes[2] * min(
                    x -> (int) Math.pow((function.getApplicationSystems() + x) -
                            Math.max(function.getApplicationSystems(), 1), 2),
                    -function.getApplicationSystems(), 1
            );

            faultActivities += (dPre == 0) && (dSub == 0) && (dDin == 0) && (dDout == 0) &&
                    (dOrg == 0) && (dIn == 0) && (dReg == 0) && (dOut == 0) && (dApp == 0) ? 0 : 1;

            if (showDetails) {
                System.out.printf("\t%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
                        function.getLabel(),
                        dPre, dSub,
                        dDin, dDout,
                        dOrg, dIn, dReg, dOut, dApp);
            }
        }

        return faultActivities;
    }

    private static final int min(BalanceFunction balance, int from, int to) {
        int min = from;
        int value = balance.function(from);

        for (int x = from + 1; x <= to; x++) {
            int newValue = balance.function(x);

            if (newValue < value) {
                value = newValue;
                min = x;
            }
        }

        return min;
    }

    private interface BalanceFunction {
        int function(double x);
    }
}
