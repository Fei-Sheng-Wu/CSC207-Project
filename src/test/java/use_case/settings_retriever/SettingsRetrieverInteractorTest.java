package use_case.settings_retriever;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import entity.Settings;
import use_case.settings.SettingsDataAccessInterface;

public class SettingsRetrieverInteractorTest {
    @Test
    public void testCurate() {
        final FakeSettingsDataAccessObject repository = new FakeSettingsDataAccessObject();
        final FakeWardrobeAdderPresenter presenter = new FakeWardrobeAdderPresenter();
        final SettingsRetrieverInteractor interactor = new SettingsRetrieverInteractor(repository, presenter);

        interactor.retrieve();

        final Settings output = presenter.getOutput();
        assertFalse(output.isHighContrast());
        assertEquals("Toronto", output.getLocationCity());
        assertEquals("CA", output.getLocationCountryCode());
    }

    private static final class FakeSettingsDataAccessObject implements SettingsDataAccessInterface {
        @Override
        public boolean isHighContrast() {
            return false;
        }

        @Override
        public void setIsHighContrast(boolean isHighContrast) {
        }

        @Override
        public String getLocationCityOrDefault() {
            return "Toronto";
        }

        @Override
        public void setLocationCity(String city) {
        }

        @Override
        public String getLocationCountryCodeOrDefault() {
            return "CA";
        }

        @Override
        public void setLocationCountryCode(String countryCode) {
        }
    }

    private static final class FakeWardrobeAdderPresenter implements SettingsRetrieverOutputBoundary {
        private Settings output;

        @Override
        public void prepareSuccessView(Settings settings) {
            this.output = settings;
        }

        @Override
        public void prepareFailView(String message) {

        }

        public Settings getOutput() {
            return output;
        }
    }
}
