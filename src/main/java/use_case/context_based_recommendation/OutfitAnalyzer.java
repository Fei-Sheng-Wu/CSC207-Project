package use_case.context_based_recommendation;

import entity.Outfit;

/**
 * Evaluates one independent aspect of an outfit recommendation.
 */
public interface OutfitAnalyzer {
    /**
     * Evaluates an outfit in the current recommendation context.
     *
     * @param outfit the outfit candidate
     * @param context the current recommendation context
     * @return the analyzer's evidence
     */
    OutfitAnalysis analyze(Outfit outfit, RecommendationContext context);
}
