package edu.bpmanalysis.web.model;

import com.google.gson.Gson;
import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.web.BPMAIApplication;
import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResultsRepositoryMySQL implements AnalysisResultsRepository {
    private Sql2o sql2o;

    public AnalysisResultsRepositoryMySQL() {
        this.sql2o = Configuration.DB_CREDENTIALS;
    }

    @Override
    public void addAnalysisResult(ProcessModelAnalysisBean processModelAnalysisBean) {
        String query = "insert into f_analysis (model_id, results, size, functions, connectorsBalance, " +
                "functionsBalance, startEvents, endEvents, mismatch, orConnectors, notation_id, time_id, quality) " +
                "values (:idParam, :resultsParam, :sizeParam, :functionsParam, :connBalParam, " +
                ":funcBalParam, :startParam, :endParam, :mismatchParam, :orConnParam, :notParam, :timeId, :qualityParam)";

        try (Connection con = sql2o.open()) {
            con.createQuery(query)
                    .addParameter("idParam", processModelAnalysisBean.getId())
                    .addParameter("resultsParam", new Gson().toJson(processModelAnalysisBean))
                    .addParameter("sizeParam", processModelAnalysisBean.getSize())
                    .addParameter("functionsParam", processModelAnalysisBean.getFunctions())
                    .addParameter("connBalParam", processModelAnalysisBean.getConnectorsBalance())
                    .addParameter("funcBalParam", processModelAnalysisBean.getFunctionsBalance())
                    .addParameter("startParam", processModelAnalysisBean.getStartEvents())
                    .addParameter("endParam", processModelAnalysisBean.getEndEvents())
                    .addParameter("mismatchParam", processModelAnalysisBean.getMismatch())
                    .addParameter("orConnParam", processModelAnalysisBean.getOrConnectors())
                    .addParameter("notParam", getNotationId(
                            processModelAnalysisBean.getNotation()))
                    .addParameter("timeId", BPMAIApplication.TIME_STAMP_ID)
                    .addParameter("qualityParam", processModelAnalysisBean.getQuality())
                    .executeUpdate();
        }
    }

    @Override
    public List<ProcessModelAnalysisBean> getAnalysisResults() {
        String query = "select results from f_analysis order by tstamp desc";

        try (Connection con = sql2o.open()) {
            List<String> stringResults = con.createQuery(query).executeScalarList(String.class);
            List<ProcessModelAnalysisBean> beansResults = new ArrayList<>();

            for (String result : stringResults) {
                beansResults.add(new Gson().fromJson(result, ProcessModelAnalysisBean.class));
            }

            return beansResults;
        }
    }

    @Override
    public ProcessModelAnalysisBean getAnalysisResult(String id) {
        String query = "SELECT results FROM f_analysis WHERE model_id = :idParam";

        try (Connection con = sql2o.open()) {
            String stringResult = con.createQuery(query)
                    .addParameter("idParam", id)
                    .executeScalar(String.class);

            return new Gson().fromJson(stringResult, ProcessModelAnalysisBean.class);
        }
    }

    public static int getNotationId(String notation) {
        switch (notation) {
            case "BPMN":
                return 1;
            case "EPC":
                return 2;
            case "IDEF0":
                return 3;
            case "DFD":
                return 4;
            default:
                return 0;
        }
    }
}
