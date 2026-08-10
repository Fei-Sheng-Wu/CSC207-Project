package use_case.wardrobe_analyzer;

import java.util.List;
import java.util.Map;

import entity.AbstractWear;

public interface WardrobeStatistic {
    /**
     * Calculates statistics from the provided wardrobe items and
     * stores the results in the given results map.
     *
     * @param items      the list of clothing items to be analyzed
     * @param resultsMap the map where calculated statistics values are stored
     */
    void calculate(List<AbstractWear> items, Map<String, Object> resultsMap);
}
