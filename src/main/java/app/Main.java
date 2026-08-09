package app;

import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import database.event.HttpEventDataAccessObject;
import database.inspiration.HttpInspirationDataAccessObject;
import database.settings.LocalSettingsDataAccessObject;
import database.wardrobe.JsonWardrobeDataAccessObject;
import database.weather.HttpWeatherDataAccessObject;
import interface_adapter.inspiration.InspirationViewModel;
import interface_adapter.inspiration_curator.InspirationCuratorController;
import interface_adapter.inspiration_curator.InspirationCuratorPresenter;
import interface_adapter.item.ItemViewModel;
import interface_adapter.recommendation.RecommendationPresenter;
import interface_adapter.recommendation.RecommendationViewModel;
import interface_adapter.recommendation_context.ContextBasedRecommendationController;
import interface_adapter.recommendation_tag.TagBasedRecommendationController;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.settings_retriever.SettingsRetrieverController;
import interface_adapter.settings_retriever.SettingsRetrieverPresenter;
import interface_adapter.settings_updater.SettingsUpdaterController;
import interface_adapter.settings_updater.SettingsUpdaterPresenter;
import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_adder.WardrobeAdderController;
import interface_adapter.wardrobe_adder.WardrobeAdderPresenter;
import interface_adapter.wardrobe_analyzer.WardrobeAnalyzerController;
import interface_adapter.wardrobe_analyzer.WardrobeAnalyzerPresenter;
import interface_adapter.wardrobe_filterer.WardrobeFiltererController;
import interface_adapter.wardrobe_filterer.WardrobeFiltererPresenter;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_remover.WardrobeRemoverPresenter;
import interface_adapter.wardrobe_retriever.WardrobeRetrieverController;
import interface_adapter.wardrobe_retriever.WardrobeRetrieverPresenter;
import interface_adapter.wardrobe_sorter.WardrobeSorterController;
import interface_adapter.wardrobe_sorter.WardrobeSorterPresenter;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterPresenter;
import use_case.inspiration_curator.InspirationCuratorInputBoundary;
import use_case.inspiration_curator.InspirationCuratorInteractor;
import use_case.inspiration_curator.InspirationCuratorOutputBoundary;
import use_case.inspiration_curator.InspirationDataAccessInterface;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation_context.ContextBasedRecommendationInputBoundary;
import use_case.recommendation_context.ContextBasedRecommendationInteractor;
import use_case.recommendation_context.EventDataAccessInterface;
import use_case.recommendation_context.WeatherDataAccessInterface;
import use_case.recommendation_tag.TagBasedRecommendationInputBoundary;
import use_case.recommendation_tag.TagBasedRecommendationInteractor;
import use_case.settings.SettingsDataAccessInterface;
import use_case.settings_retriever.SettingsRetrieverInputBoundary;
import use_case.settings_retriever.SettingsRetrieverInteractor;
import use_case.settings_retriever.SettingsRetrieverOutputBoundary;
import use_case.settings_updater.SettingsUpdaterInputBoundary;
import use_case.settings_updater.SettingsUpdaterInteractor;
import use_case.settings_updater.SettingsUpdaterOutputBoundary;
import use_case.wardrobe.WardrobeDataAccessInterface;
import use_case.wardrobe_adder.WardrobeAdderInputBoundary;
import use_case.wardrobe_adder.WardrobeAdderInteractor;
import use_case.wardrobe_adder.WardrobeAdderOutputBoundary;
import use_case.wardrobe_analyzer.WardrobeAnalyzerInputBoundary;
import use_case.wardrobe_analyzer.WardrobeAnalyzerInteractor;
import use_case.wardrobe_analyzer.WardrobeAnalyzerOutputBoundary;
import use_case.wardrobe_filterer.WardrobeFiltererInputBoundary;
import use_case.wardrobe_filterer.WardrobeFiltererInteractor;
import use_case.wardrobe_filterer.WardrobeFiltererOutputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInteractor;
import use_case.wardrobe_remover.WardrobeRemoverOutputBoundary;
import use_case.wardrobe_retriever.WardrobeRetrieverInputBoundary;
import use_case.wardrobe_retriever.WardrobeRetrieverInteractor;
import use_case.wardrobe_retriever.WardrobeRetrieverOutputBoundary;
import use_case.wardrobe_sorter.WardrobeSorterInputBoundary;
import use_case.wardrobe_sorter.WardrobeSorterInteractor;
import use_case.wardrobe_sorter.WardrobeSorterOutputBoundary;
import use_case.wardrobe_updater.WardrobeUpdaterInputBoundary;
import use_case.wardrobe_updater.WardrobeUpdaterInteractor;
import use_case.wardrobe_updater.WardrobeUpdaterOutputBoundary;
import views.InspirationView;
import views.ItemView;
import views.RecommendationView;
import views.SettingsView;
import views.WardrobeDetailsView;
import views.WardrobeOverviewView;

