package entity;

/**
 * Represents application settings.
 */
public final class Settings {
    private String locationCity = "";
    private String locationCountryCode = "";

    /**
     * Returns the city of the location of the settings.
     *
     * @return the city of the location of the settings
     */
    public String getLocationCity() {
        return locationCity;
    }

    /**
     * Updates the city of the location of the settings.
     *
     * @param city the city of the location of the settings
     */
    public void setLocationCity(String city) {
        locationCity = city;
    }

    /**
     * Returns the 2-digit country code of the location of the settings.
     *
     * @return the 2-digit country code of the location of the settings
     */
    public String getLocationCountryCode() {
        return locationCountryCode;
    }

    /**
     * Updates the 2-digit country code of the location of the settings.
     *
     * @param countryCode the 2-digit country code of the location of the settings
     */
    public void setLocationCountryCode(String countryCode) {
        locationCountryCode = countryCode;
    }
}
