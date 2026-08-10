package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    private Event event;

    @BeforeEach
    void setUp() {
        final OffsetDateTime date = LocalDate.of(2026, 7, 1)
            .atStartOfDay().atOffset(ZoneOffset.UTC);
        event = new Event(
            "Canada Day",
            date,
            date.plusMinutes(1339),
            List.of(WearColor.RED, WearColor.WHITE),
            List.of()
        );
    }

    @Test
    void testName() {
        assertEquals("Canada Day", event.getName());
    }

    @Test
    void testDateStart() {
        assertEquals(2026, event.getDateStart().getYear());
        assertEquals(7, event.getDateStart().getMonthValue());
        assertEquals(1, event.getDateStart().getDayOfMonth());
    }

    @Test
    void testDateEnd() {
        assertEquals(2026, event.getDateEnd().getYear());
        assertEquals(7, event.getDateEnd().getMonthValue());
        assertEquals(1, event.getDateEnd().getDayOfMonth());
    }

    @Test
    void testWearColors() {
        assertEquals(2, event.getWearColors().size());
        assertEquals(WearColor.RED, event.getWearColors().get(0));
        assertEquals(WearColor.WHITE, event.getWearColors().get(1));
    }

    @Test
    void testWearStyles() {
        assertEquals(0, event.getWearStyles().size());
    }
}
