package entity;

import java.util.UUID;

/**
 * Represents a bottomwear clothing item (e.g. pants, jeans, skirts, and shorts).
 */
public final class Bottomwear extends AbstractWear {
    private boolean isLong;

    /**
     * Constructs a new bottomwear.
     *
     * @param uuid the UUID of the bottomwear.
     */
    public Bottomwear(UUID uuid) {
        super(uuid);
    }

    /**
     * Returns whether the bottomwear is long.
     *
     * @return true if the bottomwear is long; otherwise, false
     */
    public boolean isLong() {
        return isLong;
    }

    /**
     * Sets whether the bottomwear is long.
     *
     * @param isLong true if the bottomwear is long; otherwise, false
     */
    public void setIsLong(boolean isLong) {
        this.isLong = isLong;
    }
}
