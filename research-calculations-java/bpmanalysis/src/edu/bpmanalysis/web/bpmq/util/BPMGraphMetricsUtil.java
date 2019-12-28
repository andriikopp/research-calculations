package edu.bpmanalysis.web.bpmq.util;

import edu.bpmanalysis.analysis.api.NodesSubset;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import edu.bpmanalysis.web.bpmq.entity.BPModel;

import java.util.stream.Collectors;

public class BPMGraphMetricsUtil {

    public static BPModel getBPModelFromGraph(Model model, String fileName) {
        int totalNodes = getTotalNodes(model);
        int invalidNodes = getInvalidNodes(model);
        int startEvents = getStartEvents(model);
        int endEvents = getEndEvents(model);
        int unmatchedGateways = getUnmatchedGateways(model);
        int totalGateways = getTotalGateways(model);
        int orGateways = getOrGateways(model);

        if (totalNodes == 0) {
            return null;
        }

        int sizeQualityH = totalNodes <= 31 ? 1 : 0;
        int degreesQualityH = invalidNodes == 0 ? 1 : 0;
        int eventsQualityH = startEvents == 1 && endEvents == 1 ? 1 : 0;
        int gatewaysQualityH = unmatchedGateways == 0 ? 1 : 0;
        int orQualityH = orGateways == 0 ? 1 : 0;

        double sizeQualityS = totalNodes <= 31 ? 1 : 31.0 / (double) totalNodes;
        double degreesQualityS = 1.0 - (double) invalidNodes / (double) totalNodes;
        double eventsQualityS = Math.min(1.0 / (1.0 + Math.pow((double) startEvents - 1.0, 2.0)),
                1.0 / (1.0 + Math.pow((double) endEvents - 1.0, 2.0)));
        double gatewaysQualityS = totalGateways > 0 ? 1.0 - (double) unmatchedGateways / (double) totalGateways : 1;
        double orQualityS = totalGateways > 0 ? 1.0 - (double) orGateways / (double) totalGateways : 1;

        double hardQuality =
                BPMQualityWeights.SIZE * sizeQualityH +
                        BPMQualityWeights.DEGREES * degreesQualityH +
                        BPMQualityWeights.EVENTS * eventsQualityH +
                        BPMQualityWeights.GATEWAYS * gatewaysQualityH +
                        BPMQualityWeights.OR * orQualityH;

        double softQuality =
                BPMQualityWeights.SIZE * sizeQualityS +
                        BPMQualityWeights.DEGREES * degreesQualityS +
                        BPMQualityWeights.EVENTS * eventsQualityS +
                        BPMQualityWeights.GATEWAYS * gatewaysQualityS +
                        BPMQualityWeights.OR * orQualityS;

        return new BPModel(
                fileName,
                totalNodes,
                invalidNodes,
                startEvents,
                endEvents,
                unmatchedGateways,
                totalGateways,
                orGateways,
                sizeQualityH,
                degreesQualityH,
                eventsQualityH,
                gatewaysQualityH,
                orQualityH,
                sizeQualityS,
                degreesQualityS,
                eventsQualityS,
                gatewaysQualityS,
                orQualityS,
                hardQuality,
                softQuality
        );
    }

    public static String assessQuality(double quality) {
        if (quality >= 0.8) {
            return "Very High";
        }

        if (quality >= 0.64) {
            return "High";
        }

        if (quality >= 0.37) {
            return "Medium";
        }

        if (quality >= 0.2) {
            return "Low";
        }

        return "Very Low";
    }

    public static int getTotalNodes(Model model) {
        return model.getNodesList().size();
    }

    public static int getInvalidNodes(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (
                        node.getNodeType().equals(Node.NodeType.FUNCTION) &&
                                (node.getInput() != 1 || node.getOutput() != 1))
                ).collect(Collectors.toList()))
                .getSubset(model).size()
                +
                ((NodesSubset) x -> x.getNodesList()
                        .stream().filter(node -> (
                                node.getNodeType().equals(Node.NodeType.EVENT) &&
                                        (node.getInput() > 1 || node.getOutput() > 1))
                        ).collect(Collectors.toList()))
                        .getSubset(model).size()
                +
                ((NodesSubset) x -> x.getNodesList()
                        .stream().filter(node -> (
                                node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) &&
                                        (
                                                (node.getInput() > 1 && node.getOutput() > 1) ||
                                                        node.getInput() == 0 || node.getOutput() == 0
                                        ))
                        ).collect(Collectors.toList()))
                        .getSubset(model).size()
                +
                ((NodesSubset) x -> x.getNodesList()
                        .stream().filter(node -> (
                                node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) &&
                                        (
                                                (node.getInput() > 1 && node.getOutput() > 1) ||
                                                        node.getInput() == 0 || node.getOutput() == 0
                                        ))
                        ).collect(Collectors.toList()))
                        .getSubset(model).size()
                +
                ((NodesSubset) x -> x.getNodesList()
                        .stream().filter(node -> (
                                node.getNodeType().equals(Node.NodeType.AND_CONNECTOR) &&
                                        (
                                                (node.getInput() > 1 && node.getOutput() > 1) ||
                                                        node.getInput() == 0 || node.getOutput() == 0
                                        ))
                        ).collect(Collectors.toList()))
                        .getSubset(model).size();
    }

    public static int getStartEvents(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.EVENT) &&
                        (node.getInput() == 0 && node.getOutput() > 0)))
                .collect(Collectors.toList()))
                .getSubset(model).size();
    }

    public static int getEndEvents(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.EVENT) &&
                        (node.getInput() > 0 && node.getOutput() == 0)))
                .collect(Collectors.toList()))
                .getSubset(model).size();
    }

    public static int getUnmatchedGateways(Model model) {
        return Math.abs(((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (
                        (node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                                node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                                node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)
                        )) && (node.getInput() == 1 && node.getOutput() > 1)
                ).collect(Collectors.toList()))
                .getSubset(model).size()
                -
                ((NodesSubset) x -> x.getNodesList()
                        .stream().filter(node -> (
                                (node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                                        node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                                        node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)
                                )) && (node.getInput() > 1 && node.getOutput() == 1)
                        ).collect(Collectors.toList()))
                        .getSubset(model).size());
    }

    public static int getTotalGateways(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (
                        node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR) ||
                                node.getNodeType().equals(Node.NodeType.OR_CONNECTOR) ||
                                node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)
                )).collect(Collectors.toList()))
                .getSubset(model).size();
    }

    public static int getOrGateways(Model model) {
        return ((NodesSubset) x -> x.getNodesList()
                .stream().filter(node -> (node.getNodeType().equals(Node.NodeType.OR_CONNECTOR)))
                .collect(Collectors.toList()))
                .getSubset(model).size();
    }
}
