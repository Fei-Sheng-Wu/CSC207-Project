package interface_adapter.recommendation_context;

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
import interface_adapter.recommendation.RecommendationPresenter;
import interface_adapter.recommendation.RecommendationViewModel;
import use_case.recommendation_context.ContextBasedRecommendationInteractor;
import use_case.recommendation_context.EventDataAccessInterface;
import use_case.recommendation_context.WeatherDataAccessInterface;
import use_case.settings.SettingsDataAccessInterface;
import use_case.wardrobe.WardrobeDataAccessInterface;

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
        final ContextBasedRecommendationController controller = chain(viewModel, warmWardrobe());

        controller.recommend(List.of(WearColor.RED.name()), List.of(WearStyle.CASUAL.name()));

        assertNotNull(viewModel.getOutfit());
        assertFalse(viewModel.getReason().isEmpty());
        assertTrue(viewModel.getErrorMessage().isEmpty());
    }

    @Test
    void aFailureAlsoTravelsAllTheWayToTheViewModel() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final ContextBasedRecommendationController controller =
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

    private static ContextBasedRecommendationController chain(RecommendationViewModel viewModel,
                                                              Wardrobe wardrobe) {
        final RecommendationPresenter presenter = new RecommendationPresenter(viewModel);
        final ContextBasedRecommendationInteractor interactor =
                new ContextBasedRecommendationInteractor(
                        new StubWardrobeRepository(wardrobe),
                        new StubSettingsRepository(),
                        new StubEventRepository(),
                        new StubWeatherRepository(),
                        presenter
                );
        return new ContextBasedRecommendationController(interactor, new Random(7));
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

    /** No events today, so the event analyzer stays neutral. */
    private static final class StubEventRepository implements EventDataAccessInterface {
        @Override
        public List<Event> getEvents(String country) {
            return List.of();
        }
    }

    /** A mild, dry day, so a minimal wardrobe is always sufficient. */
    private static final class StubWeatherRepository implements WeatherDataAccessInterface {
        @Override
        public Weather getCurrentByLocation(String location) {
            return new Weather(LocalDate.of(2026, 7, 1), "Clear", 22.0, 0.0, 4.0, 40.0, 3);
        }

        @Override
        public List<Weather> getForecastByLocation(String location) {
            return List.of(getCurrentByLocation(location));
        }
    }
}
