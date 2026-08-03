package use_case.settings_updater;

/**
 * Represents the input data for adding an item to the wardrobe.
 */
public class SettingsUpdaterInputData {
    private final String locationCity;
    private final String locationCountryCode;

    /**
     * Constructs a new input data.
     *
     * @param locationCity        the city of the location of the input data
     * @param locationCountryCode the 2-digit country code of the location of the input data
     */
    public SettingsUpdaterInputData(String locationCity, String locationCountryCode) {
        this.locationCity = locationCity;
        this.locationCountryCode = locationCountryCode;
    }

    /**
     * Returns the city of the location of the input data.
     *
     * @return the city of the location of the input data
     */
    public String getLocationCity() {
        return locationCity;
    }

    /**
     * Returns the 2-digit country code of the location of the input data.
     *
     * @return the 2-digit country code of the location of the input data
     */
    public String getLocationCountryCode() {
        return locationCountryCode;
    }
}
