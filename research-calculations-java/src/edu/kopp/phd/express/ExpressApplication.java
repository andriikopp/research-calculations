package edu.kopp.phd.express;

import edu.kopp.phd.express.landscape.ARISLandscape;
import edu.kopp.phd.express.landscape.BPMNLandscape;
import edu.kopp.phd.express.landscape.DFDLandscape;
import edu.kopp.phd.express.landscape.validation.*;
import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.RelaxedImprovement;
import edu.kopp.phd.express.metamodel.RelaxedValidation;
import edu.kopp.phd.express.metamodel.WeightedBalanceAnalysis;
import edu.kopp.phd.express.metamodel.entity.*;
import edu.kopp.phd.express.search.ModelSimilarity;
import edu.kopp.phd.express.search.PatternMatching;
import edu.kopp.phd.express.search.accuracy.ConnectivityBasedAccuracy;
import edu.kopp.phd.express.search.accuracy.DensityBasedAccuracy;
import edu.kopp.phd.express.search.accuracy.SizeBasedAccuracy;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExpressApplication {
    private static Map<Model, String> allModels = new LinkedHashMap<>();

    private static Node[] fault = {
            new Event("CF-ERR-1", 0, 0),
            new Event("CF-ERR-2", 1, 2),
            new Event("CF-ERR-3", 2, 1),
            new Event("CF-ERR-4", 2, 2),

            new Connector("CF-ERR-5", 1, 1, null),
            new Connector("CF-ERR-6", 0, 1, null),
            new Connector("CF-ERR-7", 1, 0, null),
            new Connector("CF-ERR-8", 2, 2, null),

            new ProcessInterface("CF-ERR-9", 1, 1),
            new ProcessInterface("CF-ERR-10", 0, 0),

            new DataStore("DF-ERR-1", 0, 1),
            new DataStore("DF-ERR-2", 1, 0),
            new DataStore("DF-ERR-3", 0, 0),

            new ExternalEntity("DF-ERR-4", 0, 0),
            new ExternalEntity("DF-ERR-5", 1, 1)
    };

    private static Node[] correct = {
            new Event("NDF-5", 0, 1),
            new Event("NDF-6", 1, 0),
            new Event("NDF-7", 1, 1),

            new Connector("NDF-8", 1, 2, null),
            new Connector("NDF-9", 2, 1, null),

            new ProcessInterface("NDF-10", 0, 1),
            new ProcessInterface("NDF-11", 1, 0),

            new Function("NDF-12", 1, 1),

            new DataStore("NDF-13", 1, 1),

            new ExternalEntity("NDF-14", 0, 1),
            new ExternalEntity("NDF-15", 1, 0)
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

            System.out.printf("%s\t%.2f\t%d\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%s\n",
                    model.getKey().getName(),
                    model.getKey().countNodes(),
                    model.getKey().getFunctions().size(),
                    model.getKey().density(),
                    model.getKey().connectivity(),
                    model.getKey().balance(),
                    RelaxedValidation.validate(model.getKey(), model.getValue(), false),
                    WeightedBalanceAnalysis.analyze(model.getKey(), model.getValue()),
                    model.getKey().hasIssues() ? "yes" : "no");
        }
    }

    public static void improveModelsRelaxed() {
        System.out.println("Relaxed improvement");

        for (Map.Entry<Model, String> model : allModels.entrySet()) {
            RelaxedImprovement.improve(model.getKey(), model.getValue());
        }
    }

    public static void compareModels() {
        System.out.println("Pairwise comparison");

        for (Map.Entry<Model, String> first : allModels.entrySet()) {
            for (Map.Entry<Model, String> second : allModels.entrySet()) {
                double similarity = ModelSimilarity.similarity(first.getKey(), second.getKey(),
                        ModelSimilarity.getCompareWeights(first.getValue(), second.getValue()));

                System.out.printf("%s\t%s\t%.2f\t%s\t%s\t%s\n",
                        first.getKey().getName(),
                        second.getKey().getName(),
                        similarity,
                        new SizeBasedAccuracy().getPreDefinedSimilarity(first.getKey(), second.getKey()),
                        new DensityBasedAccuracy().getPreDefinedSimilarity(first.getKey(), second.getKey()),
                        new ConnectivityBasedAccuracy().getPreDefinedSimilarity(first.getKey(), second.getKey()));
            }
        }
    }

    public static void patternMatching() {
        System.out.println("Pattern matching & Machine learning");

        PatternMatching.train(fault, correct);

        for (Map.Entry<Model, String> model : allModels.entrySet()) {
            model.getKey().enableEnvironment();

            double numberOfDefects = RelaxedValidation.validate(model.getKey(), model.getValue(), false);
            double foundByPatternMatching = PatternMatching.match(model.getKey());

            System.out.printf("%s\t%.2f\t%d\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%s\n",
                    model.getKey().getName(),
                    model.getKey().countNodes(),
                    model.getKey().getFunctions().size(),
                    model.getKey().density(),
                    model.getKey().connectivity(),
                    model.getKey().balance(),
                    numberOfDefects,
                    foundByPatternMatching,
                    numberOfDefects + foundByPatternMatching,
                    WeightedBalanceAnalysis.analyze(model.getKey(), model.getValue()),
                    model.getKey().hasIssues() ? "yes" : "no");
        }
    }

    public static void main(String[] args) {
        loadAllModels();

        loadValidationModels();

        loadRandomValidationModels();

        validateModels();

        validateModelsRelaxed();

        improveModelsRelaxed();

        compareModels();

        patternMatching();
    }
}
