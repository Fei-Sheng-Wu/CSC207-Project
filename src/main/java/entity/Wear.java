package entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The abstract base entity representing any piece of clothing or accessory.
 * It defines the core attributes shared across all wardrobe items, such as
 * physical condition, style, color, and purchase history.
 */
public abstract class Wear {
    private final UUID uuid;
    private final String name;
    private final String brand;
    private final WearColor color;
    private final WearStyle style;
    private final ZonedDateTime purchaseDate;
    private WearCondition condition;
    private double fondness;
    private List<String> tags;

    // should we make it public or protected?! hmmm...
    protected Wear(UUID uuid, String name, String brand, WearColor color, WearStyle style,
                   ZonedDateTime purchaseDate) {
        this.uuid = uuid;
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.style = style;
        this.purchaseDate = purchaseDate;
        this.condition = WearCondition.NEW;
        this.fondness = 1.0;
        // this.tag = ; not implemented yet!
    }

    /**
     * Calculate the age of this clothing item from its purchase date to today.
     * @return A Period object representing the years, months, and days owned.
     */
    public Period getAge() {
        LocalDate boughtDate = this.purchaseDate.toLocalDate();
        LocalDate today = LocalDate.now();
        return Period.between(boughtDate, today);
    }
}
