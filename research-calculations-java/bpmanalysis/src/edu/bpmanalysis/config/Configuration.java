package edu.bpmanalysis.config;

import edu.bpmanalysis.search.similarity.JaccardIndex;
import edu.bpmanalysis.search.similarity.api.Index;
import org.apache.commons.io.IOUtils;

import java.io.FileReader;
import java.io.IOException;

public class Configuration {
    public static final boolean DEBUG = false;

    public static String PATH_TO_BPMN_MODELS = null;

    static {
        try {
            PATH_TO_BPMN_MODELS = IOUtils
                    .toString(new FileReader("process-models-path.conf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final Index SIMILARITY_INDEX = new JaccardIndex();

    public static final double SIMILARITY_THRESHOLD = 1.0;

    public static final boolean LOAD_MODELS_BEFORE_START = true;

    public static final boolean CALCULATE_METRICS = false;

    public static final boolean MEASURE_PERFORMANCE = false;

}
