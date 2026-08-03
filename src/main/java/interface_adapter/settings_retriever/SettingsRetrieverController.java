package interface_adapter.settings_retriever;

import use_case.settings_retriever.SettingsRetrieverInputBoundary;

/**
 * Controller for retrieving settings.
 */
public class SettingsRetrieverController {
    private final SettingsRetrieverInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public SettingsRetrieverController(SettingsRetrieverInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the retrieve settings use case.
     */
    public void retrieve() {
        interactor.retrieve();
    }
}
