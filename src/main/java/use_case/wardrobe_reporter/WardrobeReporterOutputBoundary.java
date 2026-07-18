package use_case.wardrobe_reporter;

/**
 * The output boundary for the Wardrobe Reporter Use Case.
 */
public interface WardrobeReporterOutputBoundary {
    /**
     * Prepares the success view for the Wardrobe Reporter Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(WardrobeReporterOutputData outputData);

    /**
     * Prepares the failure view for the Wardrobe Reporter Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
