package entity;

/**
 * Represents the physical condition of a clothing item.
 */
public enum WearCondition {
    NEW("Brand New"),
    FAIR("Fair / Lightly Worn"),
    DAMAGED("Damaged / Needs Repair");

    private final String displayName;

    WearCondition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}