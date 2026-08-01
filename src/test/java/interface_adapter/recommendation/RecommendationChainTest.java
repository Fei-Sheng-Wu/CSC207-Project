package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Bottomwear;
import entity.Event;
import entity.Footwear;
import entity.InnerTopwear;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearStyle;
import entity.Weather;
import use_case.context_based_recommendation.ContextBasedRecommendationInteractor;
import use_case.context_based_recommendation.ContextProvider;
import use_case.context_based_recommendation.EventOutfitAnalyzer;
import use_case.context_based_recommendation.FondnessOutfitAnalyzer;
import use_case.context_based_recommendation.PreferenceOutfitAnalyzer;
import use_case.context_based_recommendation.WeatherOutfitAnalyzer;

/**
 * Exercises the whole recommendation chain: controller to input boundary, the real interactor
 * with all four analyzers, out through the output boundary to the presenter and view model.
 *
 * <p>No Swing component, no HTTP call, and no API credentials are involved. Each end of the
 * chain is reachable in a test only because the interactor depends on abstractions the outer
 * layers implement, rather than on the outer layers themselves.
 */
class RecommendationChainTest {
    @Test
    void aRequestTravelsFromControllerToViewModel() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final RecommendationController controller = chain(viewModel, warmWardrobe());

        controller.recommend(List.of(WearColor.RED), List.of(WearStyle.CASUAL));

        assertNotNull(viewModel.getOutfit());
        assertFalse(viewModel.getReason().isEmpty());
        assertTrue(viewModel.getErrorMessage().isEmpty());
    }

    @Test
    void aFailureAlsoTravelsAllTheWayToTheViewModel() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final RecommendationController controller =
                chain(viewModel, new Wardrobe(new ArrayList<>()));

        controller.recommend(List.of(), List.of());

        assertNull(viewModel.getOutfit());
        assertEquals(
                "A recommendation requires at least one inner topwear, bottomwear, and footwear.",
                viewModel.getErrorMessage()
        );
    }

    @Test
    void theSameSeedProducesTheSameRecommendation() {
        final RecommendationViewModel first = new RecommendationViewModel();
        final RecommendationViewModel second = new RecommendationViewModel();

        chain(first, warmWardrobe()).recommend(List.of(), List.of());
        chain(second, warmWardrobe()).recommend(List.of(), List.of());

        assertEquals(first.getReason(), second.getReason());
    }

    private static RecommendationController chain(RecommendationViewModel viewModel,
                                                  Wardrobe wardrobe) {
        final RecommendationPresenter presenter = new RecommendationPresenter(viewModel);
        final ContextBasedRecommendationInteractor interactor =
                new ContextBasedRecommendationInteractor(
                        wardrobe,
                        new StubContextProvider(),
                        presenter,
                        List.of(
                                new WeatherOutfitAnalyzer(),
                                new EventOutfitAnalyzer(),
                                new PreferenceOutfitAnalyzer(),
                                new FondnessOutfitAnalyzer()
                        )
                );
        return new RecommendationController(interactor, new Random(7));
    }

    private static Wardrobe warmWardrobe() {
        final List<AbstractWear> items = new ArrayList<>();

        final InnerTopwear shirt = new InnerTopwear(UUID.randomUUID());
        shirt.setName("Red Shirt");
        shirt.setColor(WearColor.RED);
        shirt.setStyle(WearStyle.CASUAL);
        items.add(shirt);

        final Bottomwear jeans = new Bottomwear(UUID.randomUUID());
        jeans.setName("Jeans");
        jeans.setColor(WearColor.BLACK);
        jeans.setStyle(WearStyle.CASUAL);
        items.add(jeans);

        final Footwear sneakers = new Footwear(UUID.randomUUID());
        sneakers.setName("Sneakers");
        sneakers.setColor(WearColor.WHITE);
        sneakers.setStyle(WearStyle.CASUAL);
        items.add(sneakers);

        return new Wardrobe(items);
    }

    /**
     * A mild, dry day with no events, so a minimal wardrobe is always sufficient.
     */
    private static final class StubContextProvider implements ContextProvider {
        @Override
        public Weather getCurrentWeather() {
            return new Weather(LocalDate.of(2026, 7, 1), "Clear", 22.0, 0.0, 4.0, 40.0, 3);
        }

        @Override
        public List<Event> getCurrentEvents() {
            return List.of();
        }
    }
}
