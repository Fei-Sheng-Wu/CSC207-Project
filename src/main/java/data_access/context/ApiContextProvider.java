package data_access.context;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Event;
import entity.Weather;
import use_case.context_based_recommendation.ContextProvider;
import use_case.context_based_recommendation.HolidayDataAccessInterface;
import use_case.context_based_recommendation.WeatherDataAccessInterface;

/**
 * Supplies the recommendation use case with real environmental context by composing the
 * weather and holiday repositories.
 *
 * <p>This class is the adapter that satisfies {@link ContextProvider}. The use case layer
 * declares that port and never learns which repositories sit behind it, so the interactor can
 * be exercised in tests with an in-memory provider and in production with this one.
 *
 * <p>The {@link Clock} is injected rather than calling {@code OffsetDateTime.now()} directly so
 * that "which events are happening today" is deterministic under test — the same reasoning
 * behind the seed the interactor uses to break ties.
 */
public class ApiContextProvider implements ContextProvider {
    private final WeatherDataAccessInterface weatherDataAccess;
    private final HolidayDataAccessInterface holidayDataAccess;
    private final Clock clock;
    private final String city;
    private final String countryCode;

    /**
     * Constructs a new context provider.
     *
     * @param weatherDataAccess the weather repository
     * @param holidayDataAccess the holiday repository
     * @param clock             the clock used to determine the current date
     * @param city              the city used for weather lookups
     * @param countryCode       the 2-digit country code used for holiday lookups
     */
    public ApiContextProvider(WeatherDataAccessInterface weatherDataAccess,
                              HolidayDataAccessInterface holidayDataAccess,
                              Clock clock,
                              String city,
                              String countryCode) {
        this.weatherDataAccess = weatherDataAccess;
        this.holidayDataAccess = holidayDataAccess;
        this.clock = clock;
        this.city = city;
        this.countryCode = countryCode;
    }

    @Override
    public Weather getCurrentWeather() {
        return weatherDataAccess.getCurrentByLocation(city);
    }

    @Override
    public List<Event> getCurrentEvents() {
        final LocalDate today = LocalDate.now(clock);

        final List<Event> holidays =
            holidayDataAccess.getHolidays(countryCode, today.getYear());

        final List<Event> currentEvents = new ArrayList<>();

        for (Event event : holidays) {
            final LocalDate startDate =
                event.getDateStart().toLocalDate();
            final LocalDate endDate =
                event.getDateEnd().toLocalDate();

            final boolean hasStarted =
                !today.isBefore(startDate);
            final boolean hasNotEnded =
                !today.isAfter(endDate);

            if (hasStarted && hasNotEnded) {
                currentEvents.add(event);
            }
        }

        return List.copyOf(currentEvents);
    }
}
