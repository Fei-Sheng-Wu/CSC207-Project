package entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents an Accessory clothing item (e.g., watches, rings, sunglasses, bags, earrings).
 */
public class Accessory extends Wear {
    protected Accessory(UUID uuid, String name, String brand, WearColor color,
                        WearStyle style, ZonedDateTime purchaseDate) {
        super(uuid, name, brand, color, style, purchaseDate);
    }
}
