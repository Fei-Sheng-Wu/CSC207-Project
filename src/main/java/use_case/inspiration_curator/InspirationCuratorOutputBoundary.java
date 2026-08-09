package use_case.inspiration_curator;

/**
 * Defines the output boundary for curating an inspiration feed.
 */
public interface InspirationCuratorOutputBoundary {
    /**
     * Outputs a successful response.
     *
     * @param output the output data of the response
     */
    void prepareSuccessView(InspirationCuratorOutputData output);

    /**
     * Outputs a failed response.
     *
     * @param error the error of the response
     */
    void prepareFailView(String error);
}
