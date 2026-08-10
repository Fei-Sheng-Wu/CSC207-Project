package use_case.wardrobe_analyzer;

import java.util.List;

public class AnalyzerFactory {

    /**
     * Creates and returns a list of all wardrobe statistic calculations.
     *
     * @return a list containing implementations of WardrobeStatistic
     */
    public static List<WardrobeStatistic> createStatistics() {
        return List.of(
            new TotalItemsStatistic(),
            new CategoryCountStatistic(),
            new ConditionCountStatistic(),
            new MeanFondnessStatistic(),
            new DonationCandidateStatistic(),
            new AgeStatistic()
        );
    }
}
