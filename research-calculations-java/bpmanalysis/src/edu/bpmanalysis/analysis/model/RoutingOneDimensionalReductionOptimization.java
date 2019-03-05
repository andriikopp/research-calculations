package edu.bpmanalysis.analysis.model;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.model.function.MatrixFunction;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class RoutingOneDimensionalReductionOptimization {

    public static double[][] optimization(Model model) {
        int size = 3;

        double[][] current = new double[size][2];

        Node.NodeType[] routingTypes = {
                Node.NodeType.XOR_CONNECTOR,
                Node.NodeType.OR_CONNECTOR,
                Node.NodeType.AND_CONNECTOR
        };

        for (int i = 0; i < size; i++) {
            current[i][0] = NodesSubsetsUtil.getSplitConnectors(model, routingTypes[i]).size();
            current[i][1] = NodesSubsetsUtil.getJoinConnectors(model, routingTypes[i]).size();
        }

        double[][] changes = new double[size][2];

        MatrixFunction function = (variables) -> {
            double result = 0;

            for (int i = 0; i < size; i++) {
                result += Math.pow((current[i][0] + variables[i][0]) - (current[i][1] + variables[i][1]), 2);
            }

            return result;
        };

        for (int i = 0; i < size; i++) {
            double old = function.value(changes);
            double SC = current[i][0];
            double JC = current[i][1];

            changes[i][0] = (JC - SC < 0 ? 0 : 1.0) * (JC - SC);

            if (function.value(changes) >= old) {
                changes[i][0] = 0;
            }

            old = function.value(changes);

            changes[i][1] = (SC - JC < 0 ? 0 : 1.0) * (SC - JC);

            if (function.value(changes) >= old) {
                changes[i][1] = 0;
            }
        }

        return changes;
    }
}
