package use_case.recommendation_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Footwear;
import entity.InnerTopwear;
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
 * Tests for how widely the recommendation use case searches.
 *
 * <p>These pin the properties that keep a recommendation affordable on a real wardrobe, which the
 * tests of individual criteria cannot see: how much work a wardrobe costs, and which garments an
 * outfit is obliged to include.
 */
class ContextBasedRecommendationSearchTest {
    private static final int CANDIDATE_CEILING = 10000;
    private static final int MATCHING_ACCESSORIES = 5;
    private static final int SMALL_WARDROBE_SIZE = 4;
    private static final int LARGE_WARDROBE_SIZE = 40;

    @Test
    void accessorySearchGrowsLinearlyRatherThanExponentially() {
        final CountingAnalyzer smallAnalyzer = new CountingAnalyzer();
        final CountingAnalyzer largeAnalyzer = new CountingAnalyzer();

        recommendWith(wardrobeOfEachType(SMALL_WARDROBE_SIZE), smallAnalyzer);
        recommendWith(wardrobeOfEachType(LARGE_WARDROBE_SIZE), largeAnalyzer);

        // Each base outfit evaluates one candidate per accessory prefix. Increasing the number of
        // neutral accessories tenfold therefore remains linear instead of producing 2^40 subsets.
        final int smallCandidatesPerPrefix =
                smallAnalyzer.getInvocationCount() / (SMALL_WARDROBE_SIZE + 1);
        final int largeCandidatesPerPrefix =
                largeAnalyzer.getInvocationCount() / (LARGE_WARDROBE_SIZE + 1);
        assertEquals(smallCandidatesPerPrefix, largeCandidatesPerPrefix);
        assertTrue(largeAnalyzer.getInvocationCount() < CANDIDATE_CEILING);
    }

    @Test
    void keepsEveryNeutralAccessoryThatImprovesAverageFondness() {
        final List<AbstractWear> items = new ArrayList<>();
        items.add(wearWithFondness(new InnerTopwear(new UUID(1L, 1L)), 0.0));
        items.add(wearWithFondness(new Bottomwear(new UUID(2L, 1L)), 0.0));
        items.add(wearWithFondness(new Footwear(new UUID(3L, 1L)), 0.0));

        final List<Accessory> accessories = new ArrayList<>();
        for (int index = 0; index < SMALL_WARDROBE_SIZE; index++) {
            accessories.add(wearWithFondness(new Accessory(new UUID(4L, index)), 1.0));
        }
        items.addAll(accessories);
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(new Wardrobe(items), output).recommend(
                new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertNotNull(output.outputData);
        assertEquals(accessories, output.outputData.getOutfit().getAccessories());
        assertEquals(
                (double) accessories.size() / items.size(),
                new FondnessOutfitAnalyzer().analyze(
                        output.outputData.getOutfit(),
                        new RecommendationContext(
                                StubWeatherRepository.MILD, List.of(), List.of(), List.of())
                ).getFondness()
        );
    }

    @Test
    void fetchesOneWardrobeSnapshotPerRecommendation() {
        final StubWardrobeRepository wardrobe = new StubWardrobeRepository(new Wardrobe(basicItems()));
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(wardrobe, output).recommend(
                new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertNotNull(output.outputData);
        assertEquals(1, wardrobe.getFetchCount());
    }

    @Test
    void wearsEveryAccessoryThatSuitsThePreferences() {
        final List<AbstractWear> items = new ArrayList<>(basicItems());
        final List<Accessory> matching = new ArrayList<>();
        for (int index = 0; index < MATCHING_ACCESSORIES; index++) {
            matching.add(wear(new Accessory(new UUID(5L, index)), WearColor.RED, WearStyle.CASUAL));
        }
        items.addAll(matching);
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(new Wardrobe(items), output).recommend(new ContextBasedRecommendationInputData(
                0, List.of(WearColor.RED), List.of(WearStyle.CASUAL)));

        // Wearing an accessory displaces nothing, so every accessory that matches belongs in the
        // outfit. Leaving any of them off would forfeit preference matches and gain nothing.
        assertNotNull(output.outputData);
        assertTrue(output.outputData.getOutfit().getAccessories().containsAll(matching));
    }

    @Test
    void recommendsWhenGarmentHasNoColorOrStyleRecorded() {
        final InnerTopwear unlabelled = new InnerTopwear(new UUID(9L, 1L));
        unlabelled.setCondition(WearCondition.NEW);
        final List<AbstractWear> items = new ArrayList<>(basicItems());
        items.add(unlabelled);
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(new Wardrobe(items), output).recommend(new ContextBasedRecommendationInputData(
                0, List.of(WearColor.RED), List.of(WearStyle.CASUAL)));

        assertNull(output.errorMessage);
        assertNotNull(output.outputData);
    }

    private void recommendWith(Wardrobe wardrobe, CountingAnalyzer analyzer) {
        new ContextBasedRecommendationInteractor(
                new StubWardrobeRepository(wardrobe),
                new StubSettingsRepository(),
                (country, date) -> List.of(),
                new StubWeatherRepository(),
                new CapturingOutputBoundary(),
                List.of(analyzer)
        ).recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));
    }

