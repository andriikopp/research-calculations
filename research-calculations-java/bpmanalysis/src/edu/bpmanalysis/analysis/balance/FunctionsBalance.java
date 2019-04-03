package edu.bpmanalysis.analysis.balance;

import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class FunctionsBalance implements Balance {
    public static final int MAX_F = 1;
    public static final int MAX_FUNCTIONAL_ARCS = 3;

    @Override
    public double balanceCoefficient(Model model) {
        double value = 0;

        double count = 0;

        for (Node node : model.getNodesList()) {
            for (int j = 0; j < Node.arcTypes.length; j++) {
                if (node.getNodeType().equals(Node.NodeType.FUNCTION)) {
                    double max = MAX_F;

                    if (model.getModelType().equals(Model.ModelType.IDEF0) ||
                            model.getModelType().equals(Model.ModelType.DFD)) {
                        double necessaryMin;

                        if (model.getModelType().equals(Model.ModelType.IDEF0) && (j == 0)) {
                            necessaryMin = 0;
                        } else {
                            necessaryMin = 1;
                        }

                        max = Math.max(necessaryMin, Math.min(Node.arcTypes[j].get(node),
                                MAX_FUNCTIONAL_ARCS));
                    }

                    value += Math.abs(Node.arcTypes[j].get(node) - max);
                }
            }

            count++;
        }

        return value / count;
    }
}
