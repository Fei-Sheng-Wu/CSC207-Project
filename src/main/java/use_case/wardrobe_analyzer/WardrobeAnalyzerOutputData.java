package use_case.wardrobe_analyzer;

import java.util.Map;

/**
 * Represents the output data for analyzing the items in the wardrobe.
 */
public class WardrobeAnalyzerOutputData {
    private final Map<String, Object> statistics;

    public WardrobeAnalyzerOutputData(Map<String, Object> statistics) {
        this.statistics = statistics;
    }

    public Map<String, Object> getStatistics() {
        return statistics;
    }
}
