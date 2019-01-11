package edu.bpmanalysis.analysis.balance;

import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.metamodel.Node;

public class ConnectorsBalance implements Balance {
    public static final int MAX_C = 3;

    @Override
    public double balanceCoefficient(Model model) {
        double value = 0;

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                    node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                    node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)) {
                value += Math.abs((node.getInput() + node.getOutput()) - MAX_C);
            }
        }

        return value;
    }
}