    private static ContextBasedRecommendationInteractor interactor(Wardrobe wardrobe,
                                                                   RecommendationOutputBoundary output) {
        return interactor(new StubWardrobeRepository(wardrobe), output);
    }

    private static ContextBasedRecommendationInteractor interactor(StubWardrobeRepository wardrobe,
                                                                   RecommendationOutputBoundary output) {
        return new ContextBasedRecommendationInteractor(
                wardrobe,
                new StubSettingsRepository(),
                (country, date) -> List.of(),
                new StubWeatherRepository(),
                output
        );
    }

    private static List<AbstractWear> basicItems() {
        return List.of(
                wear(new InnerTopwear(new UUID(1L, 1L)), WearColor.RED, WearStyle.CASUAL),
                wear(new Bottomwear(new UUID(2L, 1L)), WearColor.RED, WearStyle.CASUAL),
                wear(new Footwear(new UUID(3L, 1L)), WearColor.RED, WearStyle.CASUAL)
        );
    }

    private static Wardrobe wardrobeOfEachType(int countPerType) {
        final List<AbstractWear> items = new ArrayList<>();
        for (int index = 0; index < countPerType; index++) {
            items.add(wear(new InnerTopwear(new UUID(1L, index)), WearColor.BLUE, WearStyle.CASUAL));
            items.add(wear(new Bottomwear(new UUID(2L, index)), WearColor.BLUE, WearStyle.CASUAL));
            items.add(wear(new Footwear(new UUID(3L, index)), WearColor.BLUE, WearStyle.CASUAL));
            items.add(wear(new Accessory(new UUID(4L, index)), WearColor.BLUE, WearStyle.CASUAL));
        }
        return new Wardrobe(items);
    }

    private static <T extends AbstractWear> T wear(T item, WearColor color, WearStyle style) {
        item.setColor(color);
        item.setStyle(style);
        item.setCondition(WearCondition.NEW);
        item.setFondness(1.0);
        return item;
    }

    private static <T extends AbstractWear> T wearWithFondness(T item, double fondness) {
        item.setCondition(WearCondition.NEW);
        item.setFondness(fondness);
        return item;
    }

    private static final class StubWardrobeRepository implements WardrobeDataAccessInterface {
        private final Wardrobe wardrobe;
        private int fetchCount;

        private StubWardrobeRepository(Wardrobe wardrobe) {
            this.wardrobe = wardrobe;
        }

        @Override
        public Wardrobe fetchWardrobe() {
            fetchCount++;
            return wardrobe;
        }

        private int getFetchCount() {
            return fetchCount;
        }

        @Override
        public void saveWardrobe(Wardrobe updated) {
        }
    }

    private static final class StubSettingsRepository implements SettingsDataAccessInterface {
        @Override
        public boolean isHighContrast() {
            return false;
        }

        @Override
        public void setIsHighContrast(boolean isHighContrast) {
        }

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

    private static final class StubWeatherRepository implements WeatherDataAccessInterface {
        private static final Weather MILD =
                new Weather(LocalDate.of(2026, 7, 1), "Clear", 20.0, 0.0, 0.0, 0.0, 0);

        @Override
        public Weather getCurrentByLocation(String location) {
            return MILD;
        }

        @Override
        public List<Weather> getForecastByLocation(String location) {
            return List.of(MILD);
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
