package entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a piece of clothing item (e.g. topwears, headwears, and accessories).
 */
public abstract class AbstractWear {
    private final UUID uuid;

    private String name = "";
    private String brand = "";
    private WearColor color;
    private WearStyle style;
    private WearCondition condition;
    private LocalDate purchaseDate;
    private double fondness = 1.0;
    private List<String> tags = new ArrayList<>();

    /**
     * Constructs a new abstract wear.
     *
     * @param uuid the UUID of the clothing item
     */
    protected AbstractWear(UUID uuid) {
        // We only set the UUID in the constructor as it is immutable, while the other fields can be independently
        // set and updated via respective setters.
        this.uuid = uuid;
    }

    /**
     * Returns the UUID of the clothing item.
     *
     * @return the UUID of the clothing item
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the name of the clothing item.
     *
     * @return the name of the clothing item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the clothing item.
     *
     * @param name the name of the clothing item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the brand of the clothing item.
     *
     * @return the brand of the clothing item
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the clothing item.
     *
     * @param brand the brand of the clothing item
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the color of the clothing item.
     *
     * @return the color of the clothing item
     */
    @Nullable
    public WearColor getColor() {
        return color;
    }

    /**
     * Sets the color of the clothing item.
     *
     * @param color the color of the clothing item
     */
    public void setColor(@Nullable WearColor color) {
        this.color = color;
    }

    /**
     * Returns the style of the clothing item.
     *
     * @return the style of the clothing item
     */
    @Nullable
    public WearStyle getStyle() {
        return style;
    }

    /**
     * Sets the style of the clothing item.
     *
     * @param style the style of the clothing item
     */
    public void setStyle(@Nullable WearStyle style) {
        this.style = style;
    }

    /**
     * Returns the condition of the clothing item.
     *
     * @return the condition of the clothing item
     */
    @Nullable
    public WearCondition getCondition() {
        return condition;
    }

    /**
     * Sets the condition of the clothing item.
     *
     * @param condition the condition of the clothing item
     */
    public void setCondition(@Nullable WearCondition condition) {
        this.condition = condition;
    }

    /**
     * Returns the purchase date of the clothing item.
     *
     * @return the purchase date of the clothing item
     */
    @Nullable
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the purchase date of the clothing item.
     *
     * @param purchaseDate the purchase date of the clothing item
     */
    public void setPurchaseDate(@Nullable LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Returns the fondness of the clothing item.
     *
     * @return the fondness of the clothing item
     */
    public double getFondness() {
        return fondness;
    }

    /**
     * Sets the fondness of the clothing item.
     *
     * @param fondness the fondness of the clothing item
     */
    public void setFondness(double fondness) {
        this.fondness = fondness;
    }

    /**
     * Returns the tags of the clothing item.
     *
     * @return the tags of the clothing item
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the tags of the clothing item.
     *
     * @param tags the tags of the clothing item
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Returns the age of the clothing item from its purchase date to today.
     *
     * @return the age of the clothing item
     */
    public Period getAge() {
        if (purchaseDate == null) {
            return null;
        }

        return Period.between(purchaseDate, LocalDate.now());
    }

    /**
     * Returns the formatted display string of the clothing item.
     *
     * @return the formatted display string of the clothing item
     */
    public String getDisplayString() {
        final String first;
        if (name.isBlank()) {
            first = "[unnamed]";
        } else {
            first = name;
        }

        if (brand.isBlank()) {
            return first;
        } else {
            return String.format("%s (%s)", first, brand);
        }
    }
}
