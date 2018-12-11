package edu.kopp.phd.auto_xpdl;

import org.enhydra.jxpdl.XMLInterface;
import org.enhydra.jxpdl.XMLInterfaceImpl;
import org.enhydra.jxpdl.XMLUtil;
import org.enhydra.jxpdl.elements.Activity;
import org.enhydra.jxpdl.elements.Package;
import org.enhydra.jxpdl.elements.Transition;
import org.enhydra.jxpdl.elements.WorkflowProcess;

import java.io.File;
import java.util.Set;
import java.util.UUID;

public class XPDLTransformationTool {
    public static final int TASK_TYPE = 2;
    public static final int GATEWAY_TYPE = 0;

    public static int getActivityInputs(WorkflowProcess workflowProcess, Activity activity) {
        int activityInputs = 0;

        for (int k = 0; k < workflowProcess.getTransitions().size(); k++) {
            Transition transition = (Transition) workflowProcess.getTransitions().get(k);

            if (transition.getTo().equals(activity.getId())) {
                activityInputs++;
            }
        }

        return activityInputs;
    }

    public static int getActivityOutputs(WorkflowProcess workflowProcess, Activity activity) {
        int activityOutputs = 0;

        for (int k = 0; k < workflowProcess.getTransitions().size(); k++) {
            Transition transition = (Transition) workflowProcess.getTransitions().get(k);

            if (transition.getFrom().equals(activity.getId())) {
                activityOutputs++;
            }
        }

        return activityOutputs;
    }

    public static Activity getStartEvent(WorkflowProcess workflowProcess) {
        for (int k = 0; k < workflowProcess.getActivities().size(); k++) {
            Activity event = (Activity) workflowProcess.getActivities().get(k);

            if (event.getActivityType() == 6) {
                return event;
            }
        }

        return null;
    }

    public static Activity getEndEvent(WorkflowProcess workflowProcess) {
        for (int k = 0; k < workflowProcess.getActivities().size(); k++) {
            Activity event = (Activity) workflowProcess.getActivities().get(k);

            if (event.getActivityType() == 7) {
                return event;
            }
        }

        return null;
    }

    public static Activity getTaskWithoutInputs(WorkflowProcess workflowProcess) {
        for (int k = 0; k < workflowProcess.getActivities().size(); k++) {
            Activity activity = (Activity) workflowProcess.getActivities().get(k);

            if (activity.getActivityType() == 2 && getActivityInputs(workflowProcess, activity) == 0) {
                return activity;
            }
        }

        return null;
    }

    public static Activity getTaskWithoutOutputs(WorkflowProcess workflowProcess) {
        for (int k = 0; k < workflowProcess.getActivities().size(); k++) {
            Activity activity = (Activity) workflowProcess.getActivities().get(k);

            if (activity.getActivityType() == 2 && getActivityOutputs(workflowProcess, activity) == 0) {
                return activity;
            }
        }

        return null;
    }

    public static void fixMissingSequenceFlowAndStartEvent(WorkflowProcess workflowProcess) {
        for (int j = 0; j < workflowProcess.getActivities().size(); j++) {
            Activity activity = (Activity) workflowProcess.getActivities().get(j);

            int activityType = activity.getActivityType();

            if (activityType == TASK_TYPE) {
                Activity existingStartEvent = getStartEvent(workflowProcess);

                if (getActivityInputs(workflowProcess, activity) == 0) {
                    if (existingStartEvent == null) {
                        String startEventId = UUID.randomUUID().toString();

                        Activity newStartEvent = (Activity) workflowProcess.getActivities().generateNewElement();
                        newStartEvent.setId(startEventId);
                        newStartEvent.getActivityTypes().setEvent();
                        newStartEvent.getActivityTypes().getEvent().getEventTypes().setStartEvent();

                        workflowProcess.getActivities().add(newStartEvent);

                        Transition startEventTransition = (Transition) workflowProcess.getTransitions().generateNewElement();
                        startEventTransition.setId(UUID.randomUUID().toString());
                        startEventTransition.setFrom(startEventId);
                        startEventTransition.setTo(activity.getId());

                        workflowProcess.getTransitions().add(startEventTransition);
                    } else {
                        Activity existingTaskWithoutOutputs = getTaskWithoutOutputs(workflowProcess);

                        Transition startEventTransition = (Transition) workflowProcess.getTransitions().generateNewElement();
                        startEventTransition.setId(UUID.randomUUID().toString());

                        if (existingTaskWithoutOutputs == null) {
                            startEventTransition.setFrom(existingStartEvent.getId());
                        } else {
                            startEventTransition.setFrom(existingTaskWithoutOutputs.getId());
                        }

                        startEventTransition.setTo(activity.getId());

                        workflowProcess.getTransitions().add(startEventTransition);
                    }
                }
            }
        }
    }

