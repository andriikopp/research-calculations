package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.ARISLandscape;
import edu.kopp.phd.express.landscape.BPMNLandscape;
import edu.kopp.phd.express.landscape.DFDLandscape;
import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.search.ModelSimilarity;
import edu.kopp.phd.express.search.accuracy.SizeBasedAccuracy;
import edu.kopp.phd.express.search.accuracy.api.IAccuracy;

import java.util.*;

public class ExpressApplication {
    private static Map<Model, String> allModels = new LinkedHashMap<>();

    public static void loadAllModels() {
        for (Model model : new ARISLandscape().getGovernanceLog().getLandscape()) {
            allModels.put(model, "ARISeEPC");
        }

        for (Model model : new BPMNLandscape().getGovernanceLog().getLandscape()) {
            allModels.put(model, "BPMN");
        }

        for (Model model : new DFDLandscape().getGovernanceLog().getLandscape()) {
            allModels.put(model, "DFD");
        }
    }

    public static void validateModels() {
        System.out.println("ARIS");
        new ARISLandscape().getGovernanceLog().processEnvironment().ignoreRegulations().analyze();

        System.out.println("eEPC");
        new ARISLandscape().getGovernanceLog().analyze();

        System.out.println("BPMN");
        new BPMNLandscape().getGovernanceLog().analyze();

        System.out.println("DFD");
        new DFDLandscape().getGovernanceLog().analyze();
    }

    public static void compareModels(IAccuracy accuracyImpl) {
        System.out.println("Pairwise comparison");

        for (Map.Entry<Model, String> first : allModels.entrySet()) {
            for (Map.Entry<Model, String> second : allModels.entrySet()) {
                System.out.printf("%s\t%s\t%.2f\t%s\n",
                        first.getKey().getName(),
                        second.getKey().getName(),
                        ModelSimilarity.similarity(first.getKey(), second.getKey(),
                                ModelSimilarity.getCompareWeights(first.getValue(), second.getValue())),
                        accuracyImpl.getPreDefinedSimilarity(first.getKey(), second.getKey()));
            }
        }
    }

    public static void main(String[] args) {
        loadAllModels();

        validateModels();

        compareModels(new SizeBasedAccuracy());
    }
}
