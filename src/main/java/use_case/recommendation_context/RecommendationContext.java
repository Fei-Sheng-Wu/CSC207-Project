package use_case.recommendation_context;

import java.util.List;

import entity.Event;
import entity.WearColor;
import entity.WearStyle;
import entity.Weather;

/**
 * Immutable context evaluated by recommendation analyzers.
 */
public final class RecommendationContext {
    private final Weather weather;
    private final List<Event> events;
    private final List<WearColor> preferredColors;
    private final List<WearStyle> preferredStyles;

    public RecommendationContext(Weather weather,
                                 List<Event> events,
                                 List<WearColor> preferredColors,
                                 List<WearStyle> preferredStyles) {
        this.weather = weather;
        this.events = List.copyOf(events);
        this.preferredColors = List.copyOf(preferredColors);
        this.preferredStyles = List.copyOf(preferredStyles);
    }

    public Weather getWeather() {
        return weather;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<WearColor> getPreferredColors() {
        return preferredColors;
    }

    public List<WearStyle> getPreferredStyles() {
        return preferredStyles;
    }
}
