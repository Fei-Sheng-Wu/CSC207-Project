package entity;

import java.util.UUID;

/**
 * Represents an outer topwear clothing item (e.g. coats, hoodies, jackets, windbreakers, and parkas).
 */
public final class OuterTopwear extends AbstractWear {
    private boolean isThick;

    /**
     * Constructs a new outer topwear.
     *
     * @param uuid the UUID of the outer topwear.
     */
    public OuterTopwear(UUID uuid) {
        super(uuid);
    }

    /**
     * Returns whether the bottomwear is thick.
     *
     * @return true if the bottomwear is thick; otherwise, false
     */
    public boolean isThick() {
        return isThick;
    }

    /**
     * Sets whether the bottomwear is thick.
     *
     * @param isThick true if the bottomwear is thick; otherwise, false
     */
    public void setIsThick(boolean isThick) {
        this.isThick = isThick;
    }
}
