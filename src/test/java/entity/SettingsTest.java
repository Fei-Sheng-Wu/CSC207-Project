package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SettingsTest {
    @Test
    void testHighContrast() {
        final Settings settings = new Settings();
        settings.setIsHighContrast(true);

        assertTrue(settings.isHighContrast());
    }

    @Test
    void testLocationCity() {
        final Settings settings = new Settings();
        settings.setLocationCity("Toronto");

        assertEquals("Toronto", settings.getLocationCity());
    }

    @Test
    void testLocationCountryCode() {
        final Settings settings = new Settings();
        settings.setLocationCountryCode("CA");

        assertEquals("CA", settings.getLocationCountryCode());
    }
}
