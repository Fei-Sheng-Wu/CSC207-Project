package entity;

/**
 * Represents the physical condition of a clothing item.
 */
public enum WearCondition {
    NEW("Brand New"),
    FAIR("Fair / Lightly Worn"),
    DAMAGED("Damaged / Needs Repair");

    private final String displayName;

    /**
     * Constructs a new wear condition.
     *
     * @param displayName the display name of the wear condition
     */
    WearCondition(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the wear condition.
     *
     * @return the display name of the wear condition
     */
    public String getDisplayName() {
        return displayName;
    }
}
