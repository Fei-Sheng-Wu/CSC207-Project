package use_case.wardrobe_filterer;

import interface_adapter.wardrobe_filterer.WardrobeFilteringModel;

/**
 * Defines the input boundary for filtering the items in the wardrobe.
 */
public interface WardrobeFiltererInputBoundary {
    /**
     * Filters the items in the wardrobe.
     *
     * @param filteringModel the model containing the user's filter preferences
     */
    void filterItems(WardrobeFilteringModel filteringModel);
}
