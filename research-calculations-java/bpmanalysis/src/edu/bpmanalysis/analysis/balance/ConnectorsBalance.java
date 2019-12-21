package edu.bpmanalysis.analysis.balance;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class ConnectorsBalance implements Balance {
    public static final int MAX_C = 3;

    @Override
    public double balanceCoefficient(Model model) {
        if (model.getNodesList().isEmpty()) {
            return 0;
        }

        double value = 0;

        double count = NodesSubsetsUtil.getConnectors(model).size();

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                    node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                    node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)) {
                value += Math.signum(Math.abs((node.getInput() + node.getOutput()) - MAX_C));
            }
        }

        return count < 1 ? 0 : value / count;
    }
}
