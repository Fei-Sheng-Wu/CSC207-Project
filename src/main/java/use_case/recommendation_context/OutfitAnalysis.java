package use_case.recommendation_context;

import java.util.ArrayList;
import java.util.List;

/**
 * One analyzer's evidence about an outfit candidate.
 */
public final class OutfitAnalysis {
    private final boolean acceptable;
    private final int eventMatches;
    private final int preferenceMatches;
    private final double fondness;
    private final List<String> reasons;

    public OutfitAnalysis(boolean acceptable,
                          int eventMatches,
                          int preferenceMatches,
                          double fondness,
                          List<String> reasons) {
        this.acceptable = acceptable;
        this.eventMatches = eventMatches;
        this.preferenceMatches = preferenceMatches;
        this.fondness = fondness;
        this.reasons = List.copyOf(reasons);
    }

    /**
     * Returns a neutral, acceptable analysis.
     *
     * @return the neutral analysis
     */
    public static OutfitAnalysis neutral() {
        return new OutfitAnalysis(true, 0, 0, 0.0, List.of());
    }

    /**
     * Combines independent analyzer results.
     *
     * @param other another analyzer result
     * @return the combined result
     */
    public OutfitAnalysis combine(OutfitAnalysis other) {
        final List<String> combinedReasons = new ArrayList<>(reasons);
        combinedReasons.addAll(other.reasons);
        return new OutfitAnalysis(
                acceptable && other.acceptable,
                eventMatches + other.eventMatches,
                preferenceMatches + other.preferenceMatches,
                fondness + other.fondness,
                combinedReasons
        );
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public int getEventMatches() {
        return eventMatches;
    }

    public int getPreferenceMatches() {
        return preferenceMatches;
    }

    public double getFondness() {
        return fondness;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
