package use_case.wardrobe_analyzer;

import java.time.Period;
import java.util.List;
import java.util.Map;

import entity.AbstractWear;

public class DonationCandidateStatistic implements WardrobeStatistic {
    private static final double DONATION_FONDNESS_THRESHOLD = 50.0;
    private static final int MONTHS_IN_YEAR = 12;
    private static final int DONATION_MONTHS_THRESHOLD = 12;

    @Override
    public void calculate(List<AbstractWear> items, Map<String, Object> resultsMap) {
        int donationCandidateCount = 0;
        for (AbstractWear wear : items) {
            final Period age = wear.getAge();
            int ageInMonths = 0;
            if (age != null) {
                ageInMonths = (age.getYears() * MONTHS_IN_YEAR) + age.getMonths();
            }

            if (ageInMonths >= DONATION_MONTHS_THRESHOLD && wear.getFondness() < DONATION_FONDNESS_THRESHOLD) {
                donationCandidateCount++;
            }
        }
        resultsMap.put("donationCandidateCount", donationCandidateCount);
    }
}
