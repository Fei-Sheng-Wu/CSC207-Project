package entity;

import java.util.UUID;

/**
 * Represents an accessory clothing item (e.g. watches, rings, sunglasses, bags, and earrings).
 */
public final class Accessory extends AbstractWear {
    /**
     * Constructs a new accessory.
     *
     * @param uuid the UUID of the accessory.
     */
    public Accessory(UUID uuid) {
        super(uuid);
    }
}
