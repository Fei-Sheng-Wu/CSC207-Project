package use_case.wardrobe_filterer;

import java.util.List;

public final class FilterFactory {

    /**
     * Creates and returns a list of all wardrobe filter ways.
     *
     * @return a list containing implementations of WardrobeFilter
     */
    public static List<WardrobeFilter> createFilters() {
        return List.of(
            new NameFilter(),
            new CategoryFilter(),
            new MonthFilter(),
            new ConditionFilter(),
            new TagFilter()
        );
    }
}
