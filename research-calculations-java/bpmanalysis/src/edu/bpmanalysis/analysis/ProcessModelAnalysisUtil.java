package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.balance.ConnectorsBalance;
import edu.bpmanalysis.analysis.balance.FunctionsBalance;
import edu.bpmanalysis.analysis.balance.api.Balance;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.analysis.model.*;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import edu.bpmanalysis.search.partition.ProcessModelAnalysisResultsPartition;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;

import java.util.LinkedHashMap;
import java.util.List;

public class ProcessModelAnalysisUtil {

    public static Model transformToModel(ProcessModelBean processModelBean) {
        Model model;

        if (processModelBean.getNotation().equals("BPMN")) {
            model = Model.createBPMNModel(processModelBean.getName());
        } else if (processModelBean.getNotation().equals("EPC")) {
            model = Model.createEPCModel(processModelBean.getName());
        } else if (processModelBean.getNotation().equals("IDEF0")) {
            model = Model.createIDEF0Model(processModelBean.getName());
        } else {
            model = Model.createDFDModel(processModelBean.getName());
        }

        model.setId(processModelBean.getId());

        for (ProcessModelGraphNodeBean nodeBean : processModelBean.getGraph().getNodes()) {
            Node node = null;

            if (getNodeType(nodeBean).equals("event")) {
                node = Node.createEvent(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("function")) {
                node = Node.createFunction(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean),
                        getControls(processModelBean, nodeBean),
                        getMechanisms(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("AND")) {
                node = Node.createANDConnector(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("OR")) {
                node = Node.createORConnector(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("XOR")) {
                node = Node.createXORConnector(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("dataStore")) {
                node = Node.createDataStore(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("externalEntity")) {
                node = Node.createExternalEntity(getNodeLabel(nodeBean),
                        getInputs(processModelBean, nodeBean),
                        getOutputs(processModelBean, nodeBean));
            }

            if (getNodeType(nodeBean).equals("interface")) {
                node = Node.createInterface(getNodeLabel(nodeBean),
                        getInterfaceOutIn(processModelBean, nodeBean),
                        getInterfaceInOut(processModelBean, nodeBean),
                        getInterfaceConOut(processModelBean, nodeBean),
                        getInterfaceMechOut(processModelBean, nodeBean));
            }

            if (node != null) {
                model.getNodesList().add(node);
            }
        }

        return model;
    }

    public static ProcessModelAnalysisBean analyzeModel(Model model) {
        Balance connectorsBalance = new ConnectorsBalance();
        Balance functionsBalance = new FunctionsBalance();

        ProcessModelAnalysisBean processModelAnalysisBean = new ProcessModelAnalysisBean();
        processModelAnalysisBean.setId(model.getId());
        processModelAnalysisBean.setName(model.getName());
        processModelAnalysisBean.setSize((int) ModelDensity.size(model));
        processModelAnalysisBean.setFunctions(NodesSubsetsUtil.getFunctions(model).size());
        processModelAnalysisBean.setConnectorsBalance(Double
                .valueOf(String
                        .format("%.2f", connectorsBalance.balanceCoefficient(model))
                )
        );
        processModelAnalysisBean.setFunctionsBalance(Double
                .valueOf(String
                        .format("%.2f", functionsBalance.balanceCoefficient(model))
                )
        );
        processModelAnalysisBean.setStartEvents(NodesSubsetsUtil.getStartEvents(model).size());
        processModelAnalysisBean.setEndEvents(NodesSubsetsUtil.getEndEvents(model).size());
        processModelAnalysisBean.setMismatch(ConnectorsMismatch.mismatch(model));
        processModelAnalysisBean.setOrConnectors(NodesSubsetsUtil.getORRoutingElements(model).size());

        double[] nodesChanges = null;
        double[] connectorsChanges = null;
        double[][] routingChanges = null;
        double[][] functionsChanges = null;

        int improvements = 0;
        int prevImprovements = 0;

        boolean nodesFixed = false;
        boolean connectorsFixed = false;
        boolean routingFixed = false;
        boolean functionsFixed = false;

        while (true) {
            if (model.getModelType().equals(Model.ModelType.DFD) || model.getModelType().equals(Model.ModelType.IDEF0)) {
                if (!nodesFixed && ((model.getModelType().equals(Model.ModelType.DFD) &&
                        (processModelAnalysisBean.getFunctions() > 7 || processModelAnalysisBean.getFunctions() < 1)) ||
                        (model.getModelType().equals(Model.ModelType.IDEF0) &&
                                (processModelAnalysisBean.getFunctions() > 6 || processModelAnalysisBean.getFunctions() < 3)))) {
                    nodesChanges = NodesOptimization.optimization(model);
                    improvements++;
                    nodesFixed = true;
                } else {
                    if (!functionsFixed && processModelAnalysisBean.getFunctionsBalance() > 0) {
                        functionsChanges = FunctionsOptimization.optimization(model);
                        improvements++;
                        functionsFixed = true;
                    }
                }
            } else {
                if (!nodesFixed && (processModelAnalysisBean.getSize() > 31 || processModelAnalysisBean.getFunctions() < 1 ||
                        processModelAnalysisBean.getStartEvents() != 1 || processModelAnalysisBean.getEndEvents() != 1 ||
                        processModelAnalysisBean.getOrConnectors() > 0)) {
                    nodesChanges = NodesOptimization.optimization(model);
                    improvements++;
                    nodesFixed = true;
                } else {
                    if (!connectorsFixed && processModelAnalysisBean.getConnectorsBalance() > 0) {
                        connectorsChanges = ConnectorsOptimization.optimization(model);
                        improvements++;
                        connectorsFixed = true;
                    }

                    if (!routingFixed && processModelAnalysisBean.getMismatch() > 0) {
                        routingChanges = RoutingOptimization.optimization(model);
                        improvements++;
                        routingFixed = true;
                    }

                    if (!functionsFixed && processModelAnalysisBean.getFunctionsBalance() > 0) {
                        functionsChanges = FunctionsOptimization.optimization(model);
                        improvements++;
                        functionsFixed = true;
                    }
                }
            }

            if (improvements == prevImprovements) {
                break;
            }

            prevImprovements = improvements;
        }

        if (nodesChanges == null) {
            processModelAnalysisBean.setNodesChanges(new double[5]);
        } else {
            processModelAnalysisBean.setNodesChanges(nodesChanges);
        }

        processModelAnalysisBean.setConnectorsChanges(new LinkedHashMap<>());

        if (connectorsChanges != null) {
            List<Node> connectors = NodesSubsetsUtil.getConnectors(model);

            for (int i = 0; i < connectors.size(); i++) {
                processModelAnalysisBean.getConnectorsChanges()
                        .put(connectors.get(i).getLabel(), connectorsChanges[i]);
            }
        }

        processModelAnalysisBean.setRoutingChanges(new LinkedHashMap<>());

        if (routingChanges != null) {
            processModelAnalysisBean.getRoutingChanges().put("XOR", routingChanges[0]);
            processModelAnalysisBean.getRoutingChanges().put("OR", routingChanges[1]);
            processModelAnalysisBean.getRoutingChanges().put("AND", routingChanges[2]);
        }

        processModelAnalysisBean.setFunctionsChanges(new LinkedHashMap<>());

        if (functionsChanges != null) {
            List<Node> functions = NodesSubsetsUtil.getFunctions(model);

            for (int i = 0; i < functions.size(); i++) {
                processModelAnalysisBean.getFunctionsChanges()
                        .put(functions.get(i).getLabel(), functionsChanges[i]);
            }
        }

        processModelAnalysisBean.setSimilarModels(ProcessModelAnalysisResultsPartition.getPartitionedModels(model));

        double quality = EvaluationUtil.quality(model);

        processModelAnalysisBean.setQuality(Double.valueOf(
                String.format("%.2f", quality)
        ));

        return processModelAnalysisBean;
    }

    private static String getNodeType(ProcessModelGraphNodeBean nodeBean) {
        return nodeBean.getId().split("#")[0];
    }

    private static String getNodeLabel(ProcessModelGraphNodeBean nodeBean) {
        String[] nodeLabelComponents = nodeBean.getId().split("#");

        if (nodeLabelComponents.length == 1) {
            return "";
        }

        return nodeLabelComponents[1];
    }

    private static int getInputs(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("sequenceFlow") || edgeBean.getLabel().equals("dataFlow") ||
                    edgeBean.getLabel().equals("inputArc")) {
                if (edgeBean.getTo().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getOutputs(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("sequenceFlow") || edgeBean.getLabel().equals("dataFlow") ||
                    edgeBean.getLabel().equals("outputArc")) {
                if (edgeBean.getFrom().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getControls(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("controlArc")) {
                if (edgeBean.getTo().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getMechanisms(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("mechanismArc")) {
                if (edgeBean.getTo().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getInterfaceOutIn(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("outputArc")) {
                if (edgeBean.getTo().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getInterfaceInOut(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("inputArc")) {
                if (edgeBean.getFrom().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getInterfaceConOut(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("controlArc")) {
                if (edgeBean.getFrom().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }

    private static int getInterfaceMechOut(ProcessModelBean processModelBean, ProcessModelGraphNodeBean nodeBean) {
        int value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : processModelBean.getGraph().getEdges()) {
            if (edgeBean.getLabel().equals("mechanismArc")) {
                if (edgeBean.getFrom().equals(nodeBean.getId())) {
                    value++;
                }
            }
        }

        return value;
    }
}
