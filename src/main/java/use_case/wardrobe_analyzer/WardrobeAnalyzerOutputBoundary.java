package use_case.wardrobe_analyzer;

/**
 * Defines the output boundary for analyzing the items in the wardrobe.
 */
public interface WardrobeAnalyzerOutputBoundary {
    /**
     * Prepares the success view for the Wardrobe Reporter Use Case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(WardrobeAnalyzerOutputData outputData);

    /**
     * Prepares the failure view for the Wardrobe Reporter Use Case.
     *
     * @param message the explanation of the failure
     */
    void prepareFailView(String message);
}
