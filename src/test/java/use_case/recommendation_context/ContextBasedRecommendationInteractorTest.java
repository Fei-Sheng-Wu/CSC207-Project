package use_case.recommendation_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Bottomwear;
import entity.Event;
import entity.Footwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Outfit;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import entity.Weather;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;
import use_case.settings.SettingsDataAccessInterface;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Tests for the context-based recommendation interactor.
 *
 * <p>Every repository the interactor depends on is stubbed here, so the whole use case runs with
 * no network access, no API credentials, and no user interface.
 */
public class ContextBasedRecommendationInteractorTest {
    private static final UUID RED_SHIRT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID BLUE_SHIRT_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void recommendsWeatherAndEventAppropriateOutfitWithExplanation() {
        final InnerTopwear redShirt = wear(new InnerTopwear(RED_SHIRT_ID), WearColor.RED, WearStyle.CASUAL, 0.8);
        final InnerTopwear blueShirt = wear(
                new InnerTopwear(BLUE_SHIRT_ID), WearColor.BLUE, WearStyle.FORMAL, 1.0);
        final OuterTopwear coat = wear(
                new OuterTopwear(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                WearColor.WHITE,
                WearStyle.CASUAL,
                0.9
        );
        coat.setIsThick(true);
        final Bottomwear jeans = wear(
                new Bottomwear(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                0.9
        );
        jeans.setIsLong(true);
        final Footwear boots = wear(
                new Footwear(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                0.9
        );
        boots.setIsWaterproof(true);

        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>(List.of(redShirt, blueShirt, coat, jeans, boots)));
        final Event canadaDay = new Event(
                "Canada Day",
                OffsetDateTime.parse("2026-07-01T00:00:00-04:00"),
                OffsetDateTime.parse("2026-07-01T23:59:59-04:00"),
                List.of(WearColor.RED, WearColor.WHITE),
                List.of(WearStyle.CASUAL)
        );
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(wardrobe, weather(-5.0, 2.0), List.of(canadaDay), output)
                .recommend(new ContextBasedRecommendationInputData(
                        207,
                        List.of(WearColor.RED, WearColor.WHITE),
                        List.of(WearStyle.CASUAL)
                ));

        assertNull(output.errorMessage);
        assertNotNull(output.outputData);
        assertSame(redShirt, output.outputData.getOutfit().getTopwearInner());
        assertSame(coat, output.outputData.getOutfit().getTopwearOuter());
        assertSame(jeans, output.outputData.getOutfit().getBottomwear());
        assertSame(boots, output.outputData.getOutfit().getFootwear());
        assertTrue(output.outputData.getReason().contains("Thick outerwear"));
        assertTrue(output.outputData.getReason().contains("Waterproof footwear"));
        assertTrue(output.outputData.getReason().contains("Canada Day"));
        assertTrue(output.outputData.getReason().contains("preferences"));
    }

    @Test
    void sameSeedIsDeterministicWhenWardrobeOrderChanges() {
        final InnerTopwear first = wear(
                new InnerTopwear(RED_SHIRT_ID), WearColor.RED, WearStyle.CASUAL, 1.0);
        final InnerTopwear second = wear(
                new InnerTopwear(BLUE_SHIRT_ID), WearColor.BLUE, WearStyle.CASUAL, 1.0);
        final Bottomwear bottom = wear(
                new Bottomwear(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final Footwear footwear = wear(
                new Footwear(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final Weather weather = weather(20.0, 0.0);
        final CapturingOutputBoundary firstOutput = new CapturingOutputBoundary();
        final CapturingOutputBoundary secondOutput = new CapturingOutputBoundary();

        interactor(
                new Wardrobe(new ArrayList<>(List.of(first, second, bottom, footwear))),
                weather, List.of(), firstOutput
        ).recommend(new ContextBasedRecommendationInputData(42, List.of(), List.of()));
        interactor(
                new Wardrobe(new ArrayList<>(List.of(footwear, bottom, second, first))),
                weather, List.of(), secondOutput
        ).recommend(new ContextBasedRecommendationInputData(42, List.of(), List.of()));

        assertEquals(
                firstOutput.outputData.getOutfit().getTopwearInner().getUuid(),
                secondOutput.outputData.getOutfit().getTopwearInner().getUuid()
        );
    }

    @Test
    void reportsFailureWhenRequiredCategoryIsMissing() {
        final InnerTopwear shirt = wear(
                new InnerTopwear(RED_SHIRT_ID), WearColor.RED, WearStyle.CASUAL, 1.0);
        final Footwear footwear = wear(
                new Footwear(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(
                new Wardrobe(new ArrayList<>(List.of(shirt, footwear))),
                weather(20.0, 0.0), List.of(), output
        ).recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertNull(output.outputData);
        assertTrue(output.errorMessage.contains("inner topwear, bottomwear, and footwear"));
    }

    @Test
    void reportsFailureWhenRainHasNoWaterproofFootwear() {
        final InnerTopwear shirt = wear(
                new InnerTopwear(RED_SHIRT_ID), WearColor.RED, WearStyle.CASUAL, 1.0);
        final Bottomwear bottom = wear(
                new Bottomwear(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final Footwear footwear = wear(
                new Footwear(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(
                new Wardrobe(new ArrayList<>(List.of(shirt, bottom, footwear))),
                weather(20.0, 1.0), List.of(), output
        ).recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertNull(output.outputData);
        assertEquals("No outfit in the wardrobe is suitable for the current context.", output.errorMessage);
    }

    @Test
    void filtersWeatherIneligibleItemsBeforeAnalyzingCandidates() {
        final InnerTopwear firstShirt = wear(
                new InnerTopwear(RED_SHIRT_ID), WearColor.RED, WearStyle.CASUAL, 1.0);
        final InnerTopwear secondShirt = wear(
                new InnerTopwear(BLUE_SHIRT_ID), WearColor.BLUE, WearStyle.CASUAL, 1.0);
        final OuterTopwear thinJacket = wear(
                new OuterTopwear(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final OuterTopwear thickCoat = wear(
                new OuterTopwear(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        thickCoat.setIsThick(true);
        final Bottomwear shorts = wear(
                new Bottomwear(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final Bottomwear pants = wear(
                new Bottomwear(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        pants.setIsLong(true);
        final List<Footwear> footwears = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            final Footwear footwear = wear(
                    new Footwear(new UUID(0L, 7L + index)),
                    WearColor.BLACK,
                    WearStyle.CASUAL,
                    1.0
            );
            footwear.setIsWaterproof(index == 0);
            footwears.add(footwear);
        }
        final List<AbstractWear> items = new ArrayList<>(List.of(
                firstShirt, secondShirt, thinJacket, thickCoat, shorts, pants
        ));
        items.addAll(footwears);
        final CountingAnalyzer analyzer = new CountingAnalyzer();
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(new Wardrobe(items), weather(-5.0, 2.0), List.of(), output, List.of(analyzer))
                .recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertNotNull(output.outputData);
        assertEquals(2, analyzer.getInvocationCount());
        assertSame(thickCoat, output.outputData.getOutfit().getTopwearOuter());
        assertSame(pants, output.outputData.getOutfit().getBottomwear());
        assertTrue(output.outputData.getOutfit().getFootwear().isWaterproof());
    }

    @Test
    void analyzesSingleCandidateBeforePresentingIt() {
        final InnerTopwear shirt = wear(
                new InnerTopwear(RED_SHIRT_ID), WearColor.RED, WearStyle.CASUAL, 1.0);
        final Bottomwear bottom = wear(
                new Bottomwear(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        bottom.setIsLong(true);
        final Footwear footwear = wear(
                new Footwear(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                WearColor.BLACK,
                WearStyle.CASUAL,
                1.0
        );
        final CountingAnalyzer analyzer = new CountingAnalyzer();
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(
                new Wardrobe(new ArrayList<>(List.of(shirt, bottom, footwear))),
                weather(20.0, 0.0), List.of(), output, List.of(analyzer)
        ).recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertEquals(1, analyzer.getInvocationCount());
        assertEquals("The candidate was analyzed.", output.outputData.getReason());
    }

    private static ContextBasedRecommendationInteractor interactor(Wardrobe wardrobe,
                                                                   Weather weather,
                                                                   List<Event> events,
                                                                   RecommendationOutputBoundary output) {
        return new ContextBasedRecommendationInteractor(
                new StubWardrobeRepository(wardrobe),
                new StubSettingsRepository(),
                new StubEventRepository(events),
                new StubWeatherRepository(weather),
                output
        );
    }

    private static ContextBasedRecommendationInteractor interactor(Wardrobe wardrobe,
                                                                   Weather weather,
                                                                   List<Event> events,
                                                                   RecommendationOutputBoundary output,
                                                                   List<OutfitAnalyzer> analyzers) {
        return new ContextBasedRecommendationInteractor(
                new StubWardrobeRepository(wardrobe),
                new StubSettingsRepository(),
                new StubEventRepository(events),
                new StubWeatherRepository(weather),
                output,
                analyzers
        );
    }

    private static Weather weather(double temperature, double precipitation) {
        return new Weather(LocalDate.of(2026, 7, 1), "Test", temperature, precipitation, 0.0, 0.0, 0);
    }

    private static <T extends AbstractWear> T wear(T item,
                                                   WearColor color,
                                                   WearStyle style,
                                                   double fondness) {
        item.setColor(color);
        item.setStyle(style);
        item.setCondition(WearCondition.NEW);
        item.setFondness(fondness);
        return item;
    }

    private static final class StubWardrobeRepository implements WardrobeDataAccessInterface {
        private final Wardrobe wardrobe;

        private StubWardrobeRepository(Wardrobe wardrobe) {
            this.wardrobe = wardrobe;
        }

        @Override
        public Wardrobe fetchWardrobe() {
            return wardrobe;
        }

        @Override
        public void saveWardrobe(Wardrobe updated) {
        }
    }

    private static final class StubSettingsRepository implements SettingsDataAccessInterface {
        @Override
        public String getLocationCityOrDefault() {
            return "Toronto";
        }

        @Override
        public void setLocationCity(String city) {
        }

        @Override
        public String getLocationCountryCodeOrDefault() {
            return "CA";
        }

        @Override
        public void setLocationCountryCode(String countryCode) {
        }
    }

    private static final class StubEventRepository implements EventDataAccessInterface {
        private final List<Event> events;

        private StubEventRepository(List<Event> events) {
            this.events = events;
        }

        @Override
        public List<Event> getEvents(String country) {
            return events;
        }
    }

    private static final class StubWeatherRepository implements WeatherDataAccessInterface {
        private final Weather weather;

        private StubWeatherRepository(Weather weather) {
            this.weather = weather;
        }

        @Override
        public Weather getCurrentByLocation(String location) {
            return weather;
        }

        @Override
        public List<Weather> getForecastByLocation(String location) {
            return List.of(weather);
        }
    }

    private static final class CapturingOutputBoundary implements RecommendationOutputBoundary {
        private RecommendationOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(RecommendationOutputData recommendationOutputData) {
            this.outputData = recommendationOutputData;
        }

        @Override
        public void prepareFailView(String message) {
            this.errorMessage = message;
        }
    }

    private static final class CountingAnalyzer implements OutfitAnalyzer {
        private int invocationCount;

        @Override
        public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
            invocationCount++;
            return new OutfitAnalysis(true, 0, 0, 0.0, List.of("The candidate was analyzed."));
        }

        private int getInvocationCount() {
            return invocationCount;
        }
    }
}
