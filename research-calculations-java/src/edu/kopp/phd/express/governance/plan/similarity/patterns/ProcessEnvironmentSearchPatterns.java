package edu.kopp.phd.express.governance.plan.similarity.patterns;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ProcessEnvironmentSearchPatterns implements SearchPatterns {
    public static final int[] RESOURCES_ISSUES = {1, 1, 0, 0, 0, 0, 0, 0};
    public static final int[] AUTOMATION_ISSUES = {0, 0, 0, 0, 0, 0, 1, 1};
    public static final int[] MISSING_IO = {0, 0, 1, 1, 0, 0, 0, 0};
    public static final int[] REDUNDANT_IO = {0, 0, 0, 0, 1, 1, 0, 0};

    private ProcessEnvironmentSearchPatterns() { }

    public static final SearchPatterns INSTANCE = new ProcessEnvironmentSearchPatterns();

    @Override
    public Map<String, int[]> patterns() {
        Map<String, int[]> patterns = new LinkedHashMap<>();

        patterns.put("Resources issues", RESOURCES_ISSUES);
        patterns.put("Automation issues", AUTOMATION_ISSUES);
        patterns.put("Missing inputs/outputs", MISSING_IO);
        patterns.put("Redundant inputs/outputs", REDUNDANT_IO);

        return patterns;
    }
}
