package edu.bpmanalysis.web.bpmq.util;

import edu.bpmanalysis.web.bpmq.entity.BPModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BPMOntologyUtil {
    public static final Model MODEL = ModelFactory.createDefaultModel();

    public static void processModel(String fileName, BpmnModelInstance modelInstance, BPModel bpModel) {
        Set<String> happenLocation = new HashSet<>();
        Set<String> hasActivity = new HashSet<>();
        Set<String> hasInput = new HashSet<>();
        Set<String> hasOutput = new HashSet<>();
        Set<String> hasStakeholder = new HashSet<>();
        Set<String> happenTime = new HashSet<>();
        Set<String> hasMotivation = new HashSet<>();

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Process.class))) {
            Process process = (Process) modelElementInstance;
            String whereLabel = null;

            for (ModelElementInstance instance : modelInstance.getModelElementsByType(
                    modelInstance.getModel().getType(Participant.class))) {
                Participant participant = (Participant) instance;

                if (participant.getProcess().getId().equals(process.getId())) {
                    whereLabel = participant.getName();
                }
            }

            if (happenLocation != null && !happenLocation.isEmpty()) {
                happenLocation.add(whereLabel);
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Task.class))) {
            Task task = (Task) modelElementInstance;
            String taskName = task.getName();

            if (taskName != null && !taskName.isEmpty()) {
                hasActivity.add(taskName);
            }

            Collection<DataInputAssociation> inputs = task.getDataInputAssociations();
            Collection<DataOutputAssociation> outputs = task.getDataOutputAssociations();

            for (DataInputAssociation input : inputs) {
                ItemAwareElement itemAwareElement = (ItemAwareElement) input.getSources().toArray()[0];
                String sourceType = itemAwareElement.getElementType().getTypeName();
                String sourceName = null;

                if (sourceType.equals("dataStoreReference")) {
                    DataStoreReference dataStoreReference = (DataStoreReference) itemAwareElement;
                    sourceName = dataStoreReference.getName();
                }

                if (sourceType.equals("dataObjectReference")) {
                    DataObjectReference dataObjectReference = (DataObjectReference) itemAwareElement;
                    sourceName = dataObjectReference.getName();
                }

                if (sourceName != null && !sourceName.isEmpty()) {
                    hasInput.add(sourceName);
                }
            }

            for (DataOutputAssociation output : outputs) {
                String targetType = output.getTarget().getElementType().getTypeName();
                String targetName = null;

                if (targetType.equals("dataStoreReference")) {
                    DataStoreReference dataStoreReference = (DataStoreReference) output.getTarget();
                    targetName = dataStoreReference.getName();
                }

                if (targetType.equals("dataObjectReference")) {
                    DataObjectReference dataObjectReference = (DataObjectReference) output.getTarget();
                    targetName = dataObjectReference.getName();
                }

                if (targetName != null && !targetName.isEmpty()) {
                    hasOutput.add(targetName);
                }
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Lane.class))) {
            Lane lane = (Lane) modelElementInstance;
            String laneName = lane.getName();

            if (laneName != null && !laneName.isEmpty()) {
                hasStakeholder.add(laneName);
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(StartEvent.class))) {
            StartEvent startEvent = (StartEvent) modelElementInstance;
            String startEventName = startEvent.getName();

            if (startEventName != null && !startEventName.isEmpty()) {
                happenTime.add(startEventName);
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Gateway.class))) {
            Gateway gateway = (Gateway) modelElementInstance;
            String gatewayName = gateway.getName();

            if (gatewayName != null && !gatewayName.isEmpty()) {
                hasMotivation.add(gatewayName);
            }
        }

        for (String property : hasInput) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("hasInput"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        for (String property : hasOutput) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("hasOutput"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        for (String property : hasActivity) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("hasActivity"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        for (String property : happenLocation) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("happenLocation"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        for (String property : hasStakeholder) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("hasStakeholder"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        for (String property : happenTime) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("happenTime"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        for (String property : hasMotivation) {
            MODEL.createResource(fileName)
                    .addProperty(MODEL.createProperty("hasMotivation"), property
                            .replace("\n", "")
                            .replace("\r", ""));
        }

        MODEL.createResource(fileName)
                .addProperty(MODEL.createProperty("hasQuality"),
                        BPMGraphMetricsUtil.assessQuality(bpModel.getSoftQuality()));
    }
}
