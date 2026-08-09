package database.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import entity.Event;

@EnabledIfEnvironmentVariable(named = "API_BASE_URL_HOLIDAY", matches = ".+")
@EnabledIfEnvironmentVariable(named = "API_KEY_HOLIDAY", matches = ".+")
public class HttpEventDataAccessObjectTest {
    private HttpEventDataAccessObject dataAccess;

    @BeforeEach
    public void setUp() {
        dataAccess = new HttpEventDataAccessObject();
    }

    @Test
    public void testEvent() {
        final List<Event> result = dataAccess.getEvents("CA", LocalDate.of(2026, 7, 1));
        assertEquals(1, result.size());
        assertEquals("Canada Day", result.get(1).getName());
    }
}
