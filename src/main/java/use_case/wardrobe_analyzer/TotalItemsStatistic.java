package use_case.wardrobe_analyzer;

import java.util.List;
import java.util.Map;

import entity.AbstractWear;

public class TotalItemsStatistic implements WardrobeStatistic {
    @Override
    public void calculate(List<AbstractWear> items, Map<String, Object> resultsMap) {
        resultsMap.put("totalItems", items.size());
    }
}
