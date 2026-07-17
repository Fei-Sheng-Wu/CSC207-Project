package app;

import data_access.wardrobe.WardrobeReporterDataAccessObject;
import data_access.weather.WeatherRepository;
import data_access.weather.WeatherRepositoryImpl;
import entity.*;
import use_case.wardrobe_reporter.WardrobeReporterInteractor;

import java.time.LocalDate;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        // --- TESTING FOR WeatherAPI ---
//        final WeatherRepository weatherRepository = new WeatherRepositoryImpl();
//        weatherRepository.getCurrentByLocation("Toronto");
//        weatherRepository.getForecastByLocation("Toronto");


         final WearFactory factory = new WearFactory();
         final WardrobeReporterDataAccessObject dao = new WardrobeReporterDataAccessObject(factory);

         // --- TESTING FOR fetchWardrobe() & Wardrobe Reporter USE_CASE ---
        // Passed "null" for the Presenter since we commented it out. It is also not ready yet.
//         final WardrobeReporterInteractor interactor = new WardrobeReporterInteractor(dao, null);
//         interactor.report();

        Wardrobe wardrobe = dao.fetchWardrobe();
        int initialSize = wardrobe.getItems().size();
        System.out.println("Initial Wardrobe Size: " + initialSize);

        // --- TESTING FOR saveWardrobe() ---
        // Check wardrobe.json. Now it has two dummy examples, but we will add one more.
        AbstractWear test = factory.constructWear("OuterTopwear", UUID.randomUUID());

        test.setName("Jacket");
        test.setBrand("Colubmia");
        test.setColor(WearColor.BLACK);
        test.setStyle(WearStyle.CASUAL);
        test.setCondition(WearCondition.NEW);
        test.setPurchaseDate(LocalDate.now());
        test.setFondness(9.5);
        test.getTags().add("Winter");
        test.getTags().add("Occasion");
        test.getTags().add("Nice");

        wardrobe.getItems().add(test);

        dao.saveWardrobe(wardrobe);

        System.out.println("Verify. The size of the wardrobe has increased and new items has been added to json.wardrobe");
        Wardrobe reloadedWardrobe = dao.fetchWardrobe();
        int newSize = reloadedWardrobe.getItems().size();

        System.out.println("Final Wardrobe Size: " + newSize);
    }
}
