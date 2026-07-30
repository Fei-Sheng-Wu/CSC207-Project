package use_case.inspiration_curator;

/**
 * Defines the output boundary for curating an inspiration feed.
 */
public interface InspirationCuratorOutputBoundary {
    /**
     * Outputs a successful response.
     */
    void prepareSuccessView();

    /**
     * Outputs a failed response.
     */
    void prepareFailView();
}
