package edu.bpmanalysis.similarity;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.metamodel.Node;
import edu.bpmanalysis.similarity.api.Similarity;

import java.util.*;

public class GraphStructureSimilarity implements Similarity {

    @Override
    public double compare(Model first, Model second) {
        double functionsSize = NodesSubsetsUtil.getFunctions(first).size() +
                NodesSubsetsUtil.getFunctions(second).size() > 0 ? 1 : 0;
        double eventsSize = NodesSubsetsUtil.getEvents(first).size() +
                NodesSubsetsUtil.getEvents(second).size() > 0 ? 1 : 0;
        double connectorsSize = NodesSubsetsUtil.getConnectors(first).size() +
                NodesSubsetsUtil.getConnectors(second).size() > 0 ? 1 : 0;

        Node.ArcType[] arcTypes = first.getArcTypes();

        double functionsSimilarity = 0;

        for (Node.ArcType arcType : arcTypes) {
            functionsSimilarity += jaccardIndex(getMultisetOfElementsDegrees(first, arcType, Node.NodeType.FUNCTION),
                    getMultisetOfElementsDegrees(second, arcType, Node.NodeType.FUNCTION));
        }

        functionsSimilarity /= (double) arcTypes.length;

        double eventsSimilarity = 0;

        for (Node.ArcType arcType : arcTypes) {
            eventsSimilarity += jaccardIndex(getMultisetOfElementsDegrees(first, arcType, Node.NodeType.EVENT),
                    getMultisetOfElementsDegrees(second, arcType, Node.NodeType.EVENT));
        }

        eventsSimilarity /= (double) arcTypes.length;

        double connectorsSimilarity = 0;

        for (Node.ArcType arcType : arcTypes) {
            connectorsSimilarity += jaccardIndex(getMultisetOfElementsDegrees(first, arcType,
                    Node.NodeType.XOR_CONNECTOR, Node.NodeType.OR_CONNECTOR, Node.NodeType.AND_CONNECTOR),
                    getMultisetOfElementsDegrees(second, arcType,
                            Node.NodeType.XOR_CONNECTOR, Node.NodeType.OR_CONNECTOR, Node.NodeType.AND_CONNECTOR));
        }

        connectorsSimilarity /= (double) arcTypes.length;

        return (functionsSimilarity + eventsSimilarity + connectorsSimilarity) /
                (functionsSize + eventsSize + connectorsSize);
    }

    public static Map<Integer, Integer> getMultisetOfElementsDegrees(Model model, Node.ArcType arcType,
                                                                     Node.NodeType... nodeType) {
        Map<Integer, Integer> multiset = new HashMap<>();

        for (Node node : model.getNodesList()) {
            if (Arrays.asList(nodeType).contains(node.getNodeType())) {
                int key = arcType.get(node);

                if (multiset.containsKey(key)) {
                    int value = multiset.get(key);
                    value++;
                    multiset.put(key, value);
                } else {
                    multiset.put(key, 1);
                }
            }
        }

        return multiset;
    }

    public static double jaccardIndex(Map<Integer, Integer> a, Map<Integer, Integer> b) {
        Set<Integer> union = new HashSet<>(a.keySet());
        union.addAll(b.keySet());

        double min = 0;
        double max = 0;

        for (Integer key : union) {
            min += Math.min(getMultiplicity(a, key), getMultiplicity(b, key));
            max += Math.max(getMultiplicity(a, key), getMultiplicity(b, key));
        }

        return min / max;
    }

    private static double getMultiplicity(Map<Integer, Integer> set, int key) {
        return set.get(key) == null ? 0 : set.get(key);
    }
}
