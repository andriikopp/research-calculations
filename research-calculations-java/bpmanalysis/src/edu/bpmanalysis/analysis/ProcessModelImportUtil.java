package edu.bpmanalysis.analysis;

import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

public class ProcessModelImportUtil {
    public static final String PATH_TO_BPMN_MODELS = "processModelsStorage/bpmn/";

    private static Map<String, String> bpmnToInternalNodesMapping = new HashMap<>();

    static {
        bpmnToInternalNodesMapping.put("startEvent", "event");
        bpmnToInternalNodesMapping.put("endEvent", "event");
        bpmnToInternalNodesMapping.put("task", "function");
        bpmnToInternalNodesMapping.put("exclusiveGateway", "XOR");
        bpmnToInternalNodesMapping.put("parallelGateway", "AND");
    }

    public static void importModelsFromBPMNDocuments(ProcessModelRepository repository) {
        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());

                try {
                    BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

                    ProcessModelBean processModelBean = new ProcessModelBean();
                    processModelBean.setId(UUID.randomUUID().toString());
                    processModelBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
                    processModelBean.setName(file.getName());
                    processModelBean.setNotation("BPMN");
                    processModelBean.setLevel("Foundation");
                    processModelBean.setDescription(file.getName());

                    ProcessModelGraphBean processModelGraphBean = new ProcessModelGraphBean();

                    List<ProcessModelGraphNodeBean> nodeBeans = new ArrayList<>();
                    List<ProcessModelGraphEdgeBean> edgeBeans = new ArrayList<>();

                    ModelElementType sequenceFlowType = modelInstance.getModel().getType(SequenceFlow.class);
                    Collection<ModelElementInstance> sequenceFlowInstances = modelInstance.getModelElementsByType(sequenceFlowType);

                    Set<String> nodes = new HashSet<>();

                    for (ModelElementInstance modelElementInstance : sequenceFlowInstances) {
                        SequenceFlow sequenceFlow = (SequenceFlow) modelElementInstance;

                        FlowNode source = sequenceFlow.getSource();
                        FlowNode target = sequenceFlow.getTarget();

                        String sourceType = source.getElementType().getTypeName();
                        String targetType = target.getElementType().getTypeName();

                        String sourceNodeId;
                        String targetNodeId;

                        if (source.getName() == null) {
                            sourceNodeId = getNodeTypeByBPMNType(sourceType) + "#" + source.getId();
                        } else {
                            sourceNodeId = getNodeTypeByBPMNType(sourceType) + "#" + source.getName();
                        }

                        nodes.add(sourceNodeId);

                        if (target.getName() == null) {
                            targetNodeId = getNodeTypeByBPMNType(targetType) + "#" + target.getId();
                        } else {
                            targetNodeId = getNodeTypeByBPMNType(targetType) + "#" + target.getName();
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

                    for (String nodeId : nodes) {
                        ProcessModelGraphNodeBean processModelGraphNodeBean = new ProcessModelGraphNodeBean();
                        processModelGraphNodeBean.setId(nodeId);
                        processModelGraphNodeBean.setLabel(nodeId);
                        processModelGraphNodeBean.setColor(getNodeColorById(nodeId));

                        nodeBeans.add(processModelGraphNodeBean);
                    }

                    processModelGraphBean.setNodes(nodeBeans);
                    processModelGraphBean.setEdges(edgeBeans);

                    processModelBean.setGraph(processModelGraphBean);

                    repository.addProcessModel(processModelBean);
                } catch (RuntimeException e) {
                    System.err.printf("%s cannot be parsed\n", file.getName());
                }
            }
        }
    }

    private static String getNodeTypeByBPMNType(String type) {
        if (type.contains("intermediate")) {
            return "event";
        }

        return bpmnToInternalNodesMapping.get(type);
    }

    private static String getNodeColorById(String nodeId) {
        String type = nodeId.split("#")[0];

        if (type.equals("function")) {
            return "#66FF66";
        }

        if (type.equals("event")) {
            return "#FF9999";
        }

        return "#A0A0A0";
    }
}
