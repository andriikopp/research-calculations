package edu.bpmanalysis.util;

import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;

import java.util.HashSet;
import java.util.Set;

public class DiscreteOptimizationUtil {

    public static int checkElement(Node.NodeType nodeType, int incoming, int outgoing) {
        if (nodeType == Node.NodeType.FUNCTION) {
            return (incoming == 1 && outgoing == 1) ? 1 : 0;
        }

        if (nodeType == Node.NodeType.EVENT) {
            return (incoming <= 1 && outgoing <= 1) && !(incoming == 0 && outgoing == 0) ? 1 : 0;
        }

        return ((incoming == 1 && outgoing == 2) || (incoming == 2 && outgoing == 1)) ? 1 : 0;
    }

    public static int minPossibleValue(Node.NodeType nodeType) {
        if (nodeType == Node.NodeType.FUNCTION) {
            return 1;
        }

        if (nodeType == Node.NodeType.EVENT) {
            return 0;
        }

        return 1;
    }

    public static int maxPossibleValue(Node.NodeType nodeType) {
        if (nodeType == Node.NodeType.FUNCTION) {
            return 1;
        }

        if (nodeType == Node.NodeType.EVENT) {
            return 1;
        }

        return 2;
    }

    public static Set<String> elementOptimization(Node node) {
        Set<String> result = new HashSet<>();

        if (checkElement(node.getNodeType(), node.getInput(), node.getOutput()) == 0) {
            int[][] alternatives = {
                    {minPossibleValue(node.getNodeType()) - node.getInput(),
                            minPossibleValue(node.getNodeType()) - node.getOutput()},
                    {minPossibleValue(node.getNodeType()) - node.getInput(),
                            maxPossibleValue(node.getNodeType()) - node.getOutput()},
                    {maxPossibleValue(node.getNodeType()) - node.getInput(),
                            minPossibleValue(node.getNodeType()) - node.getOutput()},
                    {maxPossibleValue(node.getNodeType()) - node.getInput(),
                            maxPossibleValue(node.getNodeType()) - node.getOutput()}
            };

            for (int[] alternative : alternatives) {
                int max = checkElement(node.getNodeType(), node.getInput() + alternative[0],
                        node.getOutput() + alternative[1]);

                if (max == 1) {
                    result.add("(in=" + alternative[0] + ", out=" + alternative[1] + ")");
                }
            }
        }

        return result;
    }

    public static int objectiveFunction(Model model) {
        int result = 0;

        for (Node node : model.getNodesList()) {
            result += checkElement(node.getNodeType(), node.getInput(), node.getOutput());
        }

        return result;
    }

    public static void modelOptimization(Model model) {
        int current = objectiveFunction(model);

        if (current < model.getNodesList().size()) {
            System.out.println(model.getName());
            System.out.println(current + " (max: " + model.getNodesList().size() + ")");

            for (Node node : model.getNodesList()) {
                System.out.println("\t" + node.getLabel() + "\t" + node.getNodeType() + "\t" +
                        "(in=" + node.getInput() + ", out=" + node.getOutput() + ")\t" +
                        elementOptimization(node));
            }
        }
    }
}
