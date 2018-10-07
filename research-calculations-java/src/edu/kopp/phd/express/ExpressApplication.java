package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.ARISLandscape;
import edu.kopp.phd.express.landscape.BPMNLandscape;
import edu.kopp.phd.express.landscape.DFDLandscape;
import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.search.ModelSimilarity;

import java.util.*;

public class ExpressApplication {

    public static void compareModels() {
        Map<Model, String> allModels = new LinkedHashMap<>();

        for (Model model : new ARISLandscape().getGovernanceLog().getLandscape()) {
            allModels.put(model, "ARISeEPC");
        }

        for (Model model : new BPMNLandscape().getGovernanceLog().getLandscape()) {
            allModels.put(model, "BPMN");
        }

        for (Model model : new DFDLandscape().getGovernanceLog().getLandscape()) {
            allModels.put(model, "DFD");
        }

        for (Map.Entry<Model, String> first : allModels.entrySet()) {
            System.out.printf("%s\t", first.getKey().getName());

            for (Map.Entry<Model, String> second : allModels.entrySet()) {
                System.out.printf("%.2f\t", ModelSimilarity.similarity(first.getKey(), second.getKey(),
                        ModelSimilarity.getCompareWeights(first.getValue(), second.getValue())));
            }

            System.out.println();
        }

        System.out.println("Quality");

        for (Map.Entry<Model, String> first : allModels.entrySet()) {
            for (Map.Entry<Model, String> second : allModels.entrySet()) {
                System.out.printf("%s\t%s\t%.2f\t%.2f\t%.2f\n",
                        first.getKey().getName(),
                        second.getKey().getName(),
                        ModelSimilarity.similarity(first.getKey(), second.getKey(),
                                ModelSimilarity.getCompareWeights(first.getValue(), second.getValue())),
                        first.getKey().connectivity(),
                        second.getKey().connectivity());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("ARIS");
        new ARISLandscape().getGovernanceLog().processEnvironment().ignoreRegulations().analyze();

        System.out.println("eEPC");
        new ARISLandscape().getGovernanceLog().analyze();

        System.out.println("BPMN");
        new BPMNLandscape().getGovernanceLog().analyze();

        System.out.println("DFD");
        new DFDLandscape().getGovernanceLog().analyze();

        System.out.println("Pairwise comparison");
        compareModels();
    }
}
