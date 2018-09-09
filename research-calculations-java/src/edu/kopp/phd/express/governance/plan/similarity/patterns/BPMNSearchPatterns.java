package edu.kopp.phd.express.governance.plan.similarity.patterns;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BPMNSearchPatterns implements SearchPatterns {
    public static final int[] MISSING_NODES = {1, 1, 1, 0, 0, 0};
    public static final int[] INVALID_NODES = {0, 0, 0, 1, 1, 1};

    private BPMNSearchPatterns() { }

    public static final SearchPatterns INSTANCE = new BPMNSearchPatterns();

    @Override
    public Map<String, int[]> patterns() {
        Map<String, int[]> patterns = new LinkedHashMap<>();

        patterns.put("Missing nodes", MISSING_NODES);
        patterns.put("Invalid nodes", INVALID_NODES);

        return patterns;
    }
}