package use_case.recommendation_context;

/**
 * One garment's contribution to the score of any outfit that contains it.
 *
 * <p>Both criteria a garment contributes to are counts, and an outfit's total is the sum of its
 * items' contributions. That is what lets the interactor rank each wardrobe slot on its own
 * instead of building every combination of slots to discover which items are worth considering.
 */
public final class ItemScore {
    private static final ItemScore NONE = new ItemScore(0, 0);

    private final int eventMatches;
    private final int preferenceMatches;

    public ItemScore(int eventMatches, int preferenceMatches) {
        this.eventMatches = eventMatches;
        this.preferenceMatches = preferenceMatches;
    }

    /**
     * Returns a contribution of nothing.
     *
     * @return the empty contribution
     */
    public static ItemScore none() {
        return NONE;
    }

    /**
     * Checks whether the garment contributes to any criterion at all.
     *
     * <p>Contributions add up and never count against an outfit, so a garment that contributes
     * something belongs in any outfit that can hold it, and one that contributes nothing is worth
     * wearing only for reasons a single garment cannot be judged on.
     *
     * @return true if the garment contributes to any criterion; otherwise, false
     */
    public boolean contributes() {
        return eventMatches > 0 || preferenceMatches > 0;
    }

    /**
     * Adds another contribution to this one.
     *
     * @param other the other contribution
     * @return the combined contribution
     */
    public ItemScore plus(ItemScore other) {
        return new ItemScore(
            eventMatches + other.eventMatches,
            preferenceMatches + other.preferenceMatches
        );
    }

    public int getEventMatches() {
        return eventMatches;
    }

    public int getPreferenceMatches() {
        return preferenceMatches;
    }
}
