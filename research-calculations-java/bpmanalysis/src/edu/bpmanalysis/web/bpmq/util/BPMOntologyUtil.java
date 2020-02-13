package edu.bpmanalysis.web.bpmq.util;

import edu.bpmanalysis.web.bpmq.entity.BPModel;
import org.apache.jena.rdf.model.*;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.*;

import static edu.bpmanalysis.web.bpmq.util.BPMEALandscapeUtil.EA_LANDSCAPE;

public class BPMOntologyUtil {
    public static final Model MODEL = ModelFactory.createDefaultModel();

    public static void processModel(String fileName, BpmnModelInstance modelInstance, BPModel bpModel) {
        List<String[]> rdfGraphArcs = new LinkedList<>();

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

            if (whereLabel != null && !whereLabel.isEmpty()) {
                rdfGraphArcs.add(new String[]{"happenLocation", whereLabel, "BusinessProcess"});
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Task.class))) {
            Task task = (Task) modelElementInstance;
            String taskName = task.getName();

            if (taskName != null && !taskName.isEmpty()) {
                rdfGraphArcs.add(new String[]{"hasActivity", taskName, "BusinessFunction"});
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
                    String eaElement = sourceType.equals("dataObjectReference") ? "BusinessObject" : null;
                    rdfGraphArcs.add(new String[]{"hasInput", sourceName, eaElement});
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
                    String eaElement = targetType.equals("dataObjectReference") ? "BusinessObject" : null;
                    rdfGraphArcs.add(new String[]{"hasOutput", targetName, eaElement});
                }
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Lane.class))) {
            Lane lane = (Lane) modelElementInstance;
            String laneName = lane.getName();

            if (laneName != null && !laneName.isEmpty()) {
                rdfGraphArcs.add(new String[]{"hasStakeholder", laneName, "BusinessRole"});
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(StartEvent.class))) {
            StartEvent startEvent = (StartEvent) modelElementInstance;
            String startEventName = startEvent.getName();

            if (startEventName != null && !startEventName.isEmpty()) {
                rdfGraphArcs.add(new String[]{"happenTime", startEventName, null});
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Event.class))) {
            Event event = (Event) modelElementInstance;
            String startEventName = event.getName();

            if (startEventName != null && !startEventName.isEmpty()) {
                rdfGraphArcs.add(new String[]{null, startEventName, "BusinessEvent"});
            }
        }

        for (ModelElementInstance modelElementInstance : modelInstance.getModelElementsByType(
                modelInstance.getModel().getType(Gateway.class))) {
            Gateway gateway = (Gateway) modelElementInstance;
            String gatewayName = gateway.getName();

            if (gatewayName != null && !gatewayName.isEmpty()) {
                rdfGraphArcs.add(new String[]{"hasMotivation", gatewayName, null});
            }
        }

        for (String[] pair : rdfGraphArcs) {
            String label = pair[1]
                    .replaceAll("\\W+", " ")
                    .replaceAll("\\s+", "_")
                    .toLowerCase();

            if (pair[0] != null) {
                MODEL.createResource(fileName).addProperty(MODEL.createProperty(pair[0]), label);
            }

            if (pair[2] != null) {
                EA_LANDSCAPE.put(label, pair[2]);
            }
        }

        MODEL.createResource(fileName)
                .addProperty(MODEL.createProperty("hasQuality"),
                        BPMGraphMetricsUtil.assessQuality(bpModel.getSoftQuality()));
    }
}
