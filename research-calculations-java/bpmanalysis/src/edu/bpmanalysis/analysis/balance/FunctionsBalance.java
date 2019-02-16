package edu.bpmanalysis.analysis.balance;

import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class FunctionsBalance implements Balance {
    public static final int MAX_F = 1;

    @Override
    public double balanceCoefficient(Model model) {
        double value = 0;

        for (Node node : model.getNodesList()) {
            for (Node.ArcType arcType : model.getArcTypes()) {
                if (node.getNodeType().equals(Node.NodeType.FUNCTION)) {
                    double max = MAX_F;

                    if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                            model.getModelType().equals(Model.ModelType.DFD)) {
                        max = Math.max(1, Math.min(arcType.get(node), 3));
                    }

                    value += Math.abs(arcType.get(node) - max);
                }
            }
        }

        return value;
    }
}
