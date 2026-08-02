package entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Tests for the shared weather suitability rules and the outfit checks built on them.
 *
 * <p>Every threshold is tested at its boundary as well as on either side, because the rules are
 * written with strict comparisons and an off-by-one there is silent.
 */
class WeatherSuitabilityTest {
    @Test
    void outerTopwearIsRequiredBelowTenDegreesOnly() {
        assertTrue(WeatherSuitability.requiresOuterTopwear(9.9));
        assertFalse(WeatherSuitability.requiresOuterTopwear(10.0));
        assertFalse(WeatherSuitability.requiresOuterTopwear(25.0));
    }

    @Test
    void thickOuterTopwearIsRequiredBelowZeroDegreesOnly() {
        assertTrue(WeatherSuitability.requiresThickOuterTopwear(-0.1));
        assertFalse(WeatherSuitability.requiresThickOuterTopwear(0.0));
        assertFalse(WeatherSuitability.requiresThickOuterTopwear(8.0));
    }

    @Test
    void longBottomwearIsRequiredBelowFiveDegreesOnly() {
        assertTrue(WeatherSuitability.requiresLongBottomwear(4.9));
        assertFalse(WeatherSuitability.requiresLongBottomwear(5.0));
        assertFalse(WeatherSuitability.requiresLongBottomwear(20.0));
    }

    @Test
    void waterproofFootwearIsRequiredOnlyWhenPrecipitationIsPresent() {
        assertFalse(WeatherSuitability.requiresWaterproofFootwear(0.0));
        assertTrue(WeatherSuitability.requiresWaterproofFootwear(0.1));
        assertTrue(WeatherSuitability.requiresWaterproofFootwear(12.0));
    }

    @Test
    void outfitInRainIsAppropriateOnlyWithWaterproofFootwear() {
        final Weather rain = weather(20.0, 2.0);

        assertFalse(outfit(false, false, false).isPrecipitationAppropriate(rain));
        assertTrue(outfit(false, false, true).isPrecipitationAppropriate(rain));
    }

    @Test
    void outfitInDryWeatherIsAppropriateWithAnyFootwear() {
        final Weather dry = weather(20.0, 0.0);

        assertTrue(outfit(false, false, false).isPrecipitationAppropriate(dry));
        assertTrue(outfit(false, false, true).isPrecipitationAppropriate(dry));
    }

    @Test
    void temperatureAndPrecipitationAreBothRequiredForOverallSuitability() {
        final Weather coldAndWet = weather(-5.0, 2.0);

        // Warm enough layers, but the footwear lets the rain in.
        assertFalse(outfit(true, true, false).isWeatherAppropriate(coldAndWet));
        // Waterproof, but nothing thick enough for the cold.
        assertFalse(outfit(false, false, true).isWeatherAppropriate(coldAndWet));
        // Both satisfied.
        assertTrue(outfit(true, true, true).isWeatherAppropriate(coldAndWet));
    }

    private static Weather weather(double temperature, double precipitation) {
        return new Weather(LocalDate.of(2026, 1, 1), "Test", temperature, precipitation, 0.0, 0.0, 0);
    }

    private static Outfit outfit(boolean thickOuter, boolean longBottom, boolean waterproofShoes) {
        final InnerTopwear shirt = new InnerTopwear(UUID.randomUUID());

        final OuterTopwear coat = new OuterTopwear(UUID.randomUUID());
        coat.setIsThick(thickOuter);

        final Bottomwear bottom = new Bottomwear(UUID.randomUUID());
        bottom.setIsLong(longBottom);

        final Footwear shoes = new Footwear(UUID.randomUUID());
        shoes.setIsWaterproof(waterproofShoes);

        return new Outfit(shirt, coat, bottom, shoes, null, List.of());
    }
}
