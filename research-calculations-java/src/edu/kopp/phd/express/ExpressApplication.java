package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.ARISLandscape;
import edu.kopp.phd.express.landscape.BPMNLandscape;
import edu.kopp.phd.express.landscape.DFDLandscape;
import edu.kopp.phd.express.landscape.validation.*;
import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.ModelBuilder;
import edu.kopp.phd.express.metamodel.RelaxedImprovement;
import edu.kopp.phd.express.metamodel.RelaxedValidation;
import edu.kopp.phd.express.search.ModelSimilarity;
import edu.kopp.phd.express.search.ProcessControlFlowPatternMatching;
import edu.kopp.phd.express.search.accuracy.SizeBasedAccuracy;
import edu.kopp.phd.express.search.accuracy.api.IAccuracy;
import edu.kopp.phd.express.search.utils.VectorSimilarity;

import java.util.*;

public class ExpressApplication {
    private static Map<Model, String> allModels = new LinkedHashMap<>();

    private static Model[] patterns = {
            ModelBuilder.model("PATTERN 1 [Missing End Event]")
                    .function(1, 0)
                    .finish(),
            ModelBuilder.model("PATTERN 2 [Missing Split Gateway]")
                    .function(1, 2)
                    .finish(),
            ModelBuilder.model("PATTERN 3 [Missing Start Event]")
                    .function(0, 1)
                    .finish(),
            ModelBuilder.model("PATTERN 4 [Missing Join Gateway]")
                    .function(2, 1)
                    .finish(),

            ModelBuilder.model("PATTERN 1 & 4")
                    .function(2, 0)
                    .finish(),
            ModelBuilder.model("PATTERN 2 & 3")
                    .function(0, 2)
                    .finish()
    };

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

    public static void loadValidationModels() {
        for (Model model : new ControlFlowValidation().getGovernanceLog().getLandscape()) {
            allModels.put(model, "BPMN");
        }

        for (Model model : new DataFlowValidation().getGovernanceLog().getLandscape()) {
            allModels.put(model, "DFD");
        }

        for (Model model : new ARISEnvironmentValidation().getGovernanceLog().getLandscape()) {
            allModels.put(model, "ARISeEPC");
        }

        for (Model model : new IDEF0EnvironmentValidation().getGovernanceLog().getLandscape()) {
            allModels.put(model, "IDEF0");
        }
    }

    public static void loadRandomValidationModels() {
        for (Model model : RandomValidationSetGenerator.generateModelsWithDefects(
                RandomValidationSetGenerator.CF_DEFECTS)) {
            allModels.put(model, "BPMN");
        }

        for (Model model : RandomValidationSetGenerator.generateModelsWithDefects(
                RandomValidationSetGenerator.DF_DEFECTS)) {
            allModels.put(model, "DFD");
        }

        for (Model model : RandomValidationSetGenerator.generateModelsWithDefects(
                RandomValidationSetGenerator.HR_DEFECTS)) {
            allModels.put(model, "IDEF0");
        }

        for (Model model : RandomValidationSetGenerator.generateModelsWithDefects(
                RandomValidationSetGenerator.IO_DEFECTS)) {
            allModels.put(model, "IDEF0");
        }

        for (Model model : RandomValidationSetGenerator.generateModelsWithDefects(
                RandomValidationSetGenerator.NHR_DEFECTS)) {
            allModels.put(model, "IDEF0");
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

    public static void validateModelsRelaxed() {
        System.out.println("Relaxed validation");

        for (Map.Entry<Model, String> model : allModels.entrySet()) {
            model.getKey().enableEnvironment();

            System.out.printf("%s\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%s\n",
                    model.getKey().getName(),
                    model.getKey().countNodes(),
                    model.getKey().density(),
                    model.getKey().connectivity(),
                    model.getKey().balance(),
                    RelaxedValidation.validate(model.getKey(), model.getValue(), true),
                    model.getKey().hasIssues() ? "yes" : "no");
        }
    }

    public static void improveModelsRelaxed() {
        System.out.println("Relaxed improvement");

        for (Map.Entry<Model, String> model : allModels.entrySet()) {
            RelaxedImprovement.improve(model.getKey(), model.getValue());
        }
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

    public static void patternMatching() {
        System.out.println("Pattern matching (control flow)");

        for (Map.Entry<Model, String> first : allModels.entrySet()) {
            if (first.getValue().equals("BPMN") || first.getValue().equals("ARISeEPC")) {
                double[] modelImage = new double[patterns.length];

                int patternNum = 0;

                for (Model pattern : patterns) {
                    modelImage[patternNum] = ProcessControlFlowPatternMatching.match(first.getKey(), pattern);
                    patternNum++;
                }

                double evaluation = VectorSimilarity.similarity(modelImage, new double[]{0, 0, 0, 0, 0, 0});

                System.out.printf("%s\t%s\t%.2f\n",
                        first.getKey().getName(),
                        Arrays.toString(modelImage),
                        evaluation);
            }
        }
    }

    public static void main(String[] args) {
        loadAllModels();

        loadValidationModels();

        loadRandomValidationModels();

        validateModels();

        validateModelsRelaxed();

        improveModelsRelaxed();

        compareModels(new SizeBasedAccuracy());

        patternMatching();
    }
}
