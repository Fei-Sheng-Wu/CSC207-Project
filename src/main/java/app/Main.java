package app;

import data_access.wardrobe.WardrobeReporterDataAccessObject;
import data_access.weather.WeatherRepository;
import data_access.weather.WeatherRepositoryImpl;
import entity.WearFactory;
import use_case.wardrobe_reporter.WardrobeReporterInteractor;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello!");
        // Testing for WeatherAPI.
        final WeatherRepository weatherRepository = new WeatherRepositoryImpl();
        weatherRepository.getCurrentByLocation("Toronto");
        weatherRepository.getForecastByLocation("Toronto");

        // Testing for Wardrobe use_case.
        final WearFactory factory = new WearFactory();
        final WardrobeReporterDataAccessObject dao = new WardrobeReporterDataAccessObject(factory);
        // Passed "null" for the Presenter since we commented it out. It is also not ready yet.
        final WardrobeReporterInteractor interactor = new WardrobeReporterInteractor(dao, null);
        interactor.report();
    }
}
