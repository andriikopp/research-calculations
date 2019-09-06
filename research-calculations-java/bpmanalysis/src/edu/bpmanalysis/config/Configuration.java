package edu.bpmanalysis.config;

import edu.bpmanalysis.search.similarity.JaccardIndex;
import edu.bpmanalysis.search.similarity.api.Index;

public class Configuration {
    public static final boolean DEBUG = false;

    public static final String PATH_TO_BPMN_MODELS = "processModelsStorage/bpmn/";

    public static final Index SIMILARITY_INDEX = new JaccardIndex();

    public static final double SIMILARITY_THRESHOLD = 1.0;

    public static final boolean LOAD_MODELS_BEFORE_START = true;

    public static final boolean CALCULATE_METRICS = false;

    public static final boolean MEASURE_PERFORMANCE = false;

}
