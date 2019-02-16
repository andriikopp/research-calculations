package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.model.function.ArrayFunction;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.tools.Node;

import java.util.Arrays;
import java.util.List;

public class ConnectorsOneDimensionalReductionOptimization {

    public static double[] optimization(Model model) {
        List<Node> connectors = NodesSubsetsUtil.getConnectors(model);

        int size = connectors.size();

        double[] current = getConnectorsDegreesVector(connectors);
        double[] changes = new double[size];

        ArrayFunction function = (variables) -> {
            double result = 0;

            for (int i = 0; i < current.length; i++) {
                result += Math.pow((current[i] + variables[i]) - ConnectorsBalance.MAX_C, 2);
            }

            return result;
        };

        for (int i = 0; i < size; i++) {
            double old = function.value(changes);
            changes[i] = ConnectorsBalance.MAX_C - current[i];

            if (function.value(changes) >= old) {
                changes[i] = 0;
            }
        }

        if (Configuration.DEBUG) {
            System.out.printf("%s\t%.2f\n", Arrays.toString(changes), function.value(changes));
        }

        return changes;
    }

    private static double[] getConnectorsDegreesVector(List<Node> connectors) {
        double[] array = new double[connectors.size()];

        for (int i = 0; i < connectors.size(); i++) {
            array[i] = connectors.get(i).getInput() + connectors.get(i).getOutput();
        }

        return array;
    }
}
