package entity;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents a scheduled event or activity for the user.
 * It tracks the time, and the colors and styles for the outfit.
 */
public class Event {
    // final ? should they be final or mutable is the user allowed to edit the event?
    private final String name;
    private final ZonedDateTime dateStart;
    private final ZonedDateTime dateEnd;
    private final List<WearColor> wearColors;
    private final List<WearStyle> wearStyles;

    public Event(List<WearStyle> wearStyles, List<WearColor> wearColors, ZonedDateTime dateEnd,
                 ZonedDateTime dateStart, String name) {
        this.wearStyles = wearStyles;
        this.wearColors = wearColors;
        this.dateEnd = dateEnd;
        this.dateStart = dateStart;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public ZonedDateTime getDateStart() {
        return dateStart;
    }
    public ZonedDateTime getDateEnd() {
        return dateEnd;
    }
    public List<WearColor> getWearColors() {
        return wearColors;
    }
    public List<WearStyle> getWearStyles() {
        return wearStyles;
    }
}