    public static void fixMissingSequenceFlowAndEndEvents(WorkflowProcess workflowProcess) {
        for (int j = 0; j < workflowProcess.getActivities().size(); j++) {
            Activity activity = (Activity) workflowProcess.getActivities().get(j);

            int activityType = activity.getActivityType();

            if (activityType == TASK_TYPE) {
                Activity existingEndEvent = getEndEvent(workflowProcess);

                if (getActivityOutputs(workflowProcess, activity) == 0) {
                    if (existingEndEvent == null) {
                        String endEventId = UUID.randomUUID().toString();

                        Activity newEndEvent = (Activity) workflowProcess.getActivities().generateNewElement();
                        newEndEvent.setId(endEventId);
                        newEndEvent.getActivityTypes().setEvent();
                        newEndEvent.getActivityTypes().getEvent().getEventTypes().setEndEvent();

                        workflowProcess.getActivities().add(newEndEvent);

                        Transition endEventTransition = (Transition) workflowProcess.getTransitions().generateNewElement();
                        endEventTransition.setId(UUID.randomUUID().toString());
                        endEventTransition.setFrom(activity.getId());
                        endEventTransition.setTo(endEventId);

                        workflowProcess.getTransitions().add(endEventTransition);
                    } else {
                        Activity existingTaskWithoutInputs = getTaskWithoutInputs(workflowProcess);

                        Transition endEventTransition = (Transition) workflowProcess.getTransitions().generateNewElement();
                        endEventTransition.setId(UUID.randomUUID().toString());
                        endEventTransition.setFrom(activity.getId());

                        if (existingTaskWithoutInputs == null) {
                            endEventTransition.setTo(existingEndEvent.getId());
                        } else {
                            endEventTransition.setTo(existingTaskWithoutInputs.getId());
                        }

                        workflowProcess.getTransitions().add(endEventTransition);
                    }
                }
            }
        }
    }

    public static void fixMissingJoinGateways(WorkflowProcess workflowProcess) {
        for (int j = 0; j < workflowProcess.getActivities().size(); j++) {
            Activity activity = (Activity) workflowProcess.getActivities().get(j);

            int activityType = activity.getActivityType();

            if (activityType != GATEWAY_TYPE) {
                if (getActivityInputs(workflowProcess, activity) > 1) {
                    String joinGatewayId = UUID.randomUUID().toString();

                    Activity newJoinGateway = (Activity) workflowProcess.getActivities().generateNewElement();
                    newJoinGateway.setId(joinGatewayId);
                    newJoinGateway.getActivityTypes().setRoute();

                    workflowProcess.getActivities().add(newJoinGateway);

                    Set<Transition> incomingTransitions = XMLUtil.getIncomingTransitions(activity);

                    for (Transition transition : incomingTransitions) {
                        transition.setTo(joinGatewayId);
                    }

                    Transition outgoingTransition = (Transition) workflowProcess.getTransitions().generateNewElement();
                    outgoingTransition.setId(UUID.randomUUID().toString());
                    outgoingTransition.setFrom(joinGatewayId);
                    outgoingTransition.setTo(activity.getId());

                    workflowProcess.getTransitions().add(outgoingTransition);
                }
            }
        }
    }

    public static void fixMissingSplitGateways(WorkflowProcess workflowProcess) {
        for (int j = 0; j < workflowProcess.getActivities().size(); j++) {
            Activity activity = (Activity) workflowProcess.getActivities().get(j);

            int activityType = activity.getActivityType();

            if (activityType != GATEWAY_TYPE) {
                if (getActivityOutputs(workflowProcess, activity) > 1) {
                    String splitGatewayId = UUID.randomUUID().toString();

                    Activity newJoinGateway = (Activity) workflowProcess.getActivities().generateNewElement();
                    newJoinGateway.setId(splitGatewayId);
                    newJoinGateway.getActivityTypes().setRoute();

                    workflowProcess.getActivities().add(newJoinGateway);

                    Set<Transition> outgoingTransitions = XMLUtil.getOutgoingTransitions(activity);

                    for (Transition transition : outgoingTransitions) {
                        transition.setFrom(splitGatewayId);
                    }

                    Transition incomingTransition = (Transition) workflowProcess.getTransitions().generateNewElement();
                    incomingTransition.setId(UUID.randomUUID().toString());
                    incomingTransition.setFrom(activity.getId());
                    incomingTransition.setTo(splitGatewayId);

                    workflowProcess.getTransitions().add(incomingTransition);
                }
            }
        }
    }

    public static void fixModel(String inputFile, String outputFile) throws Exception {
        XMLInterface xmlInterface = new XMLInterfaceImpl();

        File file = new File(inputFile);
        inputFile = file.getCanonicalPath();

        Package modelPackage = xmlInterface.openPackage(inputFile, false);

        for (int i = 0; i < modelPackage.getWorkflowProcesses().size(); i++) {
            WorkflowProcess workflowProcess = (WorkflowProcess) modelPackage.getWorkflowProcesses().get(i);

            int activitiesSize = workflowProcess.getActivities().size();

            if (activitiesSize > 0) {
                fixMissingSequenceFlowAndStartEvent(workflowProcess);
                fixMissingSequenceFlowAndEndEvents(workflowProcess);
                fixMissingJoinGateways(workflowProcess);
                fixMissingSplitGateways(workflowProcess);
            }
        }

        XMLUtil.writeToFile(outputFile, modelPackage);
    }

    public static void main(String[] args) throws Exception {
        fixModel("xpdl-models\\ErrorModel03.xpdl", "xpdl-models\\output.xpdl");
    }
}
