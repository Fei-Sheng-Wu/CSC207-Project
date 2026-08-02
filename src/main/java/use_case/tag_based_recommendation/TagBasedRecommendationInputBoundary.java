package use_case.tag_based_recommendation;

/**
 * Input boundary for the tag-based recommendation use-case
 */
public interface TagBasedRecommendationInputBoundary {
    void recommend(TagBasedRecommendationRequest request);
}
