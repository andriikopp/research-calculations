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

    public static double countDepth(ProcessModelBean processModelBean) {
        double nodes = countNodes(processModelBean);
        double arcs = countArcs(processModelBean);

        return arcs - nodes + 1;
    }

    public static void analyzeModels(ProcessModelRepository processModelRepository) {
        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            double nodes = countNodes(processModelBean);
            double arcs = countArcs(processModelBean);
            double density = countDensity(processModelBean);
            double connectivity = countConnectivity(processModelBean);
            double sequentiality = countSequentiality(processModelBean);
            double depth = countDepth(processModelBean);

            double quality = EvaluationUtil.quality(ProcessModelAnalysisUtil.transformToModel(processModelBean));

            System.out.printf("%s\t%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n", processModelBean.getName(),
                    0, nodes, arcs, density, connectivity, sequentiality, quality, depth);
        }
    }

    public static void measurePerformance(ProcessModelRepository processModelRepository) {
        ProcessModelBean processModelBean = processModelRepository.getProcessModels().get(0);

        long start, end, time;

        int[] executions = {1, 10, 100, 1000, 10000};

        for (int times : executions) {
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                countDensity(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                countConnectivity(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                countSequentiality(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                countDepth(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                EvaluationUtil.quality(ProcessModelAnalysisUtil.transformToModel(processModelBean));

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
        }
    }
}
