package use_case.wardrobe_filterer;

/**
 * Defines the output boundary for filtering the items in the wardrobe.
 */
public interface WardrobeFiltererOutputBoundary {
    /**
     * Prepares the success view for the Wardrobe Filterer Use Case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(WardrobeFiltererOutputData outputData);

    /**
     * Prepares the success view for the Wardrobe Filterer Use Case.
     *
     * @param error the error string
     */
    void prepareFailView(String error);
}
