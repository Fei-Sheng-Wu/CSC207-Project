package entity;

import java.util.UUID;

/**
 * Represents an inner topwear clothing item (e.g. shirts, crew-necks, sports bras, and tank tops).
 */
public final class InnerTopwear extends AbstractWear {
    /**
     * Constructs a new inner topwear.
     *
     * @param uuid the UUID of the inner topwear.
     */
    public InnerTopwear(UUID uuid) {
        super(uuid);
    }
}
