package use_case.wardrobe_analyzer;

import java.util.Map;

/**
 * Represents the output data for analyzing the items in the wardrobe.
 */
public class WardrobeAnalyzerOutputData {
    private final int totalItems;
    private final Map<String, Integer> categoryCounts;
    private final Map<String, Integer> conditionCounts;
    private final double averageFondness;

    private final int donationCandidateCount;
    private final int oldestItemAge;
    private final int newestItemAge;

    public WardrobeAnalyzerOutputData(int totalItems,
                                      Map<String, Integer> categoryCounts,
                                      Map<String, Integer> conditionCounts,
                                      double averageFondness,
                                      int donationCandidateCount,
                                      int oldestItemAge, int newestItemAge) {
        this.totalItems = totalItems;
        this.categoryCounts = categoryCounts;
        this.conditionCounts = conditionCounts;
        this.averageFondness = averageFondness;
        this.donationCandidateCount = donationCandidateCount;
        this.oldestItemAge = oldestItemAge;
        this.newestItemAge = newestItemAge;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Map<String, Integer> getCategoryCounts() {
        return categoryCounts;
    }

    public Map<String, Integer> getConditionCounts() {
        return conditionCounts;
    }

    public double getAverageFondness() {
        return averageFondness;
    }

    public int getDonationCandidateCount() {
        return donationCandidateCount;
    }

    public int getOldestItemAge() {
        return oldestItemAge;
    }

    public int getNewestItemAge() {
        return newestItemAge;
    }
}
