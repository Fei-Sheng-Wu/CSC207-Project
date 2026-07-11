package entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a innerTopwear clothing item (e.g., crew-neck, v-neck, ripped tank).
 */
public class InnerTopwear extends Wear {
    protected InnerTopwear(UUID uuid, String name, String brand, WearColor color, WearStyle style,
                           ZonedDateTime purchaseDate) {
        super(uuid, name, brand, color, style, purchaseDate);
    }
}
