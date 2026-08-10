package use_case.settings_updater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import entity.Settings;
import use_case.settings.SettingsDataAccessInterface;

public class SettingsUpdaterInteractorTest {
    @Test
    public void testCurate() {
        final FakeSettingsDataAccessObject repository = new FakeSettingsDataAccessObject();
        final FakeWardrobeAdderPresenter presenter = new FakeWardrobeAdderPresenter();
        final SettingsUpdaterInteractor interactor = new SettingsUpdaterInteractor(repository, presenter);

        final Settings update = new Settings();
        update.setIsHighContrast(false);
        update.setLocationCity("Toronto");
        update.setLocationCountryCode("CA");

        interactor.update(new SettingsUpdaterInputData(update));

        assertFalse(repository.isHighContrast());
        assertEquals("Toronto", repository.getLocationCityOrDefault());
        assertEquals("CA", repository.getLocationCountryCodeOrDefault());
    }

    private static final class FakeSettingsDataAccessObject implements SettingsDataAccessInterface {
        private boolean isHighContrast;
        private String locationCity;
        private String locationCountryCode;

        @Override
        public boolean isHighContrast() {
            return isHighContrast;
        }

        @Override
        public void setIsHighContrast(boolean isHighContrast) {
            this.isHighContrast = isHighContrast;
        }

        @Override
        public String getLocationCityOrDefault() {
            return locationCity;
        }

        @Override
        public void setLocationCity(String city) {
            locationCity = city;
        }

        @Override
        public String getLocationCountryCodeOrDefault() {
            return locationCountryCode;
        }

        @Override
        public void setLocationCountryCode(String countryCode) {
            locationCountryCode = countryCode;
        }
    }

    private static final class FakeWardrobeAdderPresenter implements SettingsUpdaterOutputBoundary {
        @Override
        public void prepareSuccessView() {
        }

        @Override
        public void prepareFailView(String message) {
        }
    }
}
