package edu.bpmanalysis.config;

import edu.bpmanalysis.web.model.AnalysisResultsRepositoryMySQL;
import edu.bpmanalysis.web.model.ProcessModelRepositoryMySQL;
import edu.bpmanalysis.web.model.api.AnalysisResultsRepository;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import org.apache.commons.io.IOUtils;
import org.sql2o.Sql2o;

import java.io.FileReader;
import java.io.IOException;

public class Configuration {
    public static String PATH_TO_BPMN_MODELS = null;

    static {
        try {
            PATH_TO_BPMN_MODELS = IOUtils
                    .toString(new FileReader("process-models-path.conf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final boolean LOAD_MODELS_BEFORE_START = true;

    public static final Sql2o DB_CREDENTIALS = new Sql2o(
            "jdbc:mysql://localhost:3306/bpmai?useUnicode=yes&characterEncoding=UTF-8",
            "root",
            ""
    );

    public static final ProcessModelRepository MODEL_STORAGE =
            new ProcessModelRepositoryMySQL();

    public static final AnalysisResultsRepository ANALYSIS_STORAGE =
            new AnalysisResultsRepositoryMySQL();
}
