package entity;

import java.util.List;

import org.jetbrains.annotations.Nullable;

/**
 * Representing a complete set of outfit composed of clothing items of predefined types.
 */
public final class Outfit {
    private static final double TEMP_TOPWEAR_OUTER_PRESENT = 10.0;
    private static final double TEMP_TOPWEAR_OUTER_THICK = 0.0;
    private static final double TEMP_BOTTOMWEAR_LONG = 5.0;

    private final InnerTopwear topwearInner;
    private final OuterTopwear topwearOuter;
    private final Bottomwear bottomwear;
    private final Footwear footwear;
    private final Headwear headwear;
    private final List<Accessory> accessories;

    /**
     * Constructs a new outfit.
     *
     * @param topwearInner the inner topwear of the outfit
     * @param topwearOuter the outer topwear of the outfit
     * @param bottomwear   the bottomwear of the outfit
     * @param footwear     the footwear of the outfit
     * @param headwear     the headwear of the outfit
     * @param accessories  the collection of accessories of the outfit
     */
    public Outfit(InnerTopwear topwearInner,
                  @Nullable OuterTopwear topwearOuter,
                  Bottomwear bottomwear,
                  Footwear footwear,
                  @Nullable Headwear headwear,
                  List<Accessory> accessories) {
        this.topwearInner = topwearInner;
        this.topwearOuter = topwearOuter;
        this.bottomwear = bottomwear;
        this.footwear = footwear;
        this.headwear = headwear;
        this.accessories = accessories;
    }

    /**
     * Returns the inner topwear of the outfit.
     *
     * @return the inner topwear of the outfit
     */
    public InnerTopwear getTopwearInner() {
        return topwearInner;
    }

    /**
     * Returns the outer topwear of the outfit.
     *
     * @return the outer topwear of the outfit
     */
    @Nullable
    public OuterTopwear getTopwearOuter() {
        return topwearOuter;
    }

    /**
     * Returns the bottomwear of the outfit.
     *
     * @return the bottomwear of the outfit
     */
    public Bottomwear getBottomwear() {
        return bottomwear;
    }

    /**
     * Returns the footwear of the outfit.
     *
     * @return the footwear of the outfit
     */
    public Footwear getFootwear() {
        return footwear;
    }

    /**
     * Returns the headwear of the outfit.
     *
     * @return the headwear of the outfit
     */
    @Nullable
    public Headwear getHeadwear() {
        return headwear;
    }

    /**
     * Returns the collection of accessories of the outfit.
     *
     * @return the collection of accessories of the outfit
     */
    public List<Accessory> getAccessories() {
        return accessories;
    }

    /**
     * Checks if the outfit is appropriate for the specified weather data.
     *
     * @param weather the specified weather data
     * @return true if the outfit is appropriate; otherwise, false
     */
    public boolean isWeatherAppropriate(Weather weather) {
        double temperature = weather.getTemperature();

        // @TODO: Check for appropriateness in regards to precipitation.

        return !(temperature < TEMP_TOPWEAR_OUTER_PRESENT && topwearOuter == null
            || temperature < TEMP_TOPWEAR_OUTER_THICK && (topwearOuter == null || !topwearOuter.isThick())
            || temperature < TEMP_BOTTOMWEAR_LONG && (bottomwear == null || !bottomwear.isLong()));
    }
}
