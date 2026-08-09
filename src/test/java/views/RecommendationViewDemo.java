package views;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Event;
import entity.Footwear;
import entity.Headwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import entity.Weather;
import interface_adapter.recommendation.RecommendationPresenter;
import interface_adapter.recommendation.RecommendationViewModel;
import interface_adapter.recommendation_context.ContextBasedRecommendationController;
import use_case.recommendation_context.ContextBasedRecommendationInteractor;
import use_case.recommendation_context.EventDataAccessInterface;
import use_case.recommendation_context.WeatherDataAccessInterface;
import use_case.settings.SettingsDataAccessInterface;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Opens the recommendation screen on its own, backed by a wardrobe held in memory.
 *
 * <p>Run this class directly to look at the interface. Nothing is read from or written to
 * {@code ~/suitable}, and no API key is needed: the weather and event repositories are stubs, so
 * the whole screen is driven by the real interactor over invented data.
 *
 * <p>The seeded day is deliberately cold and wet on Canada Day, because that is the case where
 * the analyzers have the most to say. Change {@link #TEMPERATURE} or {@link #PRECIPITATION} to
 * watch the candidate filtering and the explanation change.
 */
public final class RecommendationViewDemo {
    private static final double TEMPERATURE = -3.0;
    private static final double PRECIPITATION = 1.5;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 520;

    private RecommendationViewDemo() {
    }

    /**
     * Opens the demo window.
     *
     * @param args the unused arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                show();
            }
        });
    }

    private static void show() {
        final RecommendationViewModel viewModel = new RecommendationViewModel();
        final ContextBasedRecommendationInteractor interactor = new ContextBasedRecommendationInteractor(
            new InMemoryWardrobe(seedWardrobe()),
            new FixedSettings(),
            new FixedEvents(),
            new FixedWeather(),
            new RecommendationPresenter(viewModel)
        );

        final ApplicationManager manager = new ApplicationManager(
            new HashMap<>(), new ArrayList<>(), new ArrayList<>());
        manager.register(RecommendationViewModel.class, viewModel);
        manager.register(
            ContextBasedRecommendationController.class,
            new ContextBasedRecommendationController(interactor, new Random()));

        final JFrame window = new JFrame("Suitable - Recommendation (demo data)");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setContentPane(new RecommendationView(manager));
        window.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * Builds a wardrobe with enough variety that the weather filter and the preferences both bite.
     *
     * <p>On the seeded cold, wet day only the thick outerwear, the long bottomwear, and the single
     * waterproof footwear survive the candidate filter, so the shorts, the denim jacket, and the
     * sneakers should never appear in a recommendation.
     */
    private static Wardrobe seedWardrobe() {
        final List<AbstractWear> items = new ArrayList<>();

        items.add(wear(new InnerTopwear(id(1)), "Red Oxford Shirt", WearColor.RED, WearStyle.CASUAL, 0.9));
        items.add(wear(new InnerTopwear(id(2)), "White Tee", WearColor.WHITE, WearStyle.CASUAL, 0.7));
        items.add(wear(new InnerTopwear(id(3)), "Navy Dress Shirt", WearColor.BLUE, WearStyle.FORMAL, 0.8));

        final OuterTopwear overcoat = wear(
            new OuterTopwear(id(4)), "Wool Overcoat", WearColor.BLACK, WearStyle.FORMAL, 0.85);
        overcoat.setIsThick(true);
        items.add(overcoat);
        final OuterTopwear parka = wear(
            new OuterTopwear(id(5)), "Red Parka", WearColor.RED, WearStyle.CASUAL, 0.95);
        parka.setIsThick(true);
        items.add(parka);
        items.add(wear(new OuterTopwear(id(6)), "Denim Jacket", WearColor.BLUE, WearStyle.CASUAL, 0.6));

        final Bottomwear jeans = wear(
            new Bottomwear(id(7)), "Dark Selvedge Jeans", WearColor.BLACK, WearStyle.CASUAL, 0.9);
        jeans.setIsLong(true);
        items.add(jeans);
        final Bottomwear trousers = wear(
            new Bottomwear(id(8)), "Grey Wool Trousers", WearColor.GREY, WearStyle.FORMAL, 0.7);
        trousers.setIsLong(true);
        items.add(trousers);
        items.add(wear(new Bottomwear(id(9)), "Chino Shorts", WearColor.BROWN, WearStyle.CASUAL, 0.5));

        final Footwear boots = wear(
            new Footwear(id(10)), "Waterproof Chelsea Boots", WearColor.BLACK, WearStyle.CASUAL, 0.9);
        boots.setIsWaterproof(true);
        items.add(boots);
        items.add(wear(new Footwear(id(11)), "White Sneakers", WearColor.WHITE, WearStyle.SPORTY, 0.8));

        items.add(wear(new Headwear(id(12)), "Grey Wool Beanie", WearColor.GREY, WearStyle.CASUAL, 0.6));
        items.add(wear(new Accessory(id(13)), "Cream Scarf", WearColor.WHITE, WearStyle.CASUAL, 0.8));

        return new Wardrobe(items);
    }

    private static UUID id(long value) {
        return new UUID(0L, value);
    }

    private static <T extends AbstractWear> T wear(T item,
                                                   String name,
                                                   WearColor color,
                                                   WearStyle style,
                                                   double fondness) {
        item.setName(name);
        item.setColor(color);
        item.setStyle(style);
        item.setCondition(WearCondition.NEW);
        item.setFondness(fondness);

        return item;
    }

    /** Holds the seeded wardrobe in memory instead of reading {@code ~/suitable/wardrobe.json}. */
    private static final class InMemoryWardrobe implements WardrobeDataAccessInterface {
        private Wardrobe wardrobe;

        private InMemoryWardrobe(Wardrobe wardrobe) {
            this.wardrobe = wardrobe;
        }

        @Override
        public Wardrobe fetchWardrobe() {
            return wardrobe;
        }

        @Override
        public void saveWardrobe(Wardrobe updated) {
            wardrobe = updated;
        }
    }

    /** Stands in for the settings file. */
    private static final class FixedSettings implements SettingsDataAccessInterface {
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

    /** Stands in for the event API, so no key is required. */
    private static final class FixedEvents implements EventDataAccessInterface {
        @Override
        public List<Event> getEvents(String country, LocalDate date) {
            return List.of(new Event(
                "Canada Day",
                OffsetDateTime.parse("2026-07-01T00:00:00-04:00"),
                OffsetDateTime.parse("2026-07-01T23:59:59-04:00"),
                List.of(WearColor.RED, WearColor.WHITE),
                List.of(WearStyle.CASUAL)
            ));
        }
    }

    /** Stands in for the weather API, so no key is required. */
    private static final class FixedWeather implements WeatherDataAccessInterface {
        @Override
        public Weather getCurrentByLocation(String location) {
            return new Weather(
                LocalDate.now(), "Cold rain", TEMPERATURE, PRECIPITATION, 18.0, 82.0, 1);
        }

        @Override
        public List<Weather> getForecastByLocation(String location) {
            return List.of(getCurrentByLocation(location));
        }
    }
}
