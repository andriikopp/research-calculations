package edu.kopp.phd.express.governance.plan.similarity.patterns;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DFDSearchPatterns implements SearchPatterns {
    public static final int[] INVALID_NODES = {1, 1, 1, 0};
    public static final int[] INVALID_DATA_FLOW = {0, 0, 0, 1};

    private DFDSearchPatterns() { }

    public static final SearchPatterns INSTANCE = new DFDSearchPatterns();

    @Override
    public Map<String, int[]> patterns() {
        Map<String, int[]> patterns = new LinkedHashMap<>();

        patterns.put("Invalid nodes", INVALID_NODES);
        patterns.put("Invalid data flow", INVALID_DATA_FLOW);

        return patterns;
    }
}
