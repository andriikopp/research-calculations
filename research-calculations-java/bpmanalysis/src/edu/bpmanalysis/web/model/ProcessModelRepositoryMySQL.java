package edu.bpmanalysis.web.model;

import com.google.gson.Gson;
import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.web.BPMAIApplication;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

public class ProcessModelRepositoryMySQL implements ProcessModelRepository {
    private Sql2o sql2o;

    public ProcessModelRepositoryMySQL() {
        this.sql2o = Configuration.DB_CREDENTIALS;
    }

    @Override
    public List<ProcessModelBean> getProcessModels() {
        String query = "select model from d_model where model_id not in " +
                "(select model_id from f_analysis where time_id <> :timeIdParam)";

        try (Connection con = sql2o.open()) {
            List<String> stringResults = con.createQuery(query)
                    .addParameter("timeIdParam", BPMAIApplication.TIME_STAMP_ID)
                    .executeScalarList(String.class);
            List<ProcessModelBean> beansResults = new ArrayList<>();

            for (String result : stringResults) {
                beansResults.add(new Gson().fromJson(result, ProcessModelBean.class));
            }

            return beansResults;
        }
    }

    @Override
    public void addProcessModel(ProcessModelBean processModelBean) {
        String query = "insert into d_model (model_id, model, name, description, fileName) " +
                "values (:idParam, :modelParam, :nameParam, :descriptionParam, :fileParam)";

        try (Connection con = sql2o.open()) {
            con.createQuery(query)
                    .addParameter("idParam", processModelBean.getId())
                    .addParameter("modelParam", new Gson().toJson(processModelBean))
                    .addParameter("nameParam", processModelBean.getName())
                    .addParameter("descriptionParam", processModelBean.getDescription())
                    .addParameter("fileParam", processModelBean.getFileName())
                    .executeUpdate();
        }
    }
}
