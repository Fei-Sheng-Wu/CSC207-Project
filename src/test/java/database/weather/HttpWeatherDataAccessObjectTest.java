package database.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import entity.Weather;

/**
 * Tests for the weather API data access object.
 *
 * <p>A live integration test: it calls the real API and needs credentials. It runs when
 * {@code API_BASE_URL_WEATHER} and {@code API_KEY_WEATHER} are set, and is skipped otherwise so
 * that a developer without credentials still gets a green build. This mirrors the gate already
 * used by {@code HttpInspirationDataAccessObjectTest}.
 */
@EnabledIfEnvironmentVariable(named = "API_BASE_URL_WEATHER", matches = ".+")
@EnabledIfEnvironmentVariable(named = "API_KEY_WEATHER", matches = ".+")
public class HttpWeatherDataAccessObjectTest {
    private static Weather currentWeatherByLocation;
    private static List<Weather> forecastByLocation;

    @BeforeAll
    public static void weatherApiSetUp() {
        final HttpWeatherDataAccessObject httpWeather = new HttpWeatherDataAccessObject();
        currentWeatherByLocation = httpWeather.getCurrentByLocation("Toronto");
        forecastByLocation = httpWeather.getForecastByLocation("Toronto");
    }

    @Test
    public void testCurrentWeatherByLocationCreatesValidWeatherObject() {
        assertNotNull(currentWeatherByLocation);
        assertNotNull(currentWeatherByLocation.getCondition());
        assertNotNull(currentWeatherByLocation.getTimestamp());
        assertNotNull(currentWeatherByLocation.getTemperature());
        assertNotNull(currentWeatherByLocation.getPrecipitation());
        assertNotNull(currentWeatherByLocation.getWind());
        assertNotNull(currentWeatherByLocation.getHumidity());
        assertNotNull(currentWeatherByLocation.getUltraviolet());
    }

    @Test
    public void testGetForecastByLocationReturnsValidList() {
        assertNotNull(forecastByLocation);
        assertFalse(forecastByLocation.isEmpty());
        // Size of the list should be 7.
        assertEquals(7, forecastByLocation.size());
    }

    @Test
    public void testForecastContainsValidDataAcrossAllDays() {
        for (Weather weather : forecastByLocation) {
            assertNotNull(weather.getCondition());
            assertNotNull(weather.getTimestamp());
            assertNotNull(weather.getTemperature());
            assertNotNull(weather.getPrecipitation());
            assertNotNull(weather.getWind());
            assertNotNull(weather.getHumidity());
            assertNotNull(weather.getUltraviolet());
        }
    }
}
