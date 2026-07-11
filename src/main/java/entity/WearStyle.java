package entity;

/**
 * Represents the style of a clothing item.
 */
public enum WearStyle {
    PROFESSIONAL("Professional / Business"),
    CASUAL("Casual"),
    SPORTY("Sporty / Athletic"),
    ROMANTIC("Romantic"),
    FORMAL("Formal"),
    INDOOR("Indoor");

    private final String displayName;

    WearStyle(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}