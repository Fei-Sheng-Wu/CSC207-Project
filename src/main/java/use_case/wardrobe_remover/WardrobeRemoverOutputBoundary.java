package use_case.wardrobe_remover;

/**
 * Defines the output boundary for adding an item to the wardrobe.
 */
public interface WardrobeRemoverOutputBoundary {
    /**
     * Outputs a successful response.
     */
    void prepareSuccessView();

    /**
     * Outputs a failed response.
     */
    void prepareFailView();
}
