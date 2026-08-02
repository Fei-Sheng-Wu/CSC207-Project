package use_case.recommendation_context;

import java.util.ArrayList;
import java.util.List;

import entity.Outfit;
import entity.Weather;
import entity.WeatherSuitability;

/**
 * Applies hard weather-suitability rules to outfit candidates.
 *
 * <p>The thresholds themselves live in {@link WeatherSuitability}, so this analyzer decides only
 * how to explain a decision, never what the rule is.
 */
public final class WeatherOutfitAnalyzer implements OutfitAnalyzer {
    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        final Weather weather = context.getWeather();
        final List<String> reasons = new ArrayList<>();

        if (!outfit.isTemperatureAppropriate(weather)) {
            return rejected("The outfit does not provide suitable coverage for the temperature.");
        }
        if (!outfit.isPrecipitationAppropriate(weather)) {
            return rejected("Waterproof footwear is required when precipitation is present.");
        }

        addTemperatureReason(weather.getTemperature(), reasons);
        if (WeatherSuitability.requiresWaterproofFootwear(weather.getPrecipitation())) {
            reasons.add("Waterproof footwear was selected for the precipitation.");
        }
        return new OutfitAnalysis(true, 0, 0, 0.0, reasons);
    }

    private static OutfitAnalysis rejected(String reason) {
        return new OutfitAnalysis(false, 0, 0, 0.0, List.of(reason));
    }

    private static void addTemperatureReason(double temperature, List<String> reasons) {
        if (WeatherSuitability.requiresThickOuterTopwear(temperature)) {
            reasons.add("Thick outerwear and long bottomwear were selected for the cold temperature.");
        }
        else if (WeatherSuitability.requiresLongBottomwear(temperature)) {
            reasons.add("Outerwear and long bottomwear were selected for the cool temperature.");
        }
        else if (WeatherSuitability.requiresOuterTopwear(temperature)) {
            reasons.add("Outerwear was selected for the cool temperature.");
        }
        else {
            reasons.add("The outfit provides suitable coverage for the current temperature.");
        }
    }
}
