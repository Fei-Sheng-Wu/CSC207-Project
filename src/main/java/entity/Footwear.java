package entity;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a footwear clothing item (e.g., sneakers, boots, sandals, heels).
 */
public class Footwear extends Wear {
    private final boolean isWaterProof;

    protected Footwear(UUID uuid, String name, String brand, WearColor color, WearStyle style,
                       ZonedDateTime purchaseDate, boolean isWaterProof) {
        super(uuid, name, brand, color, style, purchaseDate);
        this.isWaterProof = isWaterProof;
    }

    public boolean isWaterProof() {
        return isWaterProof;
    }
}