public class Main {
    /**
     * Run the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        // Swing components may only be built and shown on the event dispatch thread.
        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        final ApplicationBuilder builder = new ApplicationBuilder()
            .registerSimple(Random.class);

        // Register the data access objects.
        registerDataAccess(builder);
        // Register the view models.
        registerViewModels(builder);
        // Register the output boundaries.
        registerOutputBoundaries(builder);
        // Register the input boundaries.
        registerInputBoundaries(builder);
        // Register the controllers.
        registerControllers(builder);

        final JFrame application = builder
            // Register the views.
            .registerView(WardrobeOverviewView.class)
            .registerView(WardrobeDetailsView.class)
            .registerView(ItemView.class)
            .registerView(InspirationView.class)
            .registerView(RecommendationView.class)
            .registerView(SettingsView.class)
            // Configure the master layout.
            .setTopNavigations(List.of(WardrobeOverviewView.class, RecommendationView.class))
            .setBottomNavigations(List.of(SettingsView.class))
            .setInitialView(WardrobeOverviewView.class)
            // Build the application.
            .build();

        application.setVisible(true);
    }

    private static void registerDataAccess(ApplicationBuilder builder) {
        builder
            .registerImplementation(
                WardrobeDataAccessInterface.class, JsonWardrobeDataAccessObject.class,
                "wardrobe.json"
            )
            .registerImplementation(WeatherDataAccessInterface.class, HttpWeatherDataAccessObject.class)
            .registerImplementation(EventDataAccessInterface.class, HttpEventDataAccessObject.class)
            .registerImplementation(InspirationDataAccessInterface.class, HttpInspirationDataAccessObject.class)
            .registerImplementation(
                SettingsDataAccessInterface.class, LocalSettingsDataAccessObject.class,
                "user.properties"
            );
    }

    private static void registerViewModels(ApplicationBuilder builder) {
        builder
            .registerSimple(WardrobeViewModel.class)
            .registerSimple(ItemViewModel.class)
            .registerSimple(InspirationViewModel.class)
            .registerSimple(RecommendationViewModel.class)
            .registerSimple(SettingsViewModel.class);
    }

    private static void registerOutputBoundaries(ApplicationBuilder builder) {
        builder
            .registerImplementation(
                WardrobeRetrieverOutputBoundary.class, WardrobeRetrieverPresenter.class,
                WardrobeViewModel.class
            )
            .registerImplementation(
                WardrobeFiltererOutputBoundary.class, WardrobeFiltererPresenter.class,
                WardrobeViewModel.class
            )
            .registerImplementation(
                WardrobeSorterOutputBoundary.class, WardrobeSorterPresenter.class,
                WardrobeViewModel.class
            )
            .registerImplementation(
                WardrobeAnalyzerOutputBoundary.class, WardrobeAnalyzerPresenter.class,
                WardrobeViewModel.class
            )
            .registerImplementation(
                WardrobeAdderOutputBoundary.class, WardrobeAdderPresenter.class,
                ItemViewModel.class
            )
            .registerImplementation(
                WardrobeUpdaterOutputBoundary.class, WardrobeUpdaterPresenter.class,
                ItemViewModel.class
            )
            .registerImplementation(
                WardrobeRemoverOutputBoundary.class, WardrobeRemoverPresenter.class,
                ItemViewModel.class
            )
            .registerImplementation(
                InspirationCuratorOutputBoundary.class, InspirationCuratorPresenter.class,
                InspirationViewModel.class
            )
            .registerImplementation(
                RecommendationOutputBoundary.class, RecommendationPresenter.class,
                RecommendationViewModel.class
            )
            .registerImplementation(
                SettingsRetrieverOutputBoundary.class, SettingsRetrieverPresenter.class,
                SettingsViewModel.class
            )
            .registerImplementation(
                SettingsUpdaterOutputBoundary.class, SettingsUpdaterPresenter.class,
                SettingsViewModel.class
            );
    }

    private static void registerInputBoundaries(ApplicationBuilder builder) {
        builder
            .registerImplementation(
                WardrobeRetrieverInputBoundary.class, WardrobeRetrieverInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeRetrieverOutputBoundary.class
            )
            .registerImplementation(
                WardrobeFiltererInputBoundary.class, WardrobeFiltererInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeFiltererOutputBoundary.class
            )
            .registerImplementation(
                WardrobeSorterInputBoundary.class, WardrobeSorterInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeSorterOutputBoundary.class
            )
            .registerImplementation(
                WardrobeAnalyzerInputBoundary.class, WardrobeAnalyzerInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeAnalyzerOutputBoundary.class
            )
            .registerImplementation(
                WardrobeAdderInputBoundary.class, WardrobeAdderInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeAdderOutputBoundary.class
            )
            .registerImplementation(
                WardrobeUpdaterInputBoundary.class, WardrobeUpdaterInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeUpdaterOutputBoundary.class
            )
            .registerImplementation(
                WardrobeRemoverInputBoundary.class, WardrobeRemoverInteractor.class,
                WardrobeDataAccessInterface.class,
                WardrobeRemoverOutputBoundary.class
            )
            .registerImplementation(
                InspirationCuratorInputBoundary.class, InspirationCuratorInteractor.class,
                InspirationDataAccessInterface.class,
                InspirationCuratorOutputBoundary.class
            )
            .registerImplementation(
                ContextBasedRecommendationInputBoundary.class, ContextBasedRecommendationInteractor.class,
                WardrobeDataAccessInterface.class,
                SettingsDataAccessInterface.class,
                EventDataAccessInterface.class,
                WeatherDataAccessInterface.class,
                RecommendationOutputBoundary.class
            )
            .registerImplementation(
                TagBasedRecommendationInputBoundary.class, TagBasedRecommendationInteractor.class,
                WardrobeDataAccessInterface.class,
                RecommendationOutputBoundary.class
            )
            .registerImplementation(
                SettingsRetrieverInputBoundary.class, SettingsRetrieverInteractor.class,
                SettingsDataAccessInterface.class,
                SettingsRetrieverOutputBoundary.class
            )
            .registerImplementation(
                SettingsUpdaterInputBoundary.class, SettingsUpdaterInteractor.class,
                SettingsDataAccessInterface.class,
                SettingsUpdaterOutputBoundary.class
            );
    }

    private static void registerControllers(ApplicationBuilder builder) {
        builder
            .registerSimple(WardrobeRetrieverController.class, WardrobeRetrieverInputBoundary.class)
            .registerSimple(WardrobeFiltererController.class, WardrobeFiltererInputBoundary.class)
            .registerSimple(WardrobeSorterController.class, WardrobeSorterInputBoundary.class)
            .registerSimple(WardrobeAnalyzerController.class, WardrobeAnalyzerInputBoundary.class)
            .registerSimple(WardrobeAdderController.class, WardrobeAdderInputBoundary.class)
            .registerSimple(WardrobeUpdaterController.class, WardrobeUpdaterInputBoundary.class)
            .registerSimple(WardrobeRemoverController.class, WardrobeRemoverInputBoundary.class)
            .registerSimple(InspirationCuratorController.class, InspirationCuratorInputBoundary.class)
            .registerSimple(
                ContextBasedRecommendationController.class,
                ContextBasedRecommendationInputBoundary.class,
                Random.class
            )
            .registerSimple(
                TagBasedRecommendationController.class,
                TagBasedRecommendationInputBoundary.class,
                Random.class
            )
            .registerSimple(SettingsRetrieverController.class, SettingsRetrieverInputBoundary.class)
            .registerSimple(SettingsUpdaterController.class, SettingsUpdaterInputBoundary.class);
    }
}
