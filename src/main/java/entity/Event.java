package entity;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents a scheduled event (e.g. holidays) with specific outfit colors and styles.
 */
public final class Event {
    private final String name;
    private final ZonedDateTime dateStart;
    private final ZonedDateTime dateEnd;
    private final List<WearColor> wearColors;
    private final List<WearStyle> wearStyles;

    /**
     * Constructs a new event.
     *
     * @param name       the name of the event
     * @param dateStart  the start date of the event
     * @param dateEnd    the end date of the event
     * @param wearColors the outfit colors of the event
     * @param wearStyles the outfit styles of the event
     */
    public Event(String name,
                 ZonedDateTime dateStart,
                 ZonedDateTime dateEnd,
                 List<WearColor> wearColors,
                 List<WearStyle> wearStyles) {
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.wearColors = wearColors;
        this.wearStyles = wearStyles;
    }

    /**
     * Returns the name of the event.
     *
     * @return the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the start date of the event.
     *
     * @return the start date of the event
     */
    public ZonedDateTime getDateStart() {
        return dateStart;
    }

    /**
     * Returns the end date of the event.
     *
     * @return the end date of the event
     */
    public ZonedDateTime getDateEnd() {
        return dateEnd;
    }

    /**
     * Returns the outfit colors of the event.
     *
     * @return the outfit colors of the event
     */
    public List<WearColor> getWearColors() {
        return wearColors;
    }

    /**
     * Returns the outfit styles of the event.
     *
     * @return the outfit styles of the event
     */
    public List<WearStyle> getWearStyles() {
        return wearStyles;
    }
}
