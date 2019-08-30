package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;

public final class GraphMetricsUtil {

    private GraphMetricsUtil() {
    }

    public static int countNodes(ProcessModelBean processModelBean) {
        return processModelBean.getGraph().getNodes().size();
    }

    public static int countArcs(ProcessModelBean processModelBean) {
        return processModelBean.getGraph().getEdges().size();
    }

    public static double countDensity(ProcessModelBean processModelBean) {
        double nodes = countNodes(processModelBean);
        double arcs = countArcs(processModelBean);

        return arcs / (nodes * (nodes - 1));
    }

    public static double countConnectivity(ProcessModelBean processModelBean) {
        double nodes = countNodes(processModelBean);
        double arcs = countArcs(processModelBean);

        return arcs / nodes;
    }

    public static double countSequentiality(ProcessModelBean processModelBean) {
        double arcs = countArcs(processModelBean);
        double arcsBetweenFunctions = 0;


        for (ProcessModelGraphEdgeBean edge : processModelBean.getGraph().getEdges()) {
            if (edge.getFrom().contains("function") && edge.getTo().contains("function")) {
                arcsBetweenFunctions++;
            }
        }

        return arcsBetweenFunctions / arcs;
    }

    public static void analyzeModels(ProcessModelRepository processModelRepository) {
        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            double size = countNodes(processModelBean);
            double density = countDensity(processModelBean);
            double connectivity = countConnectivity(processModelBean);
            double sequentiality = countSequentiality(processModelBean);

            double quality = EvaluationUtil.quality(ProcessModelAnalysisUtil.transformToModel(processModelBean));

            System.out.printf("%s\t%f\t%f\t%f\t%f\t%f\n", processModelBean.getName(),
                    size, density, connectivity, sequentiality, quality);
        }
    }
}
