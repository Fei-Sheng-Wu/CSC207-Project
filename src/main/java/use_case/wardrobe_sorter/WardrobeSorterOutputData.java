package use_case.wardrobe_sorter;

import entity.AbstractWear;

import java.util.List;

public class WardrobeSorterOutputData {
    private final List<AbstractWear> sortedItems;

    public WardrobeSorterOutputData(List<AbstractWear> sortedItems) {
        this.sortedItems = sortedItems;
    }

    public List<AbstractWear> getSortedItems() {
        return sortedItems;
    }
}
