package use_case.wardrobe_sorter;

public interface WardrobeSorterOutputBoundary {
    /**
     * Prepares the success view for the Wardrobe Reporter Use Case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(WardrobeSorterOutputData outputData);
}
