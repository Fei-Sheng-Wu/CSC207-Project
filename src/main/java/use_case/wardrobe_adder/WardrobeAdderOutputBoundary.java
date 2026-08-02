package use_case.wardrobe_adder;

import entity.AbstractWear;

/**
 * Defines the output boundary for adding an item to the wardrobe.
 */
public interface WardrobeAdderOutputBoundary {
    /**
     * Outputs a successful response.
     *
     * @param wear the added clothing item
     */
    void prepareSuccessView(AbstractWear wear);

    /**
     * Outputs a failed response.
     *
     * @param message the message of the failed response
     */
    void prepareFailView(String message);
}
