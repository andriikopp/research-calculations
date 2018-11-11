package edu.kopp.phd.express.search;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.Node;
import edu.kopp.phd.express.search.utils.VectorSimilarity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PatternMatching {
    private static Set<Node> faultPatterns = new HashSet<>();
    private static Set<Node> correctPatterns = new HashSet<>();

    public static void train(Node[] fault, Node[] correct) {
        faultPatterns.addAll(Arrays.asList(fault));
        correctPatterns.addAll(Arrays.asList(correct));
    }

    public static double match(Model model) {
        double numberOfDefects = 0;

        for (Node node : model.getNodes()) {
            Node nearestPattern = nearestNeighbor(node);

            if (faultPatterns.contains(nearestPattern)) {
                numberOfDefects++;

                System.out.printf("%s\t%s\n", node.getLabel(), nearestPattern.getLabel());
            }
        }

        return numberOfDefects;
    }

    private static Node nearestNeighbor(Node node) {
        Node nearestPattern = null;

        double similarity = Double.MIN_VALUE;

        for (Node faultPattern : faultPatterns) {
            double value = sim(node, faultPattern);

            if (value > similarity) {
                similarity = value;
                nearestPattern = faultPattern;
            }
        }

        for (Node correctPattern : correctPatterns) {
            double value = sim(node, correctPattern);

            if (value > similarity) {
                similarity = value;
                nearestPattern = correctPattern;
            }
        }

        return nearestPattern;
    }

    private static double sim(Node a, Node b) {
        if (a.getClass().equals(b.getClass())) {
            return VectorSimilarity.similarity(new double[]{a.getPreceding(), a.getSubsequent()},
                    new double[]{b.getPreceding(), b.getSubsequent()});
        }

        return 0;
    }
}
