package use_case.wardrobe_analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.AbstractWear;
import entity.WearFactory;

public final class CategoryCountStatistic implements WardrobeStatistic {
    @Override
    public void calculate(List<AbstractWear> items, Map<String, Object> resultsMap) {
        final Map<String, Integer> categoryCounts = new HashMap<>();
        for (AbstractWear wear : items) {
            final String category = WearFactory.getDisplayName(wear.getClass());
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
        }
        resultsMap.put("categoryCounts", categoryCounts);
    }
}
