package use_case.recommendation_tag;

/**
 * Input boundary for the tag-based recommendation use-case.
 */
public interface TagBasedRecommendationInputBoundary {
    /**
     * Recommends an outfit for the supplied colour, style, and tag preferences.
     *
     * @param request the user's preferences
     */
    void recommend(TagBasedRecommendationInputData request);
}
