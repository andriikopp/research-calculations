package edu.bpmanalysis.search.similarity;

import edu.bpmanalysis.search.similarity.api.Index;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JaccardIndex implements Index {

    @Override
    public double measure(Map<String, Integer> a, Map<String, Integer> b) {
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
}
