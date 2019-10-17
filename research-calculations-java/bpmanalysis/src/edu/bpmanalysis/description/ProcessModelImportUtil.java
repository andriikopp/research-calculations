package edu.bpmanalysis.description;

import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.web.BPMAIApplication;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static edu.bpmanalysis.config.Configuration.PATH_TO_BPMN_MODELS;

public class ProcessModelImportUtil {

    public static void importModelsFromBPMNDocuments(ProcessModelRepository repository) {
        parseControlFlowModels("bpmn", repository);
    }

    public static void importModelsFromEPCDocuments(ProcessModelRepository repository) {
        parseControlFlowModels("epc", repository);
    }

    public static void importModelsFromIDEF0Documents(ProcessModelRepository repository) {
        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String[] splitFileName = file.getName().split("\\.");
                String fileExtension = splitFileName[splitFileName.length - 1];

                if (fileExtension.equals("idef0")) {
                    System.out.printf("File '%s' processed\n", file.getName());

                    try {
                        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

                        ProcessModelBean processModelBean = new ProcessModelBean();
                        processModelBean.setId(UUID.randomUUID().toString());
                        processModelBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
                        processModelBean.setName(file.getName());
                        processModelBean.setNotation("IDEF0");
                        processModelBean.setLevel("Foundation");
                        processModelBean.setDescription(file.getName());
                        processModelBean.setFileName(file.getName());

                        ProcessModelGraphBean processModelGraphBean = new ProcessModelGraphBean();

                        List<ProcessModelGraphNodeBean> nodeBeans = new ArrayList<>();
                        List<ProcessModelGraphEdgeBean> edgeBeans = new ArrayList<>();

                        Set<String> nodes = new HashSet<>();

                        ModelElementType taskType = modelInstance.getModel().getType(Task.class);

                        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(taskType)) {
                            Task task = (Task) modelElementInstance;

                            Collection<DataInputAssociation> inputs = task.getDataInputAssociations();
                            Collection<DataOutputAssociation> outputs = task.getDataOutputAssociations();

                            for (DataInputAssociation input : inputs) {
                                ItemAwareElement itemAwareElement = (ItemAwareElement) input.getSources().toArray()[0];
                                DataObjectReference dataObjectReference = (DataObjectReference) itemAwareElement;

                                String sourceNodeId;
                                String targetNodeId;

                                if (dataObjectReference.getName() == null) {
                                    sourceNodeId = "interface#_[Id-" + dataObjectReference.getId() + "]";
                                } else {
                                    sourceNodeId = "interface#" + dataObjectReference.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + dataObjectReference.getId() + "]";
                                }

                                nodes.add(sourceNodeId);

                                if (task.getName() == null) {
                                    targetNodeId = "function#_[Id-" + task.getId() + "]";
                                } else {
                                    targetNodeId = "function#" + task.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + task.getId() + "]";
                                }

                                nodes.add(targetNodeId);

                                String arrowType = "inputArc";

                                if (dataObjectReference.getName().startsWith("C_")) {
                                    arrowType = "controlArc";
                                }

                                if (dataObjectReference.getName().startsWith("M_")) {
                                    arrowType = "mechanismArc";
                                }

                                ProcessModelGraphEdgeBean processModelGraphEdgeBean = new ProcessModelGraphEdgeBean();
                                processModelGraphEdgeBean.setId(UUID.randomUUID().toString());
                                processModelGraphEdgeBean.setFrom(sourceNodeId);
                                processModelGraphEdgeBean.setTo(targetNodeId);
                                processModelGraphEdgeBean.setLabel(arrowType);
                                processModelGraphEdgeBean.setArrows("to");

                                edgeBeans.add(processModelGraphEdgeBean);
                            }

                            for (DataOutputAssociation output : outputs) {
                                DataObjectReference dataObjectReference = (DataObjectReference) output.getTarget();

                                String sourceNodeId;
                                String targetNodeId;

                                if (dataObjectReference.getName() == null) {
                                    targetNodeId = "interface#_[Id-" + dataObjectReference.getId() + "]";
                                } else {
                                    targetNodeId = "interface#" + dataObjectReference.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + dataObjectReference.getId() + "]";
                                }

                                nodes.add(targetNodeId);

                                if (task.getName() == null) {
                                    sourceNodeId = "function#_[Id-" + task.getId() + "]";
                                } else {
                                    sourceNodeId = "function#" + task.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + task.getId() + "]";
                                }

                                nodes.add(sourceNodeId);

                                String arrowType = "outputArc";

                                ProcessModelGraphEdgeBean processModelGraphEdgeBean = new ProcessModelGraphEdgeBean();
                                processModelGraphEdgeBean.setId(UUID.randomUUID().toString());
                                processModelGraphEdgeBean.setFrom(sourceNodeId);
                                processModelGraphEdgeBean.setTo(targetNodeId);
                                processModelGraphEdgeBean.setLabel(arrowType);
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
                    } catch (RuntimeException e) {
                        System.err.printf("File '%s' cannot be processed\n", file.getName());
                    }
                }
            }
        }
    }

    public static void importModelsFromDFDDocuments(ProcessModelRepository repository) {
        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String[] splitFileName = file.getName().split("\\.");
                String fileExtension = splitFileName[splitFileName.length - 1];

                if (fileExtension.equals("dfd")) {
                    System.out.printf("File '%s' processed\n", file.getName());

                    try {
                        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

                        ProcessModelBean processModelBean = new ProcessModelBean();
                        processModelBean.setId(UUID.randomUUID().toString());
                        processModelBean.setTimeStamp(new Timestamp(System.currentTimeMillis()).toString());
                        processModelBean.setName(file.getName());
                        processModelBean.setNotation("DFD");
                        processModelBean.setLevel("Foundation");
                        processModelBean.setDescription(file.getName());
                        processModelBean.setFileName(file.getName());

                        ProcessModelGraphBean processModelGraphBean = new ProcessModelGraphBean();

                        List<ProcessModelGraphNodeBean> nodeBeans = new ArrayList<>();
                        List<ProcessModelGraphEdgeBean> edgeBeans = new ArrayList<>();

                        Set<String> nodes = new HashSet<>();

                        ModelElementType taskType = modelInstance.getModel().getType(Task.class);

                        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(taskType)) {
                            Task task = (Task) modelElementInstance;

                            Collection<DataInputAssociation> inputs = task.getDataInputAssociations();
                            Collection<DataOutputAssociation> outputs = task.getDataOutputAssociations();

                            for (DataInputAssociation input : inputs) {
                                ItemAwareElement itemAwareElement = (ItemAwareElement) input.getSources().toArray()[0];

                                String sourceType = itemAwareElement.getElementType().getTypeName();
                                String sourceName = null;
                                String sourceId = null;
                                String type = null;

                                if (sourceType.equals("dataStoreReference")) {
                                    DataStoreReference dataStoreReference = (DataStoreReference) itemAwareElement;
                                    sourceName = dataStoreReference.getName();
                                    sourceId = dataStoreReference.getId();
                                    type = "dataStore";
                                }

                                if (sourceType.equals("dataObjectReference")) {
                                    DataObjectReference dataObjectReference = (DataObjectReference) itemAwareElement;
                                    sourceName = dataObjectReference.getName();
                                    sourceId = dataObjectReference.getId();
                                    type = "externalEntity";
                                }

                                String sourceNodeId;
                                String targetNodeId;

                                if (sourceName == null) {
                                    sourceNodeId = type + "#_[Id-" + sourceId + "]";
                                } else {
                                    sourceNodeId = type + "#" + sourceName
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + sourceId + "]";
                                }

                                nodes.add(sourceNodeId);

                                if (task.getName() == null) {
                                    targetNodeId = "function#_[Id-" + task.getId() + "]";
                                } else {
                                    targetNodeId = "function#" + task.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + task.getId() + "]";
                                }

                                nodes.add(targetNodeId);

                                String arrowType = "dataFlow";

                                ProcessModelGraphEdgeBean processModelGraphEdgeBean = new ProcessModelGraphEdgeBean();
                                processModelGraphEdgeBean.setId(UUID.randomUUID().toString());
                                processModelGraphEdgeBean.setFrom(sourceNodeId);
                                processModelGraphEdgeBean.setTo(targetNodeId);
                                processModelGraphEdgeBean.setLabel(arrowType);
                                processModelGraphEdgeBean.setArrows("to");

                                edgeBeans.add(processModelGraphEdgeBean);
                            }

                            for (DataOutputAssociation output : outputs) {
                                String targetType = output.getTarget().getElementType().getTypeName();
                                String targetName = null;
                                String targetId = null;
                                String type = null;

                                if (targetType.equals("dataStoreReference")) {
                                    DataStoreReference dataStoreReference = (DataStoreReference) output.getTarget();
                                    targetName = dataStoreReference.getName();
                                    targetId = dataStoreReference.getId();
                                    type = "dataStore";
                                }

                                if (targetType.equals("dataObjectReference")) {
                                    DataObjectReference dataObjectReference = (DataObjectReference) output.getTarget();
                                    targetName = dataObjectReference.getName();
                                    targetId = dataObjectReference.getId();
                                    type = "externalEntity";
                                }

                                String sourceNodeId;
                                String targetNodeId;

                                if (targetName == null) {
                                    targetNodeId = type + "#_[Id-" + targetId + "]";
                                } else {
                                    targetNodeId = type + "#" + targetName
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + targetId + "]";
                                }

                                nodes.add(targetNodeId);

                                if (task.getName() == null) {
                                    sourceNodeId = "function#_[Id-" + task.getId() + "]";
                                } else {
                                    sourceNodeId = "function#" + task.getName()
                                            .replace("\n", "")
                                            .replace("\r", "")
                                            .replaceAll("\\s+", "_") +
                                            "_[Id-" + task.getId() + "]";
                                }

                                nodes.add(sourceNodeId);

                                String arrowType = "dataFlow";

                                ProcessModelGraphEdgeBean processModelGraphEdgeBean = new ProcessModelGraphEdgeBean();
                                processModelGraphEdgeBean.setId(UUID.randomUUID().toString());
                                processModelGraphEdgeBean.setFrom(sourceNodeId);
                                processModelGraphEdgeBean.setTo(targetNodeId);
                                processModelGraphEdgeBean.setLabel(arrowType);
                                processModelGraphEdgeBean.setArrows("to");

                                edgeBeans.add(processModelGraphEdgeBean);
                            }
                        }

                        ModelElementType sequenceFlowType = modelInstance.getModel().getType(SequenceFlow.class);

                        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(sequenceFlowType)) {
                            if (modelElementInstance.getElementType().equals(sequenceFlowType)) {
                                SequenceFlow sequenceFlow = (SequenceFlow) modelElementInstance;

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
                                processModelGraphEdgeBean.setLabel("dataFlow");
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
                    } catch (RuntimeException e) {
                        System.err.printf("File '%s' cannot be processed\n", file.getName());
                    }
                }
            }
        }
    }

    public static void importModels() {
        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String srcFilePath = PATH_TO_BPMN_MODELS + file.getName();
                String destFilePath = "bpmanalysis/src/main/resources/web/models/" + file.getName();

                File srcFile = new File(srcFilePath);
                File destFile = new File(destFilePath);

                try {
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void cleanRepository() {
        File jsonResultsFile = new File("processModelsStorage/analysisResults.json");
        jsonResultsFile.delete();

        File jsonModelsFile = new File("processModelsStorage/processModels.json");
        jsonModelsFile.delete();

        File modelsFolder = new File("bpmanalysis/src/main/resources/web/models");

        try {
            FileUtils.deleteDirectory(modelsFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Sql2o sql2o = Configuration.DB_CREDENTIALS;

        try (Connection connection = sql2o.open()) {
            connection.createQuery("insert into d_time (time_id, t_day, t_month, t_year) " +
                    "values (:idParam, DAY(NOW()), MONTH(NOW()), YEAR(NOW()))")
                    .addParameter("idParam", BPMAIApplication.TIME_STAMP_ID)
                    .executeUpdate();
        }
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

        if (type.equals("externalEntity") || type.equals("dataStore")) {
            return "#CCCCFF";
        }

        if (type.equals("interface")) {
            return "#E5CCFF";
        }

        return "#A0A0A0";
    }

    private static void parseControlFlowModels(String extension, ProcessModelRepository repository) {
        File folder;
        folder = new File(PATH_TO_BPMN_MODELS);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String[] splitFileName = file.getName().split("\\.");
                String fileExtension = splitFileName[splitFileName.length - 1];

                if (fileExtension.equals(extension)) {
                    System.out.printf("File '%s' processed\n", file.getName());

                    try {
                        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);

                        Collection<ModelElementInstance> processes = modelInstance.getModelElementsByType(
                                modelInstance.getModel().getType(Process.class)
                        );

                        for (ModelElementInstance modelElementInstance : processes) {
                            Process process = (Process) modelElementInstance;

                            String processName = null;

                            if (extension.equals("bpmn")) {
                                Collection<ModelElementInstance> participants = modelInstance.getModelElementsByType(
                                        modelInstance.getModel().getType(Participant.class)
                                );

                                for (ModelElementInstance instance : participants) {
                                    Participant participant = (Participant) instance;

                                    if (participant.getProcess().getId().equals(process.getId())) {
                                        processName = participant.getName();
                                    }
                                }
                            }

                            if (processName == null) {
                                processName = file.getName();
                            }

                            if (extension.equals("bpmn")) {
                                processName += "_[Id-" + process.getId() + "]";
                            }

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
                        System.err.printf("File '%s' cannot be processed\n", file.getName());
                    }
                }
            }
        }
    }
}
