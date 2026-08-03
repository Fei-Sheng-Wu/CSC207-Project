package use_case.recommendation_context;

import java.util.List;

import entity.Weather;

/**
 * Defines the interface of a weather repository that provides weather data.
 */
public interface WeatherDataAccessInterface {
    /**
     * Returns the current weather of the specified location.
     *
     * @param location the location as a city name
     * @return the current weather
     * @throws ContextUnavailableException if the weather cannot be obtained
     */
    Weather getCurrentByLocation(String location);

    /**
     * Returns a 7-day weather forecast of the specified location.
     *
     * @param location the location as a city name
     * @return a collection of weather data, each representing a day in the 7-day forecast
     * @throws ContextUnavailableException if the forecast cannot be obtained
     */
    List<Weather> getForecastByLocation(String location);
}
