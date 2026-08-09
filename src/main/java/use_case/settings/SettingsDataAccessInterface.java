package use_case.settings;

/**
 * Defines the interface of a settings repository that provides settings data.
 */
public interface SettingsDataAccessInterface {
    /**
     * Returns whether high contrast is preferred.
     *
     * @return whether high contrast is preferred
     */
    boolean isHighContrast();

    /**
     * Updates whether high contrast is preferred.
     *
     * @param isHighContrast whether high contrast is preferred
     */
    void setIsHighContrast(boolean isHighContrast);

    /**
     * Returns the city of the current set location.
     *
     * @return the city of the current set location
     */
    String getLocationCityOrDefault();

    /**
     * Updates the city of the current set location.
     *
     * @param city the city of the current set location
     */
    void setLocationCity(String city);

    /**
     * Returns the 2-digit country code of the current set location.
     *
     * @return the 2-digit country code of the current set location
     */
    String getLocationCountryCodeOrDefault();

    /**
     * Updates the 2-digit country code of the current set location.
     *
     * @param countryCode the 2-digit country code of the current set location
     */
    void setLocationCountryCode(String countryCode);
}
