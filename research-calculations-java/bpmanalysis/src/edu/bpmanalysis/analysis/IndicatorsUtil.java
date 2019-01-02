package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.collection.tools.Model;

public class IndicatorsUtil {

    public static void printIndicators(Model model) {
        System.out.printf("%s\t%d\t%.2f\t%.2f\t%d\t%d\t%.2f\t%d\n",
                model.getName(),
                model.getNodesList().size(),
                new ConnectorsBalance().balanceCoefficient(model),
                new FunctionsBalance().balanceCoefficient(model),
                NodesSubsetsUtil.getStartEvents(model).size(),
                NodesSubsetsUtil.getEndEvents(model).size(),
                ConnectorsMismatch.mismatch(model),
                NodesSubsetsUtil.getORRoutingElements(model).size());
    }
}
