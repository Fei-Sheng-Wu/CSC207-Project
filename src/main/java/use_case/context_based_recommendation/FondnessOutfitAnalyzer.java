package use_case.context_based_recommendation;

import java.util.List;

import entity.AbstractWear;
import entity.Outfit;

/**
 * Uses the user's fondness values as a final ranking criterion.
 */
public final class FondnessOutfitAnalyzer implements OutfitAnalyzer {
    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        final List<AbstractWear> items = OutfitItems.collect(outfit);
        final double averageFondness = items.stream()
                .mapToDouble(AbstractWear::getFondness)
                .average()
                .orElse(0.0);
        final String reason = String.format(
                "The selected items have an average fondness of %.2f.",
                averageFondness
        );
        return new OutfitAnalysis(true, 0, 0, averageFondness, List.of(reason));
    }
}
