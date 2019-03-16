package edu.bpmanalysis.search.similarity;

import edu.bpmanalysis.description.tools.Model;
import edu.bpmanalysis.description.tools.Node;
import edu.bpmanalysis.search.similarity.api.Similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphStructureSimilarity implements Similarity {

    @Override
    public double compare(Model first, Model second) {
        return jaccardIndex(getMultisetOfElements(first),
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

    public static double jaccardIndex(Map<String, Integer> a, Map<String, Integer> b) {
        Set<String> union = new HashSet<>(a.keySet());
        union.addAll(b.keySet());

        double min = 0;
        double max = 0;

        for (String key : union) {
            min += Math.min(count(a, key), count(b, key));
            max += Math.max(count(a, key), count(b, key));
        }

        return min / max;
    }

    public static double count(Map<String, Integer> set, String key) {
        return set.get(key) == null ? 0 : set.get(key);
    }
}
