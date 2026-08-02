package use_case.recommendation;

import entity.Outfit;

/**
 * Output data produced by an outfit recommendation use case.
 */
public final class RecommendationOutputData {
    private final Outfit outfit;
    private final String reason;

    public RecommendationOutputData(Outfit outfit, String reason) {
        this.outfit = outfit;
        this.reason = reason;
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public String getReason() {
        return reason;
    }
}
