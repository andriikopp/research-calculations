package edu.bpmanalysis.application;

import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;

public class EntropyAnalysis {

    public static void main(String[] args) {
        ProcessModelRepository repository = new ProcessModelRepositoryJsonDB();

        for (ProcessModelBean processModelBean : repository.getProcessModels()) {
            ProcessModelGraphBean graphBean = processModelBean.getGraph();

            double entropy = 0;

            for (ProcessModelGraphEdgeBean edgeBean : graphBean.getEdges()) {
                double outgoing = getOutgoingArcs(edgeBean.getFrom(), graphBean);

                entropy += entropy(edgeBean.getFrom(), outgoing);
            }

            for (ProcessModelGraphNodeBean nodeBean : graphBean.getNodes()) {
                if (getElementType(nodeBean.getId()).equals("OR")) {
                    double outgoing = getOutgoingArcs(nodeBean.getId(), graphBean);

                    entropy += entropy(nodeBean.getId(), outgoing);
                }
            }

            System.out.printf("%s\t%.2f\n", processModelBean.getName(), -entropy);
        }
    }

    public static String getElementType(String elementId) {
        return elementId.split("#")[1];
    }

    public static double getOutgoingArcs(String elementId, ProcessModelGraphBean graphBean) {
        double value = 0;

        for (ProcessModelGraphEdgeBean edgeBean : graphBean.getEdges()) {
            if (edgeBean.getFrom().equals(elementId)) {
                value++;
            }
        }

        return value;
    }

    public static double xor(double degree) {
        return 1 / degree;
    }

    public static double or(double degree) {
        return 1 / (2 * degree - 1);
    }

    public static double and() {
        return 1;
    }

    public static double entropy(String elementId, double degree) {
        if (getElementType(elementId).equals("XOR")) {
            return xor(degree) * Math.log(xor(degree));
        }

        if (getElementType(elementId).equals("OR")) {
            return or(degree) * Math.log(or(degree));
        }

        if (getElementType(elementId).equals("AND")) {
            return and() * Math.log(and());
        }

        return or(degree) * Math.log(or(degree));
    }
}
