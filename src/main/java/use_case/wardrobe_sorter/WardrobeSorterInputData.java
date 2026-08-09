package use_case.wardrobe_sorter;

import entity.WardrobeSort;

public class WardrobeSorterInputData {
    private final WardrobeSort sortBy;

    public WardrobeSorterInputData(WardrobeSort sortBy) {
        this.sortBy = sortBy;
    }

    public WardrobeSort getSortBy() {
        return sortBy;
    }
}
