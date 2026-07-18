package use_case.wardrobe_updater;

/**
 * Defines the input boundary for updating an item in the wardrobe.
 */
public interface WardrobeUpdaterInputBoundary {
    /**
     * Updates the item in the wardrobe.
     *
     * @param request the input data
     */
    void updateItem(WardrobeUpdaterInputData request);
}
