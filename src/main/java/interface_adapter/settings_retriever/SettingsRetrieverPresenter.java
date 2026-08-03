package interface_adapter.settings_retriever;

import interface_adapter.settings.SettingsViewModel;
import use_case.settings_retriever.SettingsRetrieverOutputBoundary;
import use_case.settings_retriever.SettingsRetrieverOutputData;

/**
 * Presenter for the settings retriever use case.
 */
public class SettingsRetrieverPresenter implements SettingsRetrieverOutputBoundary {
    private final SettingsViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public SettingsRetrieverPresenter(SettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SettingsRetrieverOutputData output) {
        viewModel.setLocationCity(output.getLocationCity());
        viewModel.setLocationCountryCode(output.getLocationCountryCode());
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setLocationCity(null);
        viewModel.setLocationCountryCode(null);
    }
}
