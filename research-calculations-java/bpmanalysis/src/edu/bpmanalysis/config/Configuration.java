package edu.bpmanalysis.config;

import edu.bpmanalysis.search.similarity.JaccardIndex;
import edu.bpmanalysis.search.similarity.api.Index;

public class Configuration {
    public static final boolean DEBUG = false;

    public static final String PATH_TO_BPMN_MODELS = "processModelsStorage/bpmn/";

    public static final Index SIMILARITY_INDEX = new JaccardIndex();

}
