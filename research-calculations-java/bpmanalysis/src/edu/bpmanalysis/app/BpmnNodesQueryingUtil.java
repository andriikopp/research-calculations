package edu.bpmanalysis.app;

import edu.bpmanalysis.config.Configuration;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.data.Row;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BpmnNodesQueryingUtil {
    private Sql2o sql2o;

    private static final String ERROR_MESSAGE = "Error in query syntax";

    public BpmnNodesQueryingUtil() {
        this.sql2o = Configuration.DB_CREDENTIALS;
    }

    public String translateQuery(String query) {
        String[] tokens = query.split("\\s+");

        if (tokens.length != 5) {
            throw new RuntimeException(ERROR_MESSAGE);
        }

        String sql = "select * from bpmn_models where ";
        String condition;

        if (query.contains("Task")) {
            condition = "(node_indeg <> 1 or node_outdeg <> 1)";
        } else if (query.contains("Event")) {
            condition = "(node_indeg > 1 or node_outdeg > 1)";
        } else if (query.contains("XOr") || query.contains("And") || query.contains("Or")) {
            condition = "(not ((node_indeg = 1 and node_outdeg > 1) or (node_indeg > 1 and node_outdeg = 1)))";
        } else {
            throw new RuntimeException(ERROR_MESSAGE);
        }

        if (tokens[1].equals("invalid")) {
            sql += "(" + condition + " and node_type = \"" + tokens[2] + "\")";
        } else if (tokens[1].equals("valid")) {
            sql += "(not " + condition + " and node_type = \"" + tokens[2] + "\")";
        } else if (tokens[1].equals("*")) {
            sql += "(node_type = \"" + tokens[2] + "\")";
        } else {
            throw new RuntimeException(ERROR_MESSAGE);
        }

        sql += " and bpmn_model = \"" + tokens[4] + "\"";

        return sql;
    }

    public List<Map<String, String>> executeQuery(String query) {
        String sql = translateQuery(query);
        List<Map<String, String>> results = new ArrayList<>();

        try (Connection con = sql2o.open()) {
            for (Row row : con.createQuery(sql).executeAndFetchTable().rows()) {
                Map<String, String> record = new LinkedHashMap<>();

                record.put("model", row.getString("bpmn_model"));
                record.put("label", row.getString("node_label"));
                record.put("type", row.getString("node_type"));
                record.put("indeg", row.getString("node_indeg"));
                record.put("outdeg", row.getString("node_outdeg"));

                results.add(record);
            }
        }

        return results;
    }

    public List<String> getAllModels() {
        String sql = "select distinct bpmn_model from bpmn_models";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(String.class);
        }
    }

    public double getModelQuality(String model) {
        double invalidNodes = 0;

        try (Connection con = sql2o.open()) {
            invalidNodes += con.createQuery(translateQuery("select invalid Task from " + model)).executeAndFetchTable().rows().size();
            invalidNodes += con.createQuery(translateQuery("select invalid Event from " + model)).executeAndFetchTable().rows().size();

            invalidNodes += con.createQuery(translateQuery("select invalid XOr from " + model)).executeAndFetchTable().rows().size();
            invalidNodes += con.createQuery(translateQuery("select invalid And from " + model)).executeAndFetchTable().rows().size();
            invalidNodes += con.createQuery(translateQuery("select invalid Or from " + model)).executeAndFetchTable().rows().size();
        }

        double totalNodes = 0;

        try (Connection con = sql2o.open()) {
            totalNodes += con.createQuery("select * from bpmn_models where bpmn_model = :model")
                    .addParameter("model", model).executeAndFetchTable().rows().size();
        }

        return 1.0 - invalidNodes / totalNodes;
    }

    public static void main(String[] args) {
        BpmnNodesQueryingUtil bpmnNodesQueryingUtil = new BpmnNodesQueryingUtil();

        String query = "select invalid Task from Warenversand_adeeaa064e2f4a0f8ea781de0b588225.bpmn";

        System.out.println(bpmnNodesQueryingUtil.translateQuery(query));

        for (Map<String, String> entry : bpmnNodesQueryingUtil.executeQuery(query)) {
            System.out.print(entry.get("model") + "\t");
            System.out.print(entry.get("label") + "\t");
            System.out.print(entry.get("type") + "\t");
            System.out.print(entry.get("indeg") + "\t");
            System.out.print(entry.get("outdeg") + "\n");
        }
    }
}
