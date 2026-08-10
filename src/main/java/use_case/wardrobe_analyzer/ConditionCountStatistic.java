package use_case.wardrobe_analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.AbstractWear;

public final class ConditionCountStatistic implements WardrobeStatistic {

    @Override
    public void calculate(List<AbstractWear> items, Map<String, Object> resultsMap) {
        final Map<String, Integer> conditionCounts = new HashMap<>();
        for (AbstractWear wear : items) {
            if (wear.getCondition() != null) {
                final String condition = wear.getCondition().getDisplayName();
                conditionCounts.put(condition, conditionCounts.getOrDefault(condition, 0) + 1);
            }
        }
        resultsMap.put("conditionCounts", conditionCounts);
    }
}
