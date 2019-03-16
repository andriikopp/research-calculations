package edu.bpmanalysis.application;

import edu.bpmanalysis.analysis.ProcessModelAnalysisUtil;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.search.similarity.GraphStructureSimilarity;
import edu.bpmanalysis.search.similarity.api.Similarity;
import edu.bpmanalysis.web.model.ProcessModelRepositoryJsonDB;
import edu.bpmanalysis.web.model.api.ProcessModelRepository;
import edu.bpmanalysis.web.model.bean.ProcessModelBean;

import java.util.*;

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

        if (value > 0 && value < 1) {
            System.out.println(first.getName() + " <> " + second.getName() + " = " + value);
            System.out.println(first.getName());

            Map<String, Integer> firstMS = GraphStructureSimilarity.getMultisetOfElements(first);

            for (Map.Entry<String, Integer> entry : firstMS.entrySet()) {
                System.out.println(entry.getKey() + "\t" +
                        GraphStructureSimilarity.count(firstMS, entry.getKey()));
            }

            System.out.println(second.getName());

            Map<String, Integer> secondMS = GraphStructureSimilarity.getMultisetOfElements(second);

            for (Map.Entry<String, Integer> entry : secondMS.entrySet()) {
                System.out.println(entry.getKey() + "\t" +
                        GraphStructureSimilarity.count(secondMS, entry.getKey()));
            }

            System.out.println(first.getName() + " AND " + second.getName());

            Set<String> union = new HashSet<>(firstMS.keySet());
            union.addAll(secondMS.keySet());

            for (String element : union) {
                System.out.println(element + "\t" +
                        GraphStructureSimilarity.count(firstMS, element) + "\t" +
                        GraphStructureSimilarity.count(secondMS, element));
            }
        }
    }

    public static void main(String[] args) {
        ProcessModelRepository repository = new ProcessModelRepositoryJsonDB();

        List<Model> models = new ArrayList<>();

        for (ProcessModelBean bean : repository.getProcessModels()) {
            Model model = ProcessModelAnalysisUtil.transformToModel(bean);
            models.add(model);
        }

        for (Model first : models) {
            for (Model second : models) {
                printSimilarModels(first, second);
            }
        }
    }
}
