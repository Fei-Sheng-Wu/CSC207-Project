package use_case.recommendation_context;

/**
 * Input boundary for context-based outfit recommendations.
 */
public interface ContextBasedRecommendationInputBoundary {
    /**
     * Recommends an outfit for the supplied preferences and current context.
     *
     * @param inputData the recommendation input data
     */
    void recommend(ContextBasedRecommendationInputData inputData);
}
