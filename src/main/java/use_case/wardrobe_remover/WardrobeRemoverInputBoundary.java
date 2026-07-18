package use_case.wardrobe_remover;

/**
 * Defines the input boundary for removing an item from the wardrobe.
 */
public interface WardrobeRemoverInputBoundary {
    /**
     * Removes the item from the wardrobe.
     *
     * @param request the input data
     */
    void removeItem(WardrobeRemoverInputData request);
}
