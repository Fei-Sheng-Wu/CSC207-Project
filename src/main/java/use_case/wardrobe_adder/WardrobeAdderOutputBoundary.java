package use_case.wardrobe_adder;

/**
 * Defines the output boundary for adding an item to the wardrobe.
 */
public interface WardrobeAdderOutputBoundary {
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
