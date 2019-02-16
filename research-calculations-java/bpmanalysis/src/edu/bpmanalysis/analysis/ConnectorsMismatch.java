package edu.bpmanalysis.analysis;

import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class ConnectorsMismatch {

    public static double mismatch(Model model) {
        double value = 0;

        for (Node.NodeType nodeType : new Node.NodeType[]{Node.NodeType.XOR_CONNECTOR,
                Node.NodeType.OR_CONNECTOR, Node.NodeType.AND_CONNECTOR}) {
            value += Math.abs(NodesSubsetsUtil.getSplitConnectors(model, nodeType).size() -
                    NodesSubsetsUtil.getJoinConnectors(model, nodeType).size());
        }

        return value;
    }
}
