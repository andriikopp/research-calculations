package edu.bpmanalysis.application;

import edu.bpmanalysis.collection.BPMNModels;
import edu.bpmanalysis.collection.DFDModels;
import edu.bpmanalysis.collection.EPCModels;
import edu.bpmanalysis.collection.api.Models;
import edu.bpmanalysis.collection.tools.Model;
import edu.bpmanalysis.search.similarity.GraphStructureSimilarity;
import edu.bpmanalysis.search.similarity.api.Similarity;

import java.util.List;

public class SimilarityApp {
    private static Similarity similarity = new GraphStructureSimilarity();

    public static void printSimilarModels(Model first, Model second) {
        double value = similarity.compare(first, second);

        if (value == 1.0 && !first.getName().equals(second.getName())) {
            System.out.printf("%s\t%s\t%.2f\n",
                    first.getName(),
                    second.getName(),
                    value);
        }
    }

    public static void main(String[] args) {

        Models models = new EPCModels();
        List<Model> epcModels = models.importModels();

        System.out.println("EPC Models Similarity");

        for (Model first : epcModels) {
            for (Model second : epcModels) {
                printSimilarModels(first, second);
            }
        }

        models = new BPMNModels();
        List<Model> bpmnModels = models.importModels();

        System.out.println("BPMN Models Similarity");

        for (Model first : bpmnModels) {
            for (Model second : bpmnModels) {
                printSimilarModels(first, second);
            }
        }

        models = new DFDModels();
        List<Model> dfdModels = models.importModels();

        System.out.println("DFD Models Similarity");

        for (Model first : dfdModels) {
            for (Model second : dfdModels) {
                printSimilarModels(first, second);
            }
        }
    }
}
