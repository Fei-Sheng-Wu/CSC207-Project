package use_case.wardrobe_retriever;

/**
 * The output boundary for the Wardrobe Retriever Use Case.
 */
public interface WardrobeRetrieverOutputBoundary {
    /**
     * Prepares the success view for the Wardrobe Retriever Use Case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(WardrobeRetrieverOutputData outputData);

    /**
     * Prepares the failure view for the Wardrobe Retriever Use Case.
     *
     * @param message the explanation of the failure
     */
    void prepareFailView(String message);
}
