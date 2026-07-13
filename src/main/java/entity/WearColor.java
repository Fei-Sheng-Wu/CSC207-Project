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
    MULTI("Multicolored / Patterned");

    private final String displayName;

    /**
     * Constructs a new wear color.
     *
     * @param displayName the display name of the wear color
     */
    WearColor(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the wear color.
     *
     * @return the display name of the wear color
     */
    public String getDisplayName() {
        return displayName;
    }
}
