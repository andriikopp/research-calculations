package edu.bpmanalysis.util;

import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphEdgeBean;
import edu.bpmanalysis.web.model.bean.ProcessModelGraphNodeBean;

import java.util.*;

public class BpmnRdfStatementsUtil {
    public static Map<String, Set<String>> patterns = new HashMap<>();
    public static List<String> processes = new ArrayList<>();

    public static void printRdfStatements(ProcessModelBean bean) {
        String predicate = ";flowsTo;";

        for (ProcessModelGraphEdgeBean edge : bean.getGraph().getEdges()) {
            String subject = edge.getFrom().split("_\\[Id-")[0];
            String object = edge.getTo().split("_\\[Id-")[0];

            String statement = subject + predicate + object;

            if (patterns.containsKey(statement)) {
                Set<String> models = patterns.get(statement);
                models.add(bean.getFileName());
            } else {
                Set<String> models = new HashSet<>();
                models.add(bean.getFileName());
                patterns.put(statement, models);
            }
        }
    }

    public static void printFormalProcess(ProcessModelBean bean) {
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

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 0;
            }
        }

        // fill matrix
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

        // count sources
        int sources = 0;
        int lastSource = -1;

        for (int i = 0; i < size; i++) {
            if (nodes.get(i).contains("e")) {
                int outputs = 0;

                for (int j = 0; j < size; j++) {
                    outputs += matrix[i][j];
                }

                if (outputs == 1) {
                    sources++;
                    lastSource = i;
                }
            }
        }

        // count sinks
        int sinks = 0;
        int lastSink = 0;

        for (int i = 0; i < size; i++) {
            if (nodes.get(i).contains("e")) {
                int inputs = 0;

                for (int j = 0; j < size; j++) {
                    inputs += matrix[j][i];
                }

                if (inputs == 1) {
                    sinks++;
                    lastSink = i;
                }
            }
        }

        // WF compliance vector
        int[] compliance = {
                sources == 1 ? 1 : 0,
                sinks == 1 ? 1 : 0,
                0
        };

        if (sources == 1 && sinks == 1) {
            // find path from source to sink
            int visited[] = new int[size];

            Queue<Integer> queue = new PriorityQueue<>();
            queue.add(lastSource);

            while (!queue.isEmpty()) {
                int node = queue.poll();
                visited[node] = 2;

                for (int j = 0; j < size; j++) {
                    if (matrix[node][j] == 1 && visited[j] == 0) {
                        queue.add(j);
                        visited[j] = 1;
                    }
                }
            }

            compliance[2] = visited[lastSink] == 2 ? 1 : 0;
        }

        // prepare output
        String process = "";

        for (int i = 0; i < size; i++) {
            process += nodes.get(i) + " = " + Arrays.toString(matrix[i]) + ", ";
        }

        process += "WF = " + Arrays.toString(compliance);
        processes.add(process);
    }
}
