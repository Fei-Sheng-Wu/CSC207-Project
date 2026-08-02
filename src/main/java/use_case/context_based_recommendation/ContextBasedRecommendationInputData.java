package use_case.context_based_recommendation;

import java.util.List;

import entity.WearColor;
import entity.WearStyle;

/**
 * Input data for a context-based outfit recommendation.
 */
public final class ContextBasedRecommendationInputData {
    private final int seed;
    private final List<WearColor> preferredColors;
    private final List<WearStyle> preferredStyles;

    public ContextBasedRecommendationInputData(int seed,
                                               List<WearColor> preferredColors,
                                               List<WearStyle> preferredStyles) {
        this.seed = seed;
        this.preferredColors = List.copyOf(preferredColors);
        this.preferredStyles = List.copyOf(preferredStyles);
    }

    public int getSeed() {
        return seed;
    }

    public List<WearColor> getPreferredColors() {
        return preferredColors;
    }

    public List<WearStyle> getPreferredStyles() {
        return preferredStyles;
    }
}
