package entity;

import java.time.LocalDate;

/**
 * Represents a calendar holiday.
 * NOTE - I will leave this as it is for now, and then you can decide whether the Holiday entity should be
 * removed or if there is a better way to integrate the results into the project. What I did here is similar
 * to what we did for the Weather API, where we constructed a new instance.
 *
 */
public class Holiday {
    private final LocalDate date;
    private final String name;
    private final String type;

    /**
     * Constructs a new Holiday.
     *
     * @param date the date of the holiday
     * @param name the name of the holiday
     * @param type the type of the holiday
     */
    public Holiday(LocalDate date, String name, String type) {
        this.date = date;
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the date of the holiday
     *
     * @return the date of the holiday
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the name of the holiday.
     *
     * @return the type of the holiday
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the holiday.
     *
     * @return the type of the holiday
     */
    public String getType() {
        return type;
    }
}
