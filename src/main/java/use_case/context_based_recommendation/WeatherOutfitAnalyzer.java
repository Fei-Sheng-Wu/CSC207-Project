package use_case.context_based_recommendation;

import java.util.ArrayList;
import java.util.List;

import entity.Outfit;
import entity.Weather;

/**
 * Applies hard weather-suitability rules to outfit candidates.
 */
public final class WeatherOutfitAnalyzer implements OutfitAnalyzer {
    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        final Weather weather = context.getWeather();
        final List<String> reasons = new ArrayList<>();

        if (!outfit.isWeatherAppropriate(weather)) {
            return rejected("The outfit does not provide suitable coverage for the temperature.");
        }
        if (weather.getPrecipitation() > 0.0 && !outfit.getFootwear().isWaterproof()) {
            return rejected("Waterproof footwear is required when precipitation is present.");
        }

        addTemperatureReason(weather.getTemperature(), reasons);
        if (weather.getPrecipitation() > 0.0) {
            reasons.add("Waterproof footwear was selected for the precipitation.");
        }
        return new OutfitAnalysis(true, 0, 0, 0.0, reasons);
    }

    private static OutfitAnalysis rejected(String reason) {
        return new OutfitAnalysis(false, 0, 0, 0.0, List.of(reason));
    }

    private static void addTemperatureReason(double temperature, List<String> reasons) {
        if (temperature < 0.0) {
            reasons.add("Thick outerwear and long bottomwear were selected for the cold temperature.");
        }
        else if (temperature < 5.0) {
            reasons.add("Outerwear and long bottomwear were selected for the cool temperature.");
        }
        else if (temperature < 10.0) {
            reasons.add("Outerwear was selected for the cool temperature.");
        }
        else {
            reasons.add("The outfit provides suitable coverage for the current temperature.");
        }
    }
}
