package edu.bpmanalysis.util;

import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

public class BpmnMLDatasetUtil {

    public static void printMLDataset(Model model) {
        for (Node node : model.getNodesList()) {

            if (node.getNodeType() == Node.NodeType.XOR_CONNECTOR ||
                    node.getNodeType() == Node.NodeType.OR_CONNECTOR ||
                    node.getNodeType() == Node.NodeType.AND_CONNECTOR) {

                int result = ((node.getInput() == 1 && node.getOutput() > 1) ||
                        (node.getInput() > 1 && node.getOutput() == 1)) ? 1 : 0;

                System.out.println(String.format("%d\t%d\t%d", node.getInput(), node.getOutput(),
                        result));
            }
        }
    }
}
