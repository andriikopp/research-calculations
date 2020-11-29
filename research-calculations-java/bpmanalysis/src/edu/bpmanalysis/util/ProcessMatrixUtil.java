package edu.bpmanalysis.util;

import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;

import java.util.*;

public class ProcessMatrixUtil {

    public static int[][] getProcessMatrix(ProcessModelBean bean) {
        Map<String, String> tasks = new HashMap<>();
        Map<String, String> events = new HashMap<>();
        Map<String, String> gateways = new HashMap<>();

        List<String> nodes = new ArrayList<>();

        for (ProcessModelGraphNodeBean node : bean.getGraph().getNodes()) {
            if (node.getLabel().contains("function#")) {
                tasks.put(node.getLabel(), "t" + (tasks.size() + 1));
                nodes.add("t" + tasks.size());
            } else if (node.getLabel().contains("event#")) {
                events.put(node.getLabel(), "e" + (events.size() + 1));
                nodes.add("e" + events.size());
            } else {
                gateways.put(node.getLabel(), "g" + (gateways.size() + 1));
                nodes.add("g" + gateways.size());
            }
        }

        int size = tasks.size() + events.size() + gateways.size();
        int[][] matrix = new int[size][size];

        for (ProcessModelGraphEdgeBean edge : bean.getGraph().getEdges()) {
            String subj = tasks.containsKey(edge.getFrom()) ?
                    tasks.get(edge.getFrom()) :
                    events.containsKey(edge.getFrom()) ?
                            events.get(edge.getFrom()) :
                            gateways.get(edge.getFrom());

            String obj = tasks.containsKey(edge.getTo()) ?
                    tasks.get(edge.getTo()) :
                    events.containsKey(edge.getTo()) ?
                            events.get(edge.getTo()) :
                            gateways.get(edge.getTo());

            int row = nodes.indexOf(subj);
            int col = nodes.indexOf(obj);

            matrix[row][col] = 1;
        }

        System.out.print(bean.getFileName() + "\t");

        for (int i = 0; i < size; i++) {
            System.out.print(nodes.get(i) + "\t");
        }

        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print(nodes.get(i) + "\t");

            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + "\t");
            }

            System.out.println();
        }

        return matrix;
    }
}
