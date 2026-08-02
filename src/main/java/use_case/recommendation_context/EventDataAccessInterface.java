package use_case.recommendation_context;

import java.util.List;

import entity.Event;

/**
 * Defines the interface of an event repository that provides event data.
 */
public interface EventDataAccessInterface {
    /**
     * Returns a collection of events for the specified country.
     *
     * @param country the country as a 2-digit code (e.g., "CA")
     * @return a collection of events
     */
    List<Event> getEvents(String country);
}
