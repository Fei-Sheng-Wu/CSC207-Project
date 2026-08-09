package use_case.recommendation_context;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Bottomwear;
import entity.Footwear;
import entity.InnerTopwear;
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
 * Tests that a repository failure is presented rather than thrown.
 *
 * <p>The weather and the day's events come from services that can be down, unreachable, or simply
 * unconfigured. When that happens the use case still has to answer its caller, because whoever
 * invoked it — a button handler, most likely — has nowhere sensible to put an exception.
 */
class ContextBasedRecommendationFailureTest {
    @Test
    void presentsFailureWhenTheWeatherCannotBeObtained() {
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        assertDoesNotThrow(() -> {
            interactor(new FailingWeatherRepository(), output)
                    .recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));
        });

        assertNull(output.outputData);
        assertNotNull(output.errorMessage);
        assertTrue(output.errorMessage.contains("could not be retrieved"));
    }

    @Test
    void presentsFailureWhenTheEventsCannotBeObtained() {
        final CapturingOutputBoundary output = new CapturingOutputBoundary();
        final ContextBasedRecommendationInteractor interactor = new ContextBasedRecommendationInteractor(
                new StubWardrobeRepository(),
                new StubSettingsRepository(),
                country -> {
                    throw new ContextUnavailableException("The events service is unavailable.");
                },
                new StubWeatherRepository(),
                output
        );

        assertDoesNotThrow(() -> {
            interactor.recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));
        });

        assertNull(output.outputData);
        assertNotNull(output.errorMessage);
        assertTrue(output.errorMessage.contains("could not be retrieved"));
    }

    @Test
    void stillRecommendsWhenBothRepositoriesAnswer() {
        final CapturingOutputBoundary output = new CapturingOutputBoundary();

        interactor(new StubWeatherRepository(), output)
                .recommend(new ContextBasedRecommendationInputData(0, List.of(), List.of()));

        assertNull(output.errorMessage);
        assertNotNull(output.outputData);
    }

    private static ContextBasedRecommendationInteractor interactor(WeatherDataAccessInterface weather,
                                                                   RecommendationOutputBoundary output) {
        return new ContextBasedRecommendationInteractor(
                new StubWardrobeRepository(),
                new StubSettingsRepository(),
                country -> List.of(),
                weather,
                output
        );
    }

    private static <T extends AbstractWear> T wear(T item) {
        item.setColor(WearColor.RED);
        item.setStyle(WearStyle.CASUAL);
        item.setCondition(WearCondition.NEW);
        return item;
    }

    private static final class StubWardrobeRepository implements WardrobeDataAccessInterface {
        @Override
        public Wardrobe fetchWardrobe() {
            final List<AbstractWear> items = new ArrayList<>();
            items.add(wear(new InnerTopwear(new UUID(0L, 1L))));
            items.add(wear(new Bottomwear(new UUID(0L, 2L))));
            items.add(wear(new Footwear(new UUID(0L, 3L))));
            return new Wardrobe(items);
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

    private static final class FailingWeatherRepository implements WeatherDataAccessInterface {
        @Override
        public Weather getCurrentByLocation(String location) {
            throw new ContextUnavailableException("The weather service is unavailable.");
        }

        @Override
        public List<Weather> getForecastByLocation(String location) {
            throw new ContextUnavailableException("The weather service is unavailable.");
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
}
