package use_case.context_based_recommendation;

import java.util.List;

import entity.Event;

/**
 * Defines the interface of a holiday repository that provides holiday data.
 */
public interface HolidayDataAccessInterface {
    /**
     * Returns a list of holidays for the specified country and year.
     *
     * @param country the location as a country code (e.g., "CA")
     * @param year the target year (e.g., 2025)
     * @return a collection of holiday data
     */
    List<Event> getHolidays(String country, int year);
}
