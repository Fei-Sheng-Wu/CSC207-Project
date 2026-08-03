package use_case.settings_updater;

/**
 * Defines the output boundary for updating the settings.
 */
public interface SettingsUpdaterOutputBoundary {
    /**
     * Outputs a successful response.
     */
    void prepareSuccessView();

    /**
     * Outputs a failed response.
     *
     * @param message the message of the failed response
     */
    void prepareFailView(String message);
}
