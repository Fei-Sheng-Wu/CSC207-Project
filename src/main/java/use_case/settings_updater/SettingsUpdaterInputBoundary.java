package use_case.settings_updater;

/**
 * Defines the input boundary for updating the settings.
 */
public interface SettingsUpdaterInputBoundary {
    /**
     * Updates the settings.
     *
     * @param request the input data
     */
    void update(SettingsUpdaterInputData request);
}
