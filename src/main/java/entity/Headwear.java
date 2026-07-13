package entity;

import java.util.UUID;

/**
 * Represents a headwear clothing item (e.g. hats, caps, and beanies).
 */
public final class Headwear extends AbstractWear {
    /**
     * Constructs a new headwear.
     *
     * @param uuid the UUID of the headwear.
     */
    public Headwear(UUID uuid) {
        super(uuid);
    }
}
