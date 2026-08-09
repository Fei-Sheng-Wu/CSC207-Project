package interface_adapter.wardrobe_analyzer;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public class WardrobeAnalyzerState {
    private int totalItemsCount;
    private String averageFondnessString = "0.00/100";
    private int donationCandidateCount;
    private String oldestItemAge = "0 months";
    private String newestItemAge = "0 months";
    private Map<String, Integer> categoryDistribution = new HashMap<>();
    private Map<String, Integer> conditionDistribution = new HashMap<>();
    private String error;

    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(int totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }

    public String getAverageFondnessString() {
        return averageFondnessString;
    }

    public void setAverageFondnessString(String averageFondnessString) {
        this.averageFondnessString = averageFondnessString;
    }

    public int getDonationCandidateCount() {
        return donationCandidateCount;
    }

    public void setDonationCandidateCount(int donationCandidateCount) {
        this.donationCandidateCount = donationCandidateCount;
    }

    public String getOldestItemAge() {
        return oldestItemAge;
    }

    public void setOldestItemAge(String oldestItemAge) {
        this.oldestItemAge = oldestItemAge;
    }

    public String getNewestItemAge() {
        return newestItemAge;
    }

    public void setNewestItemAge(String newestItemAge) {
        this.newestItemAge = newestItemAge;
    }

    public Map<String, Integer> getCategoryDistribution() {
        return categoryDistribution;
    }

    public void setCategoryDistribution(Map<String, Integer> categoryDistribution) {
        this.categoryDistribution = categoryDistribution;
    }

    public Map<String, Integer> getConditionDistribution() {
        return conditionDistribution;
    }

    public void setConditionDistribution(Map<String, Integer> conditionDistribution) {
        this.conditionDistribution = conditionDistribution;
    }

    @Nullable
    public String getError() {
        return error;
    }

    public void setError(@Nullable String error) {
        this.error = error;
    }
}
