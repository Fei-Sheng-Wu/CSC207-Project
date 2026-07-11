package entity;

/**
 * Represents the primary color of a clothing item.
 */
public enum WearColor {
    BLACK("Black"),
    WHITE("White"),
    GREY("Grey"),
    BROWN("Brown"),
    RED("Red"),
    ORANGE("Orange"),
    YELLOW("Yellow"),
    GREEN("Green"),
    BLUE("Blue"),
    PURPLE("Purple"),
    PINK("Pink"),
    MULTI("Multi-colored/Patterned"); // For complex shirts!

    private final String displayName;

    WearColor(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
