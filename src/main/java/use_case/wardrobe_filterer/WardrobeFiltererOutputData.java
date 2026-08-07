package use_case.wardrobe_filterer;

import entity.AbstractWear;

import java.util.List;

/**
 * Represents the output data for filtering the items in the wardrobe.
 */
public class WardrobeFiltererOutputData {
    private final List<AbstractWear> filteredItems;

    /**
     * Constructs a new wardrobe reporter output data.
     *
     * @param filteredItems a list of all clothing items in the wardrobe that has been filtered.
     */
    public WardrobeFiltererOutputData(
        List<AbstractWear> filteredItems
    ) {
        this.filteredItems = filteredItems;
    }

    /**
     * Returns the list of all clothing items.
     *
     * @return a list of all items in the wardrobe
     */
    public List<AbstractWear> getFilteredItems() {
        return filteredItems;
    }
}
