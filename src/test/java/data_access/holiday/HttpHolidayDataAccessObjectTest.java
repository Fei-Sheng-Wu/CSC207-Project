package data_access.holiday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import entity.Event;

public class HttpHolidayDataAccessObjectTest {
    private static List<Event> holidays;

    @BeforeAll
    public static void HolidayApiSetUp() {
        final HttpHolidayDataAccessObject httpHoliday = new HttpHolidayDataAccessObject();
        holidays = httpHoliday.getHolidays("CA", 2025);
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
            if (holiday.getName().equals("New Year's Day")) {
                assertEquals(LocalDate.of(2025, 1, 1), holiday.getDateStart().toLocalDate());
            }

            if (holiday.getName().equals("Canada Day")) {
                assertEquals(LocalDate.of(2025, 7, 1), holiday.getDateStart().toLocalDate());
            }
        }
    }
}
