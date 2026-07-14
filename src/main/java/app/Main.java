package app;

import data_access.weather.WeatherRepository;
import data_access.weather.WeatherRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        System.out.println("Ello");
        WeatherRepository weatherRepository = new WeatherRepositoryImpl();
        weatherRepository.getCurrentByLocation("Toronto");
        weatherRepository.getForecastByLocation("Toronto");
    }
}
