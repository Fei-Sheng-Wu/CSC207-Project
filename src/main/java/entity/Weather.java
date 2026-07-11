package entity;

import java.time.ZonedDateTime;

/**
 * Represents the weather conditions for a specific time.
 * Used by the recommender to filter appropriate clothing.
 */
public class Weather {
    private final ZonedDateTime timestamp;
    private final String condition;

    private final double temperature;
    private final double precipitation;
    private final double wind;

    private final double humidity;

    private final double feelsLike;
    private final int ultraviolet;

    public Weather(ZonedDateTime timestamp, String condition, double temperature, double precipitation,
                   double wind, double humidity, int ultraviolet, double feelsLike) {
        this.timestamp = timestamp;
        this.condition = condition;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
        this.humidity = humidity;
        this.ultraviolet = ultraviolet;
        this.feelsLike = feelsLike;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
    public String getCondition() {
        return condition;
    }
    public double getTemperature() {
        return temperature;
    }
    public double getPrecipitation() {
        return precipitation;
    }
    public double getWind() {
        return wind;
    }
    public double getHumidity() {
        return humidity;
    }
    public double getFeelsLike() {
        return feelsLike;
    }
    public int getUltraviolet() {
        return ultraviolet;
    }
}
