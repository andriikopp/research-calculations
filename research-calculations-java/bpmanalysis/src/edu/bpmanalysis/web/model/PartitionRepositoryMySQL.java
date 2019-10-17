package edu.bpmanalysis.web.model;

import edu.bpmanalysis.analysis.bean.ProcessModelAnalysisBean;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.search.partition.ProcessModelAnalysisResultsPartition;
import edu.bpmanalysis.web.model.api.PartitionRepository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class PartitionRepositoryMySQL implements PartitionRepository {
    private Sql2o sql2o;

    public PartitionRepositoryMySQL() {
        this.sql2o = Configuration.DB_CREDENTIALS;
    }

    @Override
    public void addPartition(ProcessModelAnalysisBean processModelAnalysisBean) {
        String featuresId = ProcessModelAnalysisResultsPartition
                .getModelDescriptionFromArray(processModelAnalysisBean.getGuidelines());
        String featuresDescription = ProcessModelAnalysisResultsPartition
                .getGroupDescription(featuresId);

        String featuresQuery = "insert ignore into d_features (features_id, description) " +
                "values (:idParam, :descrParam)";
        String partitionQuery = "insert into f_partition (model_id, features_id) " +
                "values (:idParam, :featParam)";

        try (Connection con = sql2o.open()) {
            con.createQuery(featuresQuery)
                    .addParameter("idParam", featuresId)
                    .addParameter("descrParam", featuresDescription)
                    .executeUpdate();

            con.createQuery(partitionQuery)
                    .addParameter("idParam", processModelAnalysisBean.getId())
                    .addParameter("featParam", featuresId)
                    .executeUpdate();
        }
    }
}
