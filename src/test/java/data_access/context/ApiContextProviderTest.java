package data_access.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.Event;
import entity.Weather;
import use_case.context_based_recommendation.HolidayDataAccessInterface;
import use_case.context_based_recommendation.WeatherDataAccessInterface;

/**
 * Tests for the context provider adapter.
 *
 * <p>Every test here runs against stub repositories: no network, no API credentials, no clock
 * drift. That is possible only because the use case depends on the
 * {@code WeatherDataAccessInterface} and {@code HolidayDataAccessInterface} abstractions rather
 * than on the HTTP classes that implement them.
 */
class ApiContextProviderTest {
    private static final LocalDate TODAY = LocalDate.of(2025, 7, 1);
    private static final Clock FIXED_CLOCK =
            Clock.fixed(TODAY.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);

    @Test
    void asksTheWeatherRepositoryForTheConfiguredCity() {
        final Weather expected = weather();
        final RecordingWeatherDataAccess weatherDataAccess = new RecordingWeatherDataAccess(expected);

        final ApiContextProvider provider = new ApiContextProvider(
                weatherDataAccess,
                new StubHolidayDataAccess(List.of()),
                FIXED_CLOCK,
                "Toronto",
                "CA"
        );

        assertSame(expected, provider.getCurrentWeather());
        assertEquals("Toronto", weatherDataAccess.requestedLocation);
    }

    @Test
    void asksTheHolidayRepositoryForTheConfiguredCountryAndTheClockYear() {
        final StubHolidayDataAccess holidayDataAccess = new StubHolidayDataAccess(List.of());

        final ApiContextProvider provider = new ApiContextProvider(
                new RecordingWeatherDataAccess(weather()),
                holidayDataAccess,
                FIXED_CLOCK,
                "Toronto",
                "CA"
        );

        provider.getCurrentEvents();

        assertEquals("CA", holidayDataAccess.requestedCountry);
        assertEquals(2025, holidayDataAccess.requestedYear);
    }

    @Test
    void returnsOnlyEventsHappeningToday() {
        final Event today = event("Canada Day", TODAY, TODAY);
        final Event spanningToday = event("Summer Festival", TODAY.minusDays(2), TODAY.plusDays(2));
        final Event past = event("New Year's Day", TODAY.minusMonths(6), TODAY.minusMonths(6));
        final Event future = event("Christmas Day", TODAY.plusMonths(5), TODAY.plusMonths(5));

        final ApiContextProvider provider = new ApiContextProvider(
                new RecordingWeatherDataAccess(weather()),
                new StubHolidayDataAccess(List.of(past, today, future, spanningToday)),
                FIXED_CLOCK,
                "Toronto",
                "CA"
        );

        final List<Event> current = provider.getCurrentEvents();

        assertEquals(2, current.size());
        assertTrue(current.contains(today));
        assertTrue(current.contains(spanningToday));
    }

    @Test
    void returnsEmptyListWhenNoHolidaysMatch() {
        final ApiContextProvider provider = new ApiContextProvider(
                new RecordingWeatherDataAccess(weather()),
                new StubHolidayDataAccess(List.of(
                        event("New Year's Day", TODAY.minusMonths(6), TODAY.minusMonths(6))
                )),
                FIXED_CLOCK,
                "Toronto",
                "CA"
        );

        assertTrue(provider.getCurrentEvents().isEmpty());
    }

    private static Weather weather() {
        return new Weather(TODAY, "Clear", 22.0, 0.0, 5.0, 40.0, 3);
    }

    private static Event event(String name, LocalDate start, LocalDate end) {
        return new Event(
                name,
                start.atStartOfDay().atOffset(ZoneOffset.UTC),
                end.atTime(23, 59).atOffset(ZoneOffset.UTC),
                List.of(),
                List.of()
        );
    }

    private static final class RecordingWeatherDataAccess implements WeatherDataAccessInterface {
        private final Weather weather;
        private String requestedLocation;

        private RecordingWeatherDataAccess(Weather weather) {
            this.weather = weather;
        }

        @Override
        public Weather getCurrentByLocation(String location) {
            requestedLocation = location;
            return weather;
        }

        @Override
        public List<Weather> getForecastByLocation(String location) {
            return List.of(weather);
        }
    }

    private static final class StubHolidayDataAccess implements HolidayDataAccessInterface {
        private final List<Event> holidays;
        private String requestedCountry;
        private int requestedYear;

        private StubHolidayDataAccess(List<Event> holidays) {
            this.holidays = holidays;
        }

        @Override
        public List<Event> getHolidays(String country, int year) {
            requestedCountry = country;
            requestedYear = year;
            return holidays;
        }
    }
}
