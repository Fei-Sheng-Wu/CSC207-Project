package database.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocalSettingsDataAccessObjectTest {
    private static final String TEST_FILE = "user_test.properties";

    private LocalSettingsDataAccessObject settingsDao;

    @BeforeEach
    public void setUp() {
        settingsDao = new LocalSettingsDataAccessObject(TEST_FILE);
        settingsDao.setIsHighContrast(false);
        settingsDao.setLocationCity("");
        settingsDao.setLocationCountryCode("");
    }

    @Test
    public void testSaveIsHighContrast() {
        settingsDao.setIsHighContrast(true);
        assertTrue(settingsDao.isHighContrast());

        settingsDao.setIsHighContrast(false);
        assertFalse(settingsDao.isHighContrast());
    }

    @Test
    public void testSaveLocationCity() {
        settingsDao.setLocationCity("Toronto");
        assertEquals("Toronto", settingsDao.getLocationCityOrDefault());
    }

    @Test
    public void testSaveLocationCountryCode() {
        settingsDao.setLocationCountryCode("CA");
        assertEquals("CA", settingsDao.getLocationCountryCodeOrDefault());
    }
}
