package data_access.weather;

import org.json.JSONException;

import entity.Weather;

import java.util.List;

/**
 * WeatherRepository is an interface that defines the methods that
 * the WeatherRepository implementation must provide to retrieve weather data.
 */
public interface WeatherRepository {
    /**
     * A method that returns the current weather for a given location.
     * @param location is the city for which to retrieve its current weather.
     * @return the Weather entity containing current meteorological data,
     * including temperature, precipitation, humidity.
     * @throws JSONException if an error occurs.
     */
    Weather getByLocation(String location) throws JSONException;

    /**
     * A method that returns a 7-day weather forecast for a given location.
     * @param location is the city to retrieve the 7-day forecast for.
     * @return a List of Weather entities, each representing a day in the 7-day forecast.
     * @throws JSONException if an error occurs.
     */
    List<Weather> getForecastByLocation(String location) throws JSONException;
}
