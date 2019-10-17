package edu.bpmanalysis.web.model;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.web.model.api.RecommendationsRepository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.UUID;

public class RecommendationsRepositoryMySQL implements RecommendationsRepository {
    private Sql2o sql2o;

    public RecommendationsRepositoryMySQL() {
        this.sql2o = Configuration.DB_CREDENTIALS;
    }

    @Override
    public void addRecommendations(ProcessModelAnalysisBean processModelAnalysisBean) {
        String query = "insert into f_recommendations (model_id, recommendation_id, recommendation) " +
                "values (:idParam, :recommIdParam, :recommParam)";

        try (Connection con = sql2o.open()) {
            for (String recommendation : processModelAnalysisBean.getRecommendations()) {
                con.createQuery(query)
                        .addParameter("idParam", processModelAnalysisBean.getId())
                        .addParameter("recommIdParam", UUID.randomUUID().toString())
                        .addParameter("recommParam", recommendation)
                        .executeUpdate();
            }
        }
    }
}
