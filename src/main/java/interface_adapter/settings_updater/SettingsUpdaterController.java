package interface_adapter.settings_updater;

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
     * @param locationCity        the city of the location
     * @param locationCountryCode the 2-digit country code of the location
     */
    public void update(String locationCity, String locationCountryCode) {
        interactor.update(new SettingsUpdaterInputData(locationCity, locationCountryCode));
    }
}
