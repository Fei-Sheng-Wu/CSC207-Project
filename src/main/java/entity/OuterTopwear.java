package entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a OuterTopwear clothing item (e.g., coats, jackets, windbreakers, parkas).
 */
public class OuterTopwear extends Wear {
    private final boolean isThick;

    protected OuterTopwear(UUID uuid, String name, String brand, WearColor color, WearStyle style,
                           ZonedDateTime purchaseDate, boolean isThick) {
        super(uuid, name, brand, color, style, purchaseDate);
        this.isThick = isThick;
    }

    public boolean isThick() {
        return isThick;
    }
}
