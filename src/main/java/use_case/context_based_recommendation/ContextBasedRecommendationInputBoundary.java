package use_case.context_based_recommendation;

/**
 * Input boundary for context-based outfit recommendations.
 */
public interface ContextBasedRecommendationInputBoundary {
    /**
     * Recommends an outfit for the supplied preferences and current context.
     *
     * @param request the recommendation input data
     */
    void recommend(ContextBasedRecommendationRequest request);
}
