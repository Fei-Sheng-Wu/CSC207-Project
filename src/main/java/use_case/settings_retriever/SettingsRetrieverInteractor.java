package use_case.settings_retriever;

import entity.Settings;
import use_case.settings.SettingsDataAccessInterface;

/**
 * Use case interactor for retrieving settings.
 */
public class SettingsRetrieverInteractor implements SettingsRetrieverInputBoundary {
    private final SettingsDataAccessInterface repository;
    private final SettingsRetrieverOutputBoundary outputBoundary;

    /**
     * Constructs a new interactor.
     *
     * @param repository     the data access object of the interactor
     * @param outputBoundary the output boundary of the interactor
     */
    public SettingsRetrieverInteractor(
        SettingsDataAccessInterface repository,
        SettingsRetrieverOutputBoundary outputBoundary
    ) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void retrieve() {
        final Settings settings = new Settings();
        settings.setLocationCity(repository.getLocationCityOrDefault());
        settings.setLocationCountryCode(repository.getLocationCountryCodeOrDefault());

        outputBoundary.prepareSuccessView(settings);
    }
}
