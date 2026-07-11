package entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a headwear clothing item (e.g., hats, caps, Beanies).
 */
public class Headwear extends Wear {
    protected Headwear(UUID uuid, String name, String brand, WearColor color, WearStyle style,
                       ZonedDateTime purchaseDate) {
        super(uuid, name, brand, color, style, purchaseDate);
    }
}
