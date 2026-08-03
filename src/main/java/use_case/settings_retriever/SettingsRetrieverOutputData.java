package use_case.settings_retriever;

/**
 * Represents the output data for settings retriever.
 */
public class SettingsRetrieverOutputData {
    private final String locationCity;
    private final String locationCountryCode;

    /**
     * Constructs a new output data.
     *
     * @param locationCity        the city of the location of the output data
     * @param locationCountryCode the 2-digit country code of the location of the output data
     */
    public SettingsRetrieverOutputData(String locationCity, String locationCountryCode) {
        this.locationCity = locationCity;
        this.locationCountryCode = locationCountryCode;
    }

    /**
     * Returns the city of the location of the output data.
     *
     * @return the city of the location of the output data
     */
    public String getLocationCity() {
        return locationCity;
    }

    /**
     * Returns the 2-digit country code of the location of the output data.
     *
     * @return the 2-digit country code of the location of the output data
     */
    public String getLocationCountryCode() {
        return locationCountryCode;
    }
}
