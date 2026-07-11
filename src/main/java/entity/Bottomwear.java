package entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a bottomwear clothing item (e.g., pants, jeans, shorts).
 */
public class Bottomwear extends Wear {
    private final boolean isLong;

    protected Bottomwear(UUID uuid, String name, String brand, WearColor color, WearStyle style,
                         ZonedDateTime purchaseDate, boolean isLong) {
        super(uuid, name, brand, color, style, purchaseDate);
        this.isLong = isLong;
    }

    public boolean isLong() {
        return isLong;
    }
}
