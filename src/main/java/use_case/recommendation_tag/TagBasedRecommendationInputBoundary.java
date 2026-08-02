package use_case.recommendation_tag;

/**
 * Input boundary for the tag-based recommendation use-case
 */
public interface TagBasedRecommendationInputBoundary {
    void recommend(TagBasedRecommendationInputData request);
}
