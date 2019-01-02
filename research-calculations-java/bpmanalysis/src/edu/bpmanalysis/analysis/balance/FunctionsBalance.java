package edu.bpmanalysis.analysis.balance;

import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.metamodel.Node;

public class FunctionsBalance implements Balance {
    private static final int MAX_F = 1;

    @Override
    public double balanceCoefficient(Model model) {
        double value = 0;

        for (Node node : model.getNodesList()) {
            for (Node.ArcType arcType : model.getArcTypes()) {
                if (node.getNodeType().equals(Node.NodeType.FUNCTION)) {
                    value += Math.abs(arcType.get(node) - MAX_F);
                }
            }
        }

        return value;
    }
}
