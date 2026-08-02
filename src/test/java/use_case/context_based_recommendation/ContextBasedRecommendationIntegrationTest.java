package use_case.context_based_recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import entity.Weather;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;
import use_case.recommendation_context.ContextBasedRecommendationInputBoundary;
import use_case.recommendation_context.ContextBasedRecommendationInputData;
import use_case.recommendation_context.ContextBasedRecommendationInteractor;
import use_case.recommendation_context.EventOutfitAnalyzer;
import use_case.recommendation_context.FondnessOutfitAnalyzer;
import use_case.recommendation_context.PreferenceOutfitAnalyzer;
import use_case.recommendation_context.WeatherOutfitAnalyzer;

/** Exercises the complete use case from its input boundary to its success output boundary. */
class ContextBasedRecommendationIntegrationTest {
    @Test
    void requestFlowsThroughAllAnalyzersToSuccessView() {
        final InnerTopwear redShirt = wear(
                new InnerTopwear(id(1)), WearColor.RED, WearStyle.CASUAL, 0.9);
        final InnerTopwear blueShirt = wear(
                new InnerTopwear(id(2)), WearColor.BLUE, WearStyle.FORMAL, 1.0);
        final OuterTopwear winterCoat = wear(
                new OuterTopwear(id(3)), WearColor.WHITE, WearStyle.CASUAL, 0.9);
        winterCoat.setIsThick(true);
        final Bottomwear longJeans = wear(
                new Bottomwear(id(4)), WearColor.BLACK, WearStyle.CASUAL, 0.9);
        longJeans.setIsLong(true);
        final Footwear waterproofBoots = wear(
                new Footwear(id(5)), WearColor.BLACK, WearStyle.CASUAL, 0.9);
        waterproofBoots.setIsWaterproof(true);
        final Footwear formalShoes = wear(
                new Footwear(id(6)), WearColor.BLACK, WearStyle.FORMAL, 1.0);

        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>(List.of(
                redShirt, blueShirt, winterCoat, longJeans, waterproofBoots, formalShoes
        )));
        final Weather weather = new Weather(
                LocalDate.of(2026, 7, 18), "Cold rain", -5.0, 2.0, 20.0, 80.0, 1
        );
        final Event event = new Event(
                "Casual team meeting",
                OffsetDateTime.parse("2026-07-18T09:00:00-04:00"),
                OffsetDateTime.parse("2026-07-18T10:00:00-04:00"),
                List.of(WearColor.RED, WearColor.WHITE),
                List.of(WearStyle.CASUAL)
        );
        final InMemoryContextProvider contextProvider =
                new InMemoryContextProvider(weather, List.of(event));
        final SuccessPresenter presenter = new SuccessPresenter();
        final ContextBasedRecommendationInputBoundary inputBoundary =
                new ContextBasedRecommendationInteractor(
                        wardrobe,
                        contextProvider,
                        presenter,
                        List.of(
                                new WeatherOutfitAnalyzer(),
                                new EventOutfitAnalyzer(),
                                new PreferenceOutfitAnalyzer(),
                                new FondnessOutfitAnalyzer()
                        )
                );

        inputBoundary.recommend(new ContextBasedRecommendationInputData(
                207,
                List.of(WearColor.RED, WearColor.WHITE),
                List.of(WearStyle.CASUAL)
        ));

        assertEquals(1, presenter.getSuccessCount());
        assertNotNull(presenter.getOutputData());
        assertSame(redShirt, presenter.getOutputData().getOutfit().getTopwearInner());
        assertSame(winterCoat, presenter.getOutputData().getOutfit().getTopwearOuter());
        assertSame(longJeans, presenter.getOutputData().getOutfit().getBottomwear());
        assertSame(waterproofBoots, presenter.getOutputData().getOutfit().getFootwear());
        assertTrue(presenter.getOutputData().getReason().contains("cold temperature"));
        assertTrue(presenter.getOutputData().getReason().contains("Waterproof footwear"));
        assertTrue(presenter.getOutputData().getReason().contains("Casual team meeting"));
        assertTrue(presenter.getOutputData().getReason().contains("preferences"));
        assertTrue(presenter.getOutputData().getReason().contains("average fondness"));
    }

    private static UUID id(long value) {
        return new UUID(0L, value);
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

    private static final class InMemoryContextProvider implements ContextProvider {
        private final Weather weather;
        private final List<Event> events;

        private InMemoryContextProvider(Weather weather, List<Event> events) {
            this.weather = weather;
            this.events = List.copyOf(events);
        }

        @Override
        public Weather getCurrentWeather() {
            return weather;
        }

        @Override
        public List<Event> getCurrentEvents() {
            return events;
        }
    }

    private static final class SuccessPresenter implements RecommendationOutputBoundary {
        private RecommendationOutputData outputData;
        private int successCount;

        @Override
        public void prepareSuccessView(RecommendationOutputData recommendationOutputData) {
            outputData = recommendationOutputData;
            successCount++;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            throw new AssertionError("Expected success but received: " + errorMessage);
        }

        private RecommendationOutputData getOutputData() {
            return outputData;
        }

        private int getSuccessCount() {
            return successCount;
        }
    }
}
