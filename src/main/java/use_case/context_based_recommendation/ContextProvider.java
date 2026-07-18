package use_case.context_based_recommendation;

import java.util.List;

import entity.Event;
import entity.Weather;

/**
 * Supplies environmental context required by the recommendation use case.
 */
public interface ContextProvider {
    /**
     * Returns the current weather.
     *
     * @return the current weather
     */
    Weather getCurrentWeather();

    /**
     * Returns the events that are currently relevant to the user.
     *
     * @return the current events
     */
    List<Event> getCurrentEvents();
}
