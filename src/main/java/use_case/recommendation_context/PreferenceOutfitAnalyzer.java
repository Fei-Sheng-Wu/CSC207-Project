package use_case.recommendation_context;

import java.util.List;

import entity.AbstractWear;
import entity.Outfit;

/**
 * Scores outfit attributes that match explicit user preferences.
 *
 * <p>The rule itself lives in {@link PreferenceItemAnalyzer}; this class only sums that rule over
 * the outfit's garments and explains the total. Narrowing and final scoring therefore ask the
 * same object what a preference match is, and cannot drift apart.
 */
public final class PreferenceOutfitAnalyzer implements OutfitAnalyzer {
    private final ItemAnalyzer itemAnalyzer = new PreferenceItemAnalyzer();

    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        int matches = 0;
        for (AbstractWear item : outfit.toList()) {
            matches += itemAnalyzer.analyze(item, context).getPreferenceMatches();
        }

        if (matches == 0) {
            return OutfitAnalysis.neutral();
        }
        final String reason = String.format(
                "%d clothing attributes match your color and style preferences.",
                matches
        );
        return new OutfitAnalysis(true, 0, matches, 0.0, List.of(reason));
    }
}
