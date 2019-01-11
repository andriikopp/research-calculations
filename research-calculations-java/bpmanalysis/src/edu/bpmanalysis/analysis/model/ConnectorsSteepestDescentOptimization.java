package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.model.function.ArrayFunction;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.metamodel.Node;

import java.util.Arrays;
import java.util.List;

public class ConnectorsSteepestDescentOptimization {

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
            if (Math.pow(current[i] - ConnectorsBalance.MAX_C, 2) > 0) {
                double lambda = (current[i] + changes[i] - ConnectorsBalance.MAX_C) /
                        (2.0 * (ConnectorsBalance.MAX_C + current[i] + changes[i]));

                changes[i] = changes[i] - lambda * 2.0 * (ConnectorsBalance.MAX_C + (current[i] + changes[i]));
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
