package entity;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

/**
 * Representing a complete set of outfit composed of clothing items of predefined types.
 */
public final class Outfit {
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
     * Checks if the outfit is appropriate for the specified weather data, in both temperature
     * and precipitation.
     *
     * @param weather the specified weather data
     * @return true if the outfit is appropriate; otherwise, false
     */
    public boolean isWeatherAppropriate(Weather weather) {
        return isTemperatureAppropriate(weather) && isPrecipitationAppropriate(weather);
    }

    /**
     * Checks if the outfit provides suitable coverage for the temperature.
     *
     * @param weather the specified weather data
     * @return true if the coverage is suitable; otherwise, false
     */
    public boolean isTemperatureAppropriate(Weather weather) {
        final double temperature = weather.getTemperature();
        final boolean missingOuterTopwear =
            WeatherSuitability.requiresOuterTopwear(temperature) && topwearOuter == null;
        final boolean outerTopwearTooThin =
            WeatherSuitability.requiresThickOuterTopwear(temperature)
                && (topwearOuter == null || !topwearOuter.isThick());
        final boolean bottomwearTooShort =
            WeatherSuitability.requiresLongBottomwear(temperature)
                && (bottomwear == null || !bottomwear.isLong());

        return !(missingOuterTopwear || outerTopwearTooThin || bottomwearTooShort);
    }

    /**
     * Checks if the outfit provides suitable footwear for the precipitation.
     *
     * @param weather the specified weather data
     * @return true if the footwear is suitable; otherwise, false
     */
    public boolean isPrecipitationAppropriate(Weather weather) {
        return !WeatherSuitability.requiresWaterproofFootwear(weather.getPrecipitation())
            || footwear != null && footwear.isWaterproof();
    }

    /**
     * Converts the outfit to a collection of clothing items.
     *
     * @return the clothing of clothing items
     */
    public List<AbstractWear> toList() {
        final List<AbstractWear> items = new ArrayList<>();
        items.add(topwearInner);
        if (topwearOuter != null) {
            items.add(topwearOuter);
        }
        items.add(bottomwear);
        items.add(footwear);
        if (headwear != null) {
            items.add(headwear);
        }
        items.addAll(accessories);

        return items;
    }
}
