package use_case.recommendation;

/**
 * Presents the result of an outfit recommendation use case.
 */
public interface RecommendationOutputBoundary {
    /**
     * Presents a successful recommendation.
     *
     * @param outputData the recommendation output data
     */
    void prepareSuccessView(RecommendationOutputData outputData);

    /**
     * Presents a failed recommendation.
     *
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
