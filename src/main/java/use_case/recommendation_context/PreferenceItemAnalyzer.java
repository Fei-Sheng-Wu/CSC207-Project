package use_case.recommendation_context;

import entity.AbstractWear;

/**
 * Scores a garment's attributes against the user's explicit preferences.
 *
 * <p>A garment may leave its colour or style unset, so neither is looked up without first
 * checking that it exists: the preference lists are immutable, and an immutable list rejects a
 * null lookup rather than answering it.
 */
public final class PreferenceItemAnalyzer implements ItemAnalyzer {
    @Override
    public ItemScore analyze(AbstractWear item, RecommendationContext context) {
        int matches = 0;
        if (item.getColor() != null && context.getPreferredColors().contains(item.getColor())) {
            matches++;
        }
        if (item.getStyle() != null && context.getPreferredStyles().contains(item.getStyle())) {
            matches++;
        }

        return new ItemScore(0, matches);
    }
}
