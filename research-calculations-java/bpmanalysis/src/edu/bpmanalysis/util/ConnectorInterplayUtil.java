package edu.bpmanalysis.util;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

import java.util.List;

public class ConnectorInterplayUtil {

    public static int mismatch(Model model) {
        return mismatch(model, Node.NodeType.AND_CONNECTOR) +
                mismatch(model, Node.NodeType.XOR_CONNECTOR) +
                mismatch(model, Node.NodeType.OR_CONNECTOR);
    }

    public static int mismatch(Model model, Node.NodeType nodeType) {
        int splitsDegreeSum = 0;
        int joinsDegreeSum = 0;

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(nodeType)) {
                int in = node.getInput();
                int out = node.getOutput();

                int degree = in + out;

                if (in == 1 && out > 1) {
                    splitsDegreeSum += degree;
                }

                if (in > 1 && out == 1) {
                    joinsDegreeSum += degree;
                }
            }
        }

        return Math.abs(splitsDegreeSum - joinsDegreeSum);
    }

    public static double mismatch2(Model model) {
        double mismatch = 0;

        Node.NodeType[] nodeTypes = {Node.NodeType.AND_CONNECTOR,
                Node.NodeType.XOR_CONNECTOR,
                Node.NodeType.OR_CONNECTOR};

        for (Node.NodeType type : nodeTypes) {
            List<Node> splits = NodesSubsetsUtil.getSplitConnectors(model, type);
            List<Node> joins = NodesSubsetsUtil.getJoinConnectors(model, type);

            double splitsDegree = 1;
            double joinsDegree = 1;

            for (Node node : splits) {
                splitsDegree += (node.getInput() + node.getOutput());
            }

            for (Node node : joins) {
                joinsDegree += (node.getInput() + node.getOutput());
            }

            splitsDegree /= (splits.size() + 1);
            joinsDegree /= (joins.size() + 1);

            mismatch += Math.abs(splitsDegree - joinsDegree)
                    + Math.abs(splits.size() - splitsDegree / 3)
                    + Math.abs(joins.size() - joinsDegree / 3);
        }

        return mismatch;
    }
}
