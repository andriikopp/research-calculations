package edu.bpmanalysis.description;

import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static edu.bpmanalysis.config.Configuration.PATH_TO_BPMN_MODELS;

public class ProcessModelImportUtil {

    public static void importModelsFromBPMNDocuments(ProcessModelRepository repository) {
        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());

                try {
                    BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

                    Collection<ModelElementInstance> processes = modelInstance.getModelElementsByType(
                            modelInstance.getModel().getType(Process.class)
                    );

                    for (ModelElementInstance modelElementInstance : processes) {
                        Process process = (Process) modelElementInstance;

                        String processName;

                        if (process.getName() == null) {
                            processName = file.getName();
                        } else {
                            processName = process.getName();
                        }

                        processName += "_[Id-" + process.getId() + "]";

                        ProcessModelBean processModelBean = new ProcessModelBean();
                        processModelBean.setId(UUID.randomUUID().toString());
                        processModelBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
                        processModelBean.setName(processName);
                        processModelBean.setNotation("BPMN");
                        processModelBean.setLevel("Foundation");
                        processModelBean.setDescription(file.getName());

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
                                    sourceNodeId = getNodeTypeByBPMNType(sourceType) +
                                            "#_[Id-" + source.getId() + "]";
                                } else {
                                    sourceNodeId = getNodeTypeByBPMNType(sourceType) + "#" + source.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + source.getId() + "]";
                                }

                                nodes.add(sourceNodeId);

                                if (target.getName() == null) {
                                    targetNodeId = getNodeTypeByBPMNType(targetType) +
                                            "#_[Id-" + target.getId() + "]";
                                } else {
                                    targetNodeId = getNodeTypeByBPMNType(targetType) + "#" + target.getName()
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
                            processModelGraphNodeBean.setColor(getNodeColorById(nodeId));

                            nodeBeans.add(processModelGraphNodeBean);
                        }

                        processModelGraphBean.setNodes(nodeBeans);
                        processModelGraphBean.setEdges(edgeBeans);

                        processModelBean.setGraph(processModelGraphBean);

                        repository.addProcessModel(processModelBean);
                    }
                } catch (RuntimeException e) {
                    System.err.printf("%s cannot be parsed\n", file.getName());
                }
            }
        }
    }

    public static void cleanRepository() {
        File jsonModelsFile = new File("processModelsStorage/processModels.json");
        jsonModelsFile.delete();

        File rdfGraphFile = new File("processModelsStorage/graph.rdf");
        rdfGraphFile.delete();
    }

    public static String getNodeTypeByBPMNType(String type) {
        if (type.toLowerCase().contains("task".toLowerCase()) ||
                type.toLowerCase().contains("subProcess".toLowerCase())) {
            return "function";
        }

        if (type.toLowerCase().contains("event".toLowerCase())) {
            return "event";
        }

        if (type.toLowerCase().contains("gateway".toLowerCase())) {
            if (type.toLowerCase().contains("parallel".toLowerCase())) {
                return "AND";
            }

            if (type.toLowerCase().contains("inclusive".toLowerCase())) {
                return "OR";
            }

            return "XOR";
        }

        return null;
    }

    public static String getNodeColorById(String nodeId) {
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
