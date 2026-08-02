package data_access.holiday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import data_access.event.HttpEventDataAccessObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import entity.Event;

/**
 * Tests for the holiday API data access object.
 *
 * <p>Disabled for two independent reasons, both of which must be resolved before re-enabling:
 *
 * <ul>
 *   <li>{@code HttpHolidayDataAccessObject.getHolidays} currently returns an empty list. Its
 *       {@code result.add(...)} call is commented out pending a decision on how a holiday's
 *       {@code primary_type} maps onto the {@code Event} entity's wear colors and styles.</li>
 *   <li>The test performs a live API call and requires the {@code API_BASE_URL_HOLIDAY} and
 *       {@code API_KEY_HOLIDAY} environment variables to be set.</li>
 * </ul>
 *
 * <p>The assertions below describe the intended behaviour once the mapping is implemented, so
 * this class doubles as the specification for that work.
 */
@Disabled("Holiday DAO returns an empty list (mapping unimplemented) and this test needs live API credentials")
public class HttpHolidayDataAccessObjectTest {
    private static List<Event> holidays;

    @BeforeAll
    public static void holidayApiSetUp() {
        final HttpEventDataAccessObject httpHoliday = new HttpEventDataAccessObject();
        holidays = httpHoliday.getEvents("CA", 2025);
    }

    @Test
    public void testGetHolidaysReturnsValidList() {
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());

        final Event firstHoliday = holidays.get(0);
        assertNotNull(firstHoliday.getDateStart());
        assertNotNull(firstHoliday.getDateEnd());
        assertNotNull(firstHoliday.getName());
        assertNotNull(firstHoliday.getWearColors());
        assertNotNull(firstHoliday.getWearStyles());
    }

    @Test
    public void testGetHolidaysReturnsValidListElement() {
        for (Event holiday : holidays) {
            if ("New Year's Day".equals(holiday.getName())) {
                assertEquals(LocalDate.of(2025, 1, 1), holiday.getDateStart().toLocalDate());
            }

            if ("Canada Day".equals(holiday.getName())) {
                assertEquals(LocalDate.of(2025, 7, 1), holiday.getDateStart().toLocalDate());
            }
        }
    }
}
