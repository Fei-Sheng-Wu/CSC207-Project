package interface_adapter.settings;

import interface_adapter.AbstractViewModel;

/**
 * Represents the settings view model.
 */
public class SettingsViewModel extends AbstractViewModel {
    public static final String PROPERTY_HIGH_CONTRAST = "highContrast";
    public static final String PROPERTY_LOCATION_CITY = "locationCity";
    public static final String PROPERTY_LOCATION_COUNTRY_CODE = "locationCountryCode";

    private boolean isHighContrast;
    private String locationCity = "";
    private String locationCountryCode = "";

    /**
     * Returns whether high contrast is preferred.
     *
     * @return whether high contrast is preferred
     */
    public boolean isHighContrast() {
        return isHighContrast;
    }

    /**
     * Updates whether high contrast is preferred.
     *
     * @param isHighContrast whether high contrast is preferred
     */
    public void setIsHighContrast(boolean isHighContrast) {
        this.isHighContrast = isHighContrast;
        firePropertyChange(PROPERTY_HIGH_CONTRAST, this.isHighContrast);
    }

    /**
     * Returns the city of the location.
     *
     * @return the city of the location
     */
    public String getLocationCity() {
        return locationCity;
    }

    /**
     * Updates the city of the location.
     *
     * @param city the city of the location
     */
    public void setLocationCity(String city) {
        locationCity = city;
        firePropertyChange(PROPERTY_LOCATION_CITY, locationCity);
    }

    /**
     * Returns the 2-digit country code of the location.
     *
     * @return the 2-digit country code of the location
     */
    public String getLocationCountryCode() {
        return locationCountryCode;
    }

    /**
     * Updates the 2-digit country code of the location.
     *
     * @param countryCode the 2-digit country code of the location
     */
    public void setLocationCountryCode(String countryCode) {
        locationCountryCode = countryCode;
        firePropertyChange(PROPERTY_LOCATION_COUNTRY_CODE, locationCountryCode);
    }
}
