package use_case.wardrobe_adder;

/**
 * Defines the input boundary for adding an item to the wardrobe.
 */
public interface WardrobeAdderInputBoundary {
    /**
     * Adds the item to the wardrobe.
     *
     * @param request the input data
     */
    void addItem(WardrobeAdderInputData request);
}
