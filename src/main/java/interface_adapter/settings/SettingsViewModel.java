package interface_adapter.settings;

import interface_adapter.AbstractViewModel;

/**
 * Represents the settings view model.
 */
public class SettingsViewModel extends AbstractViewModel {
    private String locationCity = "";
    private String locationCountryCode = "";

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
        firePropertyChange("locationCity", locationCity);
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
        firePropertyChange("locationCountryCode", locationCountryCode);
    }
}
