package app;

import data_access.weather.WeatherRepository;
import data_access.weather.WeatherRepositoryImpl;

public class main {
    public static void main(String[] args) {
        System.out.println("Ello");
        WeatherRepository weatherRepository = new WeatherRepositoryImpl();
        weatherRepository.getByLocation("Toronto");
        weatherRepository.getForecastByLocation("Toronto");
    }
}
