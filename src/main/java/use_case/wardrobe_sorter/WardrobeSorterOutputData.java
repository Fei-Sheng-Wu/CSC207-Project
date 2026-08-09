package use_case.wardrobe_sorter;

import java.util.List;

import entity.AbstractWear;

public class WardrobeSorterOutputData {
    private final List<AbstractWear> sortedItems;

    public WardrobeSorterOutputData(List<AbstractWear> sortedItems) {
        this.sortedItems = sortedItems;
    }

    public List<AbstractWear> getSortedItems() {
        return sortedItems;
    }
}
