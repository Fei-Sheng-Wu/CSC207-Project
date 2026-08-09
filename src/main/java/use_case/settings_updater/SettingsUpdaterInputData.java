package use_case.settings_updater;

import entity.Settings;

/**
 * Represents the input data for adding an item to the wardrobe.
 */
public class SettingsUpdaterInputData {
    private final Settings settings;

    /**
     * Constructs a new input data.
     *
     * @param settings the settings of the input data
     */
    public SettingsUpdaterInputData(Settings settings) {
        this.settings = settings;
    }

    /**
     * Returns the settings of the input data.
     *
     * @return the settings of the input data
     */
    public Settings getSettings() {
        return settings;
    }
}
