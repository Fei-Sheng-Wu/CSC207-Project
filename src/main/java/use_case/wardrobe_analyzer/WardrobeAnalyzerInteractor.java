package use_case.wardrobe_analyzer;

import entity.AbstractWear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WardrobeAnalyzerInteractor implements WardrobeAnalyzerInputBoundary {
    private static final double DONATION_FONDNESS_THRESHOLD = 50.0;
    private static final int MONTHS_IN_YEAR = 12;
    private static final int DONATION_MONTHS_THRESHOLD = 12;

    private final WardrobeDataAccessInterface repository;
    private final WardrobeAnalyzerOutputBoundary outputBoundary;

    public WardrobeAnalyzerInteractor(WardrobeDataAccessInterface repository,
                                      WardrobeAnalyzerOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void analyze() {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final List<AbstractWear> items = wardrobe.getItems();

        if (items.isEmpty()) {
            outputBoundary.prepareFailView("Your wardrobe is empty. Add some clothes to see statistics!");
            return;
        }

        final Map<String, Integer> categoryCounts = new HashMap<>();
        final Map<String, Integer> conditionCounts = new HashMap<>();
        double totalFondness = 0;

        int donationCandidateCount = 0;
        int oldestItemAge = 0;
        int newestItemAge = Integer.MAX_VALUE;

        for (AbstractWear wear : items) {
            final String category = wear.getClass().getSimpleName();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);

            if (wear.getCondition() != null) {
                final String condition = wear.getCondition().name();
                conditionCounts.put(condition, conditionCounts.getOrDefault(condition, 0) + 1);
            }

            totalFondness += wear.getFondness();

            int ageInMonths = 0;
            final Period age = wear.getAge();
            if (age != null) {
                ageInMonths = (age.getYears() * MONTHS_IN_YEAR) + age.getMonths();
            }

            if (ageInMonths > oldestItemAge) {
                oldestItemAge = ageInMonths;
            }
            if (ageInMonths < newestItemAge) {
                newestItemAge = ageInMonths;
            }

            // Donation Candidate Logic: Older than a year and low fondness
            if (ageInMonths >= DONATION_MONTHS_THRESHOLD && wear.getFondness() < DONATION_FONDNESS_THRESHOLD) {
                donationCandidateCount++;
            }
        }

        if (newestItemAge == Integer.MAX_VALUE) {
            newestItemAge = 0;
        }

        final double meanFondness = totalFondness / items.size();

        final WardrobeAnalyzerOutputData outputData = new WardrobeAnalyzerOutputData(
            items.size(),
            categoryCounts,
            conditionCounts,
            meanFondness,
            donationCandidateCount,
            oldestItemAge,
            newestItemAge
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}
