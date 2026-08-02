package use_case.recommendation_context;

import entity.AbstractWear;
import entity.Outfit;

/**
 * Scores outfit attributes that match explicit user preferences.
 */
public final class PreferenceOutfitAnalyzer implements OutfitAnalyzer {
    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        int matches = 0;
        for (AbstractWear item : outfit.toList()) {
            if (context.getPreferredColors().contains(item.getColor())) {
                matches++;
            }
            if (context.getPreferredStyles().contains(item.getStyle())) {
                matches++;
            }
        }

        if (matches == 0) {
            return OutfitAnalysis.neutral();
        }
        final String reason = String.format(
                "%d clothing attributes match your color and style preferences.",
                matches
        );
        return new OutfitAnalysis(true, 0, matches, 0.0, java.util.List.of(reason));
    }
}
