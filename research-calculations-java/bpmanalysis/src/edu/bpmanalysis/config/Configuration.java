package edu.bpmanalysis.config;

import org.apache.commons.io.IOUtils;

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
}
