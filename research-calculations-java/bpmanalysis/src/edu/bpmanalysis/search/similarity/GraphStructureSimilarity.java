package edu.bpmanalysis.search.similarity;

import edu.bpmanalysis.config.Configuration;
import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import edu.bpmanalysis.search.similarity.api.Similarity;

import java.util.HashMap;
import java.util.Map;

public class GraphStructureSimilarity implements Similarity {

    @Override
    public double compare(Model first, Model second) {
        return Configuration.SIMILARITY_INDEX.measure(getMultisetOfElements(first),
                getMultisetOfElements(second));
    }

    public static Map<String, Integer> getMultisetOfElements(Model model) {
        Map<String, Integer> multiset = new HashMap<>();

        for (Node node : model.getNodesList()) {
            String key = node.getDescription();

            if (multiset.containsKey(key)) {
                int multiplicity = multiset.get(key);
                multiplicity++;
                multiset.put(key, multiplicity);
            } else {
                multiset.put(key, 1);
            }
        }

        return multiset;
    }
}
