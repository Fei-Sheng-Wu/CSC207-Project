package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherTest {
    private Weather weather;

    @BeforeEach
    void setUp() {
        weather = new Weather(
            LocalDate.of(2000, 1, 1),
            "TEST CONDITION",
            10.0,
            1.0,
            3.0,
            2.0,
            1
        );
    }

    @Test
    void testTimestamp() {
        assertEquals(2000, weather.getTimestamp().getYear());
        assertEquals(1, weather.getTimestamp().getMonthValue());
        assertEquals(1, weather.getTimestamp().getDayOfMonth());
    }

    @Test
    void testCondition() {
        assertEquals("TEST CONDITION", weather.getCondition());
    }

    @Test
    void testTemperature() {
        assertEquals(10.0, weather.getTemperature());
    }

    @Test
    void testPrecipitation() {
        assertEquals(1.0, weather.getPrecipitation());
    }

    @Test
    void testWind() {
        assertEquals(3.0, weather.getWind());
    }

    @Test
    void testHumidity() {
        assertEquals(2.0, weather.getHumidity());
    }

    @Test
    void testUltraviolet() {
        assertEquals(1, weather.getUltraviolet());
    }
}
