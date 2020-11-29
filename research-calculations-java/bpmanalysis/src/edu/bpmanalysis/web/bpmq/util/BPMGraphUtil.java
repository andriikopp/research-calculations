package edu.bpmanalysis.web.bpmq.util;

import edu.bpmanalysis.analysis.NodesSubsetsUtil;
import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.description.ProcessModelImportUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.web.bpmq.entity.BPMMeasures;
import edu.bpmanalysis.web.bpmq.entity.BPModel;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static edu.bpmanalysis.config.Configuration.PATH_TO_BPMN_MODELS;

public class BPMGraphUtil {

    public static List<BPModel> getReadyBPMNModels() {
        final String extension = "bpmn";

        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        List<BPModel> bpmnModels = new ArrayList<>();

        for (File file : listOfFiles != null ? listOfFiles : new File[0]) {
            if (file.isFile()) {
                String[] splitFileName = file.getName().split("\\.");
                String fileExtension = splitFileName[splitFileName.length - 1];

                if (fileExtension.equals(extension)) {
                    try {
                        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

                        Collection<ModelElementInstance> processes = modelInstance.getModelElementsByType(
                                modelInstance.getModel().getType(Process.class)
                        );

                        for (ModelElementInstance modelElementInstance : processes) {
                            Process process = (Process) modelElementInstance;

                            String processName = null;

                            Collection<ModelElementInstance> participants = modelInstance.getModelElementsByType(
                                    modelInstance.getModel().getType(Participant.class)
                            );

                            for (ModelElementInstance instance : participants) {
                                Participant participant = (Participant) instance;

                                if (participant.getProcess().getId().equals(process.getId())) {
                                    processName = participant.getName();
                                }
                            }

                            if (processName == null) {
                                processName = file.getName();
                            }

                            processName += "_[Id-" + process.getId() + "]";
                            processName = processName.replaceAll("\r\n", "");
                            processName = processName.replaceAll("\n", "");

                            ProcessModelBean processModelBean = new ProcessModelBean();
                            processModelBean.setId(UUID.randomUUID().toString());
                            processModelBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
                            processModelBean.setName(processName);
                            processModelBean.setNotation(extension.toUpperCase());
                            processModelBean.setLevel("Foundation");
                            processModelBean.setDescription(processName);
                            processModelBean.setFileName(file.getName());

                            ProcessModelGraphBean processModelGraphBean = new ProcessModelGraphBean();

                            List<ProcessModelGraphNodeBean> nodeBeans = new ArrayList<>();
                            List<ProcessModelGraphEdgeBean> edgeBeans = new ArrayList<>();

                            ModelElementType sequenceFlowType = modelInstance.getModel().getType(SequenceFlow.class);

                            Set<String> nodes = new HashSet<>();

                            for (FlowElement flowElement : process.getFlowElements()) {
                                if (flowElement.getElementType().equals(sequenceFlowType)) {
                                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;

                                    FlowNode source = sequenceFlow.getSource();
                                    FlowNode target = sequenceFlow.getTarget();

                                    String sourceType = source.getElementType().getTypeName();
                                    String targetType = target.getElementType().getTypeName();

                                    String sourceNodeId;
                                    String targetNodeId;

                                    if (source.getName() == null) {
                                        sourceNodeId = ProcessModelImportUtil.getNodeTypeByBPMNType(sourceType) +
                                                "#_[Id-" + source.getId() + "]";
                                    } else {
                                        sourceNodeId = ProcessModelImportUtil.getNodeTypeByBPMNType(sourceType) + "#" + source.getName()
                                                .replace("\n", "")
                                                .replace("\r", "")
                                                .replaceAll("\\s+", "_") +
                                                "_[Id-" + source.getId() + "]";
                                    }

                                    nodes.add(sourceNodeId);

                                    if (target.getName() == null) {
                                        targetNodeId = ProcessModelImportUtil.getNodeTypeByBPMNType(targetType) +
                                                "#_[Id-" + target.getId() + "]";
                                    } else {
                                        targetNodeId = ProcessModelImportUtil.getNodeTypeByBPMNType(targetType) + "#" + target.getName()
                                                .replace("\n", "")
                                                .replace("\r", "")
                                                .replaceAll("\\s+", "_") +
                                                "_[Id-" + target.getId() + "]";
                                    }

                                    nodes.add(targetNodeId);

                                    ProcessModelGraphEdgeBean processModelGraphEdgeBean = new ProcessModelGraphEdgeBean();
                                    processModelGraphEdgeBean.setId(UUID.randomUUID().toString());
                                    processModelGraphEdgeBean.setFrom(sourceNodeId);
                                    processModelGraphEdgeBean.setTo(targetNodeId);
                                    processModelGraphEdgeBean.setLabel("sequenceFlow");
                                    processModelGraphEdgeBean.setArrows("to");

                                    edgeBeans.add(processModelGraphEdgeBean);
                                }
                            }

                            for (String nodeId : nodes) {
                                ProcessModelGraphNodeBean processModelGraphNodeBean = new ProcessModelGraphNodeBean();
                                processModelGraphNodeBean.setId(nodeId);
                                processModelGraphNodeBean.setLabel(nodeId);
                                processModelGraphNodeBean.setColor(ProcessModelImportUtil.getNodeColorById(nodeId));

                                nodeBeans.add(processModelGraphNodeBean);
                            }

                            processModelGraphBean.setNodes(nodeBeans);
                            processModelGraphBean.setEdges(edgeBeans);

                            processModelBean.setGraph(processModelGraphBean);

                            Model model = ProcessModelAnalysisUtil.transformToModel(processModelBean);
                            BPModel bpModel = BPMGraphMetricsUtil.getBPModelFromGraph(model,
                                    processModelBean.getFileName() + "_" + processName);

                            BPMMeasures bpmMeasures = new BPMMeasures();

                            bpmMeasures.totalNodes = nodeBeans.size();
                            bpmMeasures.sequenceFlows = edgeBeans.size();

                            bpmMeasures.tasks = NodesSubsetsUtil.getFunctions(model).size();

                            bpmMeasures.startEvents = NodesSubsetsUtil.getStartEvents(model).size();
                            bpmMeasures.intermediateEvents = NodesSubsetsUtil.getIntermediateEvents(model).size();
                            bpmMeasures.endEvents = NodesSubsetsUtil.getEndEvents(model).size();

                            bpmMeasures.andGateways =
                                    NodesSubsetsUtil.getSplitANDConnectors(model).size() +
                                            NodesSubsetsUtil.getJoinANDConnectors(model).size();
                            bpmMeasures.orGateways =
                                    NodesSubsetsUtil.getSplitORConnectors(model).size() +
                                            NodesSubsetsUtil.getJoinORConnectors(model).size();
                            bpmMeasures.xorGateways =
                                    NodesSubsetsUtil.getSplitXORConnectors(model).size() +
                                            NodesSubsetsUtil.getJoinXORConnectors(model).size();

                            bpModel.setMeasures(bpmMeasures);

                            if (bpModel != null) {
                                bpmnModels.add(bpModel);

                                BPMOntologyUtil.processModel(file.getName() + "_" + processName,
                                        modelInstance, bpModel);
                            }
                        }
                    } catch (RuntimeException e) {
                        System.err.printf("File '%s' cannot be processed\n", file.getName());
                    }
                }
            }
        }

        return bpmnModels;
    }
}
