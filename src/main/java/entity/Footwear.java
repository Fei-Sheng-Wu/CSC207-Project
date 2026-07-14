package entity;

import java.util.UUID;

/**
 * Represents a footwear clothing item (e.g. sneakers, boots, sandals, and heels).
 */
public final class Footwear extends AbstractWear {
    private boolean isWaterproof;

    /**
     * Constructs a new footwear.
     *
     * @param uuid the UUID of the footwear.
     */
    public Footwear(UUID uuid) {
        super(uuid);
    }

    /**
     * Returns whether the footwear is waterproof.
     *
     * @return true if the footwear is waterproof; otherwise, false
     */
    public boolean isWaterproof() {
        return isWaterproof;
    }

    /**
     * Sets whether the footwear is waterproof.
     *
     * @param isWaterproof true if the footwear is waterproof; otherwise, false
     */
    public void setIsWaterproof(boolean isWaterproof) {
        this.isWaterproof = isWaterproof;
    }
}
