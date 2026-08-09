package use_case.settings_retriever;

import entity.Settings;

/**
 * Defines the output boundary for retrieving the settings.
 */
public interface SettingsRetrieverOutputBoundary {
    /**
     * Outputs a successful response.
     *
     * @param settings the settings
     */
    void prepareSuccessView(Settings settings);

    /**
     * Outputs a failed response.
     *
     * @param message the message of the failed response
     */
    void prepareFailView(String message);
}
