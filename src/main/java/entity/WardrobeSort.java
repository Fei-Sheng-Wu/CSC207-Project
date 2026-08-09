package entity;

/**
 * Represents the sorting criteria of wardrobe.
 */
public enum WardrobeSort {
    NONE("None"),
    TYPE("Type"),
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    BRAND_ASC("Brand (A-Z)"),
    BRAND_DESC("Brand (Z-A)");

    private final String displayName;

    /**
     * Constructs a new sorting criteria.
     *
     * @param displayName the display name of the sorting criteria
     */
    WardrobeSort(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the sorting criteria.
     *
     * @return the display name of the sorting criteria
     */
    public String getDisplayName() {
        return displayName;
    }
}
