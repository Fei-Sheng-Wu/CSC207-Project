package use_case.wardrobe_updater;

/**
 * Defines the output boundary for updating an item to the wardrobe.
 */
public interface WardrobeUpdaterOutputBoundary {
    /**
     * Outputs a successful response.
     */
    void prepareSuccessView();

    /**
     * Outputs a failed response.
     *
     * @param message the message of the failed response
     */
    void prepareFailView(String message);
}
