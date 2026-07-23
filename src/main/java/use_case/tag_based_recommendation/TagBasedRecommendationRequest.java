package use_case.tag_based_recommendation;

import java.util.List;

import entity.WearColor;
import entity.WearStyle;

/**
 * user's request which stores preferences based on tags, colors, styles, so the algorithm can determine a desirable
 * outfit
 */
public class TagBasedRecommendationRequest {
    private int seed;
    private List<WearColor> preferredColors;
    private List<WearStyle> preferredStyles;
    private List<String> preferredTags;

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public List<WearColor> getPreferredColors() {
        return preferredColors;
    }

    public void setPreferredColors(List<WearColor> preferredColors) {
        this.preferredColors = preferredColors;
    }

    public List<WearStyle> getPreferredStyles() {
        return preferredStyles;
    }

    public void setPreferredStyles(List<WearStyle> preferredStyles) {
        this.preferredStyles = preferredStyles;
    }

    public List<String> getPreferredTags() {
        return preferredTags;
    }

    public void setPreferredTags(List<String> preferredTags) {
        this.preferredTags = preferredTags;
    }
}
