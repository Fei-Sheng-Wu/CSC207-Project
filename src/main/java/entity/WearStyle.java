package entity;

/**
 * Represents the style of a clothing item.
 */
public enum WearStyle {
    PROFESSIONAL("Professional"),
    CASUAL("Casual"),
    SPORTY("Athletic"),
    ROMANTIC("Romantic"),
    FORMAL("Formal"),
    INDOOR("Indoor");

    private final String displayName;

    /**
     * Constructs a new wear style.
     *
     * @param displayName the display name of the wear style
     */
    WearStyle(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the wear style.
     *
     * @return the display name of the wear style
     */
    public String getDisplayName() {
        return displayName;
    }
}
