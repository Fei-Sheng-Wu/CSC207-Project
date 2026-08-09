package use_case.settings_updater;

import use_case.settings.SettingsDataAccessInterface;

/**
 * Use case interactor for updating settings.
 */
public class SettingsUpdaterInteractor implements SettingsUpdaterInputBoundary {
    private final SettingsDataAccessInterface repository;
    private final SettingsUpdaterOutputBoundary outputBoundary;

    /**
     * Constructs a new interactor.
     *
     * @param repository     the data access object of the interactor
     * @param outputBoundary the output boundary of the interactor
     */
    public SettingsUpdaterInteractor(
        SettingsDataAccessInterface repository,
        SettingsUpdaterOutputBoundary outputBoundary
    ) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void update(SettingsUpdaterInputData request) {
        repository.setIsHighContrast(request.getSettings().isHighContrast());
        repository.setLocationCity(request.getSettings().getLocationCity());
        repository.setLocationCountryCode(request.getSettings().getLocationCountryCode());
        outputBoundary.prepareSuccessView();
    }
}
