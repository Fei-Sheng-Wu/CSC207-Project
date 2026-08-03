package interface_adapter.settings_updater;

import interface_adapter.settings.SettingsViewModel;
import use_case.settings_updater.SettingsUpdaterOutputBoundary;

/**
 * Presenter for the settings updater use case.
 */
public class SettingsUpdaterPresenter implements SettingsUpdaterOutputBoundary {
    private final SettingsViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public SettingsUpdaterPresenter(SettingsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView() {
        // Do nothing.
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setLocationCity(null);
        viewModel.setLocationCountryCode(null);
    }
}
