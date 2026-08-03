package use_case.settings_retriever;

/**
 * Defines the output boundary for retrieving the settings.
 */
public interface SettingsRetrieverOutputBoundary {
    /**
     * Outputs a successful response.
     *
     * @param output the output data
     */
    void prepareSuccessView(SettingsRetrieverOutputData output);

    /**
     * Outputs a failed response.
     *
     * @param message the message of the failed response
     */
    void prepareFailView(String message);
}
