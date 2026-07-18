package use_case.recommendation;

/**
 * Presents the result of an outfit recommendation use case.
 */
public interface RecommendationOutputBoundary {
    /**
     * Presents a successful recommendation.
     *
     * @param response the recommendation output data
     */
    void prepareSuccessView(RecommendationResponse response);

    /**
     * Presents a failed recommendation.
     *
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
