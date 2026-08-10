package use_case.wardrobe_analyzer;

import java.time.Period;
import java.util.List;
import java.util.Map;

import entity.AbstractWear;

public class AgeStatistic implements WardrobeStatistic {
    private static final int MONTHS_IN_YEAR = 12;

    @Override
    public void calculate(List<AbstractWear> items, Map<String, Object> resultsMap) {
        if (items.isEmpty()) {
            resultsMap.put("oldestItemAge", 0);
            resultsMap.put("newestItemAge", 0);
            return;
        }

        int oldest = 0;
        int newest = Integer.MAX_VALUE;

        for (AbstractWear wear : items) {
            final Period age = wear.getAge();
            int ageInMonths = 0;
            if (age != null) {
                ageInMonths = (age.getYears() * MONTHS_IN_YEAR) + age.getMonths();
            }

            if (ageInMonths > oldest) {
                oldest = ageInMonths;
            }
            if (ageInMonths < newest) {
                newest = ageInMonths;
            }
        }

        final int newestAgeResult;
        if (newest == Integer.MAX_VALUE) {
            newestAgeResult = 0;
        } else {
            newestAgeResult = newest;
        }

        resultsMap.put("oldestItemAge", oldest);
        resultsMap.put("newestItemAge", newestAgeResult);
    }
}
