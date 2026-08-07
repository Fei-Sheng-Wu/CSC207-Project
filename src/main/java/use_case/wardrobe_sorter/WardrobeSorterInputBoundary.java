package use_case.wardrobe_sorter;

public interface WardrobeSorterInputBoundary {
    /**
     * Executes the Wardrobe Sorter Use Case.
     * Sort all the clothing items based on an input String.
     *
     * @param inputData the data containing the sorting criteria
     */
    void sortWardrobe(WardrobeSorterInputData inputData);
}
