package database.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import database.AbstractFileDataAccessObject;
import use_case.settings.SettingsDataAccessInterface;

public class LocalSettingsDataAccessObject
    extends AbstractFileDataAccessObject
    implements SettingsDataAccessInterface {
    private static final String KEY_HIGH_CONTRAST = "isHighContrast";
    private static final String KEY_LOCATION_CITY = "locationCity";
    private static final String KEY_LOCATION_COUNTRY = "locationCountry";

    private static final String DEFAULT_HIGH_CONTRAST = "false";
    private static final String DEFAULT_LOCATION_CITY = "Toronto";
    private static final String DEFAULT_LOCATION_COUNTRY = "CA";

    private final String filename;
    private final Properties properties = new Properties();

    /**
     * Constructs a new data access object.
     *
     * @param filename the filename to be used by the object
     * @throws RuntimeException if the object cannot be constructed
     */
    public LocalSettingsDataAccessObject(String filename) {
        this.filename = filename;

        try (FileInputStream input = new FileInputStream(getPath(filename).toFile())) {
            this.properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("The settings cannot be loaded.");
        }
    }

    private void writeUpdates() {
        try (FileOutputStream output = new FileOutputStream(getPath(filename).toFile())) {
            properties.store(output, null);
        } catch (IOException ex) {
            throw new RuntimeException("The settings cannot be updated.");
        }
    }

    @Override
    public boolean isHighContrast() {
        return "true".equals(properties.getProperty(KEY_HIGH_CONTRAST, DEFAULT_HIGH_CONTRAST));
    }

    @Override
    public void setIsHighContrast(boolean isHighContrast) {
        if (isHighContrast) {
            properties.setProperty(KEY_HIGH_CONTRAST, "true");
        } else {
            properties.setProperty(KEY_HIGH_CONTRAST, "false");
        }
        writeUpdates();
    }

    @Override
    public String getLocationCityOrDefault() {
        return properties.getProperty(KEY_LOCATION_CITY, DEFAULT_LOCATION_CITY);
    }

    @Override
    public void setLocationCity(String city) {
        properties.setProperty(KEY_LOCATION_CITY, city);
        writeUpdates();
    }

    @Override
    public String getLocationCountryCodeOrDefault() {
        return properties.getProperty(KEY_LOCATION_COUNTRY, DEFAULT_LOCATION_COUNTRY);
    }

    @Override
    public void setLocationCountryCode(String countryCode) {
        properties.setProperty(KEY_LOCATION_COUNTRY, countryCode);
        writeUpdates();
    }
}
