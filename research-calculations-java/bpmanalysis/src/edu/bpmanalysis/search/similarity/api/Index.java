package edu.bpmanalysis.search.similarity.api;

import java.util.Map;

public interface Index {

    double measure(Map<String, Integer> a, Map<String, Integer> b);

    default double count(Map<String, Integer> set, String key) {
        return set.get(key) == null ? 0 : set.get(key);
    }
}
