package edu.bpmanalysis.analysis;

import edu.bpmanalysis.analysis.model.EvaluationUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;

import java.util.ArrayList;
import java.util.List;

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

    public static double countBalancing(Model model) {
        double max = 0;
        double size = 0;
        double result = 0;

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(Node.NodeType.FUNCTION)) {
                double arcs = node.getInput() + node.getOutput() + node.getControl() + node.getMechanism();

                if (arcs > max) {
                    max = arcs;
                }

                size++;
            }
        }

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(Node.NodeType.FUNCTION)) {
                double arcs = node.getInput() + node.getOutput() + node.getControl() + node.getMechanism();

                result += arcs - max;
            }
        }

        return Math.abs(result / size);
    }

    public static double countCFC(Model model) {
        double result = 0;

        for (Node node : model.getNodesList()) {
            if (node.getNodeType().equals(Node.NodeType.XOR_CONNECTOR)) {
                result += node.getOutput();
            }

            if (node.getNodeType().equals(Node.NodeType.OR_CONNECTOR)) {
                result += Math.pow(2, node.getOutput()) - 1;
            }

            if (node.getNodeType().equals(Node.NodeType.AND_CONNECTOR)) {
                result += 1;
            }
        }

        return result;
    }

    public static void analyzeModels(ProcessModelRepository processModelRepository) {
        System.out.println("=== Methods comparison ===");

        for (ProcessModelBean processModelBean : processModelRepository.getProcessModels()) {
            double density = countDensity(processModelBean);
            double connectivity = countConnectivity(processModelBean);
            double sequentiality = countSequentiality(processModelBean);

            Model model = ProcessModelAnalysisUtil.transformToModel(processModelBean);

            double balance = countBalancing(model);
            double quality = EvaluationUtil.quality(model);

            System.out.printf("%s\t%f\t%f\t%f\t%f\t%f\n",
                    processModelBean.getName(),
                    density, connectivity, sequentiality, balance,
                    quality);
        }

        System.out.println("==========================");
    }

    public static void measurePerformance(ProcessModelRepository processModelRepository) {
        System.out.println("========= Analysis Performance =========");

        List<ProcessModelBean> processModelBeanList = processModelRepository.getProcessModels();

        long start, end, time;

        int[] executions = {1, 10, 100, 1000, 10000};

        for (int times : executions) {
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                for (ProcessModelBean processModelBean : processModelBeanList)
                    countDensity(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                for (ProcessModelBean processModelBean : processModelBeanList)
                    countConnectivity(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                for (ProcessModelBean processModelBean : processModelBeanList)
                    countSequentiality(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                for (ProcessModelBean processModelBean : processModelBeanList)
                    countDepth(processModelBean);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                for (ProcessModelBean processModelBean : processModelBeanList)
                    EvaluationUtil.quality(ProcessModelAnalysisUtil.transformToModel(processModelBean));

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
        }

        measureOptimizationPerformance(processModelRepository);
    }

    public static void measureOptimizationPerformance(ProcessModelRepository processModelRepository) {
        System.out.println("========= Optimization Performance =========");

        List<ProcessModelBean> processModelBeanList = processModelRepository.getProcessModels();

        List<Model> modelList = new ArrayList<>();

        for (ProcessModelBean processModelBean : processModelBeanList) {
            modelList.add(ProcessModelAnalysisUtil.transformToModel(processModelBean));
        }

        long start, end, time;

        int[] executions = {1, 10, 100, 1000, 10000};

        for (int times : executions) {
            start = System.nanoTime();

            for (int i = 0; i < times; i++)
                for (Model model : modelList)
                    ProcessModelAnalysisUtil.analyzeModel(model);

            end = System.nanoTime();
            time = end - start;
            System.out.println(time);
        }
    }
}
