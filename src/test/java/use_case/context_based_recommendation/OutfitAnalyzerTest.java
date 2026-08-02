package use_case.context_based_recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.AbstractWear;
import entity.Bottomwear;
import entity.Event;
import entity.Footwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Outfit;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import entity.Weather;
import use_case.recommendation_context.EventOutfitAnalyzer;
import use_case.recommendation_context.OutfitAnalysis;
import use_case.recommendation_context.PreferenceOutfitAnalyzer;
import use_case.recommendation_context.RecommendationContext;
import use_case.recommendation_context.WeatherOutfitAnalyzer;

class OutfitAnalyzerTest {
    @Test
    void weatherAnalyzerRejectsNonWaterproofFootwearInRain() {
        final Outfit outfit = basicOutfit();
        final Weather rainyWeather = new Weather(
                LocalDate.of(2026, 7, 1), "Rain", 20.0, 1.0, 0.0, 0.0, 0);
        final RecommendationContext context = new RecommendationContext(
                rainyWeather, List.of(), List.of(), List.of());

        final OutfitAnalysis analysis = new WeatherOutfitAnalyzer().analyze(outfit, context);

        assertFalse(analysis.isAcceptable());
    }

    @Test
    void weatherAnalyzerAcceptsColdWeatherLayersAndWaterproofFootwear() {
        final InnerTopwear shirt = wear(new InnerTopwear(UUID.randomUUID()), WearColor.RED, WearStyle.CASUAL);
        final OuterTopwear coat = wear(new OuterTopwear(UUID.randomUUID()), WearColor.WHITE, WearStyle.CASUAL);
        coat.setIsThick(true);
        final Bottomwear bottom = wear(new Bottomwear(UUID.randomUUID()), WearColor.BLACK, WearStyle.CASUAL);
        bottom.setIsLong(true);
        final Footwear footwear = wear(new Footwear(UUID.randomUUID()), WearColor.BLACK, WearStyle.CASUAL);
        footwear.setIsWaterproof(true);
        final Outfit outfit = new Outfit(shirt, coat, bottom, footwear, null, List.of());
        final Weather weather = new Weather(
                LocalDate.of(2026, 7, 1), "Snow", -5.0, 1.0, 0.0, 0.0, 0);

        final OutfitAnalysis analysis = new WeatherOutfitAnalyzer().analyze(
                outfit,
                new RecommendationContext(weather, List.of(), List.of(), List.of())
        );

        assertTrue(analysis.isAcceptable());
        assertEquals(2, analysis.getReasons().size());
    }

    @Test
    void eventAndPreferenceAnalyzersCountMatchingAttributes() {
        final Outfit outfit = basicOutfit();
        outfit.getTopwearInner().setColor(WearColor.RED);
        outfit.getTopwearInner().setStyle(WearStyle.CASUAL);
        final Event event = new Event(
                "Canada Day",
                OffsetDateTime.parse("2026-07-01T00:00:00-04:00"),
                OffsetDateTime.parse("2026-07-01T23:59:59-04:00"),
                List.of(WearColor.RED),
                List.of(WearStyle.CASUAL)
        );
        final RecommendationContext context = new RecommendationContext(
                new Weather(LocalDate.of(2026, 7, 1), "Clear", 20.0, 0.0, 0.0, 0.0, 0),
                List.of(event),
                List.of(WearColor.RED),
                List.of(WearStyle.CASUAL)
        );

        final OutfitAnalysis eventAnalysis = new EventOutfitAnalyzer().analyze(outfit, context);
        final OutfitAnalysis preferenceAnalysis = new PreferenceOutfitAnalyzer().analyze(outfit, context);

        assertEquals(2, eventAnalysis.getEventMatches());
        assertEquals(2, preferenceAnalysis.getPreferenceMatches());
    }

    private static Outfit basicOutfit() {
        final InnerTopwear shirt = wear(new InnerTopwear(UUID.randomUUID()), WearColor.BLUE, WearStyle.FORMAL);
        final Bottomwear bottom = wear(new Bottomwear(UUID.randomUUID()), WearColor.BLACK, WearStyle.FORMAL);
        final Footwear footwear = wear(new Footwear(UUID.randomUUID()), WearColor.BLACK, WearStyle.FORMAL);
        return new Outfit(shirt, null, bottom, footwear, null, List.of());
    }

    private static <T extends AbstractWear> T wear(T item, WearColor color, WearStyle style) {
        item.setColor(color);
        item.setStyle(style);
        item.setCondition(WearCondition.NEW);
        return item;
    }
}
