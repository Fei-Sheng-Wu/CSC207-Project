package entity;

import java.time.ZonedDateTime;

/**
 * Represents a weather condition.
 */
public final class Weather {
    private final String condition;
    private final ZonedDateTime timestamp;
    private final double temperature;
    private final double precipitation;
    private final double wind;
    private final double humidity;
    private final int ultraviolet;

    /**
     * Constructs a new weather.
     *
     * @param condition     the condition of the weather
     * @param timestamp     the timestamp of the weather
     * @param temperature   the temperature in Celsius of the weather
     * @param precipitation the precipitation in millimeter of the weather
     * @param wind          the wind speed in KPH of the weather
     * @param humidity      the humidity percentage of the weather
     * @param ultraviolet   the ultraviolet index of the weather
     */
    public Weather(String condition,
                   ZonedDateTime timestamp,
                   double temperature,
                   double precipitation,
                   double wind,
                   double humidity,
                   int ultraviolet) {
        this.condition = condition;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
        this.humidity = humidity;
        this.ultraviolet = ultraviolet;
    }

    /**
     * Returns the condition of the weather.
     *
     * @return the condition of the weather
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the timestamp of the weather.
     *
     * @return the timestamp of the weather
     */
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the temperature in Celsius of the weather.
     *
     * @return the temperature in Celsius of the weather
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Returns the precipitation in millimeter of the weather.
     *
     * @return the precipitation in millimeter of the weather
     */
    public double getPrecipitation() {
        return precipitation;
    }

    /**
     * Returns the wind speed in KPH of the weather.
     *
     * @return the wind speed in KPH of the weather
     */
    public double getWind() {
        return wind;
    }

    /**
     * Returns the humidity percentage of the weather.
     *
     * @return the humidity percentage of the weather
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Returns the ultraviolet index of the weather.
     *
     * @return the ultraviolet index of the weather
     */
    public int getUltraviolet() {
        return ultraviolet;
    }
}
