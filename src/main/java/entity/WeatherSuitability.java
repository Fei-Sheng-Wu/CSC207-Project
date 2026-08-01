package entity;

/**
 * The rules that decide what an outfit must provide for a given set of weather conditions.
 *
 * <p>These thresholds previously appeared as literals in three places at once: the constants on
 * {@link Outfit}, the candidate filters in the recommendation interactor, and the explanation
 * messages in the weather analyzer. Changing a rule therefore meant remembering all three, and
 * the three could silently disagree. They are collected here so that a rule has exactly one
 * definition, and every layer asks the same question of the same object.
 */
public final class WeatherSuitability {
    /**
     * At or above this temperature in Celsius, an outer topwear is optional.
     */
    public static final double TEMP_OUTER_TOPWEAR_REQUIRED = 10.0;

    /**
     * Below this temperature in Celsius, an outer topwear must be thick.
     */
    public static final double TEMP_THICK_OUTER_TOPWEAR_REQUIRED = 0.0;

    /**
     * Below this temperature in Celsius, a bottomwear must be long.
     */
    public static final double TEMP_LONG_BOTTOMWEAR_REQUIRED = 5.0;

    /**
     * Above this precipitation in millimetres, a footwear must be waterproof.
     */
    public static final double PRECIPITATION_WATERPROOF_REQUIRED = 0.0;

    private WeatherSuitability() {
    }

    /**
     * Checks whether the temperature calls for an outer topwear at all.
     *
     * @param temperature the temperature in Celsius
     * @return true if an outer topwear is required; otherwise, false
     */
    public static boolean requiresOuterTopwear(double temperature) {
        return temperature < TEMP_OUTER_TOPWEAR_REQUIRED;
    }

    /**
     * Checks whether the temperature calls for the outer topwear to be thick.
     *
     * @param temperature the temperature in Celsius
     * @return true if a thick outer topwear is required; otherwise, false
     */
    public static boolean requiresThickOuterTopwear(double temperature) {
        return temperature < TEMP_THICK_OUTER_TOPWEAR_REQUIRED;
    }

    /**
     * Checks whether the temperature calls for a long bottomwear.
     *
     * @param temperature the temperature in Celsius
     * @return true if a long bottomwear is required; otherwise, false
     */
    public static boolean requiresLongBottomwear(double temperature) {
        return temperature < TEMP_LONG_BOTTOMWEAR_REQUIRED;
    }

    /**
     * Checks whether the precipitation calls for waterproof footwear.
     *
     * @param precipitation the precipitation in millimetres
     * @return true if waterproof footwear is required; otherwise, false
     */
    public static boolean requiresWaterproofFootwear(double precipitation) {
        return precipitation > PRECIPITATION_WATERPROOF_REQUIRED;
    }
}
