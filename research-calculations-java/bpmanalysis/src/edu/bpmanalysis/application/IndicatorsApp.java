package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.IndicatorsUtil;
import edu.bpmanalysis.collection.BPMNModels;
import edu.bpmanalysis.collection.DFDModels;
import edu.bpmanalysis.collection.EPCModels;
import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;

public class IndicatorsApp {

    public static void main(String[] args) {
        Models models = new EPCModels();

        for (Model model : models.importModels()) {
            IndicatorsUtil.printIndicators(model);
        }

        models = new BPMNModels();

        for (Model model : models.importModels()) {
            IndicatorsUtil.printIndicators(model);
        }

        models = new DFDModels();

        for (Model model : models.importModels()) {
            IndicatorsUtil.printIndicators(model);
        }
    }
}
