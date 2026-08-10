package use_case.wardrobe_analyzer;

import java.util.List;
import java.util.Map;

import entity.AbstractWear;

public class MeanFondnessStatistic implements WardrobeStatistic {
    @Override
    public void calculate(List<AbstractWear> items, Map<String, Object> resultsMap) {
        if (items.isEmpty()) {
            resultsMap.put("meanFondness", 0.0);
            return;
        }
        double total = 0;
        for (AbstractWear wear : items) {
            total += wear.getFondness();
        }
        resultsMap.put("meanFondness", total / items.size());
    }
}
