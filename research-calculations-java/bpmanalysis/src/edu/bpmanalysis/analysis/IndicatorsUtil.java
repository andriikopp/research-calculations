package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.description.tools.Model;

public class IndicatorsUtil {

    public static void printIndicators(Model model) {
        System.out.printf("%s\t%.2f\t%.2f\t%d\t%.2f\t%.2f\t%d\t%d\t%.2f\t%d\t%s%.2f\n",
                model.getName().replaceAll("\n", ""),
                ModelDensity.size(model),
                ModelDensity.arcs(model),
                NodesSubsetsUtil.getFunctions(model).size(),
                new ConnectorsBalance().balanceCoefficient(model),
                new FunctionsBalance().balanceCoefficient(model),
                NodesSubsetsUtil.getStartEvents(model).size(),
                NodesSubsetsUtil.getEndEvents(model).size(),
                ConnectorsMismatch.mismatch(model),
                NodesSubsetsUtil.getORRoutingElements(model).size(),
                CriteriaVector.toString(EvaluationUtil.checkGuidelines(model)),
                EvaluationUtil.quality(model));
    }
}

class CriteriaVector {
    public static String toString(double[] criteria) {
        String str = "";

        for (double val : criteria) {
            str += val + "\t";
        }

        return str;
    }
}
