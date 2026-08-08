package use_case.wardrobe_filterer;

/**
 * Defines the output boundary for filtering the items in the wardrobe.
 */
public interface WardrobeFiltererOutputBoundary {
    /**
     * Prepares the success view for the Wardrobe Reporter Use Case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(WardrobeFiltererOutputData outputData);

    /**
     * Prepares the failure view for the Wardrobe Reporter Use Case.
     *
     * @param message the explanation of the failure
     */
    void prepareFailView(String message);
}
