package use_case.recommendation_tag;

import java.util.List;

import entity.WearColor;
import entity.WearStyle;

/**
 * User's request which stores preferences based on tags, colors, styles, so the algorithm can determine a desirable
 * outfit.
 */
public class TagBasedRecommendationInputData {
    private int seed;
    private List<WearColor> preferredColors;
    private List<WearStyle> preferredStyles;
    private List<String> preferredTags;

    public TagBasedRecommendationInputData(
        int seed,
        List<WearColor> preferredColors,
        List<WearStyle> preferredStyles,
        List<String> preferredTags
    ) {
        this.seed = seed;
        this.preferredColors = preferredColors;
        this.preferredStyles = preferredStyles;
        this.preferredTags = preferredTags;
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

    public List<String> getPreferredTags() {
        return preferredTags;
    }
}
