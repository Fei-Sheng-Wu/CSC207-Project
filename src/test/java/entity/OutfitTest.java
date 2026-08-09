package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutfitTest {
    private InnerTopwear shirt;
    private OuterTopwear thinJacket;
    private OuterTopwear thickCoat;
    private Bottomwear shorts;
    private Bottomwear pants;
    private Footwear sneakers;
    private Footwear boots;
    private Headwear hat;
    private Accessory jewelry;

    @BeforeEach
    void setUp() {
        shirt = new InnerTopwear(UUID.randomUUID());
        thinJacket = new OuterTopwear(UUID.randomUUID());
        thickCoat = new OuterTopwear(UUID.randomUUID());
        thickCoat.setIsThick(true);
        shorts = new Bottomwear(UUID.randomUUID());
        pants = new Bottomwear(UUID.randomUUID());
        pants.setIsLong(true);
        sneakers = new Footwear(UUID.randomUUID());
        boots = new Footwear(UUID.randomUUID());
        boots.setIsWaterproof(true);
        hat = new Headwear(UUID.randomUUID());
        jewelry = new Accessory(UUID.randomUUID());
    }

    @Test
    void temperatureIsAppropriateWhenAllConditionsHold() {
        final Outfit outfit = new Outfit(
                shirt, thickCoat, pants, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", -5.0, 0.0, 0.0, 0.0, 0);

        assertTrue(outfit.isTemperatureAppropriate(weather));
    }

    @Test
    void testTemperatureMissingOuterTopwear() {
        final Outfit outfit = new Outfit(
                shirt, null, pants, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 7.0, 0.0, 0.0, 0.0, 0);

        assertFalse(outfit.isTemperatureAppropriate(weather));
    }

    @Test
    void testTemperatureWithoutThickTopwear() {
        final Outfit outfit = new Outfit(
                shirt, thinJacket, pants, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", -5.0, 0.0, 0.0, 0.0, 0);

        assertFalse(outfit.isTemperatureAppropriate(weather));
    }

    @Test
    void testTemperatureWithoutLongBottomwear() {
        final Outfit outfit = new Outfit(
                shirt, thinJacket, shorts, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 3.5, 0.0, 0.0, 0.0, 0);

        assertFalse(outfit.isTemperatureAppropriate(weather));
    }

    @Test
    void testPrecipitationWithNonWaterproofFootwearAndNoRain() {
        final Outfit outfit = new Outfit(
                shirt, null, shorts, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 20.0, 0.0, 0.0, 0.0, 0);

        assertTrue(outfit.isPrecipitationAppropriate(weather));
    }

    @Test
    void testPrecipitationWithWaterproofFootwearAndRain() {
        final Outfit outfit = new Outfit(
                shirt, null, shorts, boots, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 20.0, 1.0, 0.0, 0.0, 0);

        assertTrue(outfit.isPrecipitationAppropriate(weather));
    }

    @Test
    void testPrecipitationWithNonWaterproofFootwearAndRain() {
        final Outfit outfit = new Outfit(
                shirt, null, shorts, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 20.0, 1.0, 0.0, 0.0, 0);

        assertFalse(outfit.isPrecipitationAppropriate(weather));
    }

    @Test
    void weatherIsAppropriateIfBothTemperatureAndPrecipitationAreAppropriate() {
        final Outfit outfit = new Outfit(
                shirt, null, shorts, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 20.0, 0.0, 0.0, 0.0, 0);

        assertTrue(outfit.isWeatherAppropriate(weather));
    }

    @Test
    void weatherIsNotAppropriateIfOnlyTemperatureIsAppropriate() {
        final Outfit outfit = new Outfit(
                shirt, null, shorts, sneakers, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 1, 1), "Test", 20.0, 1.0, 0.0, 0.0, 0);

        assertFalse(outfit.isWeatherAppropriate(weather));
    }

    @Test
    void toListReturnsRequiredItemsEvenWhenOptionalItemsAreNull() {
        final Outfit outfit = new Outfit(
                shirt, null, pants, sneakers, null, List.of());

        final List<AbstractWear> items = outfit.toList();
        assertEquals(List.of(shirt, pants, sneakers), items);
    }
}
