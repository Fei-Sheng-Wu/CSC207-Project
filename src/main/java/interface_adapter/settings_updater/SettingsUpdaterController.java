package interface_adapter.settings_updater;

import entity.Settings;
import use_case.settings_updater.SettingsUpdaterInputBoundary;
import use_case.settings_updater.SettingsUpdaterInputData;

/**
 * Controller for updating settings.
 */
public class SettingsUpdaterController {
    private final SettingsUpdaterInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public SettingsUpdaterController(SettingsUpdaterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the update settings use case.
     *
     * @param isHighContrast      whether high contrast is preferred
     * @param locationCity        the city of the location
     * @param locationCountryCode the 2-digit country code of the location
     */
    public void update(boolean isHighContrast, String locationCity, String locationCountryCode) {
        final Settings settings = new Settings();
        settings.setIsHighContrast(isHighContrast);
        settings.setLocationCity(locationCity);
        settings.setLocationCountryCode(locationCountryCode);

        interactor.update(new SettingsUpdaterInputData(settings));
    }
}
