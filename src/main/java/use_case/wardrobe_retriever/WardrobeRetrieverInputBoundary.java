package use_case.wardrobe_retriever;

/**
 * Input Boundary for actions which are related to using Wardrobe Reporter.
 */
public interface WardrobeRetrieverInputBoundary {
    /**
     * Executes the Wardrobe Retriever Use Case.
     * Reports all the clothes and their related info that are in this wardrobe.
     */
    void retrieve();
}
