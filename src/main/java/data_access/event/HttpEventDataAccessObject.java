package data_access.event;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import okhttp3.Request;
import okhttp3.Response;

import data_access.AbstractHttpDataAccessObject;
import entity.Event;
import entity.WearColor;
import entity.WearStyle;
import use_case.recommendation_context.EventDataAccessInterface;

/**
 * Represents an implementation of the holiday repository interface.
 */
public class HttpEventDataAccessObject
    extends AbstractHttpDataAccessObject
    implements EventDataAccessInterface {
    private static final String API_BASE_URL = System.getenv("API_BASE_URL_HOLIDAY");
    private static final String API_KEY = System.getenv("API_KEY_HOLIDAY");

    private static final int MINUTES_IN_DAY = 1440;

    private final Map<String, List<WearColor>> eventColors;
    private final Map<String, List<WearStyle>> eventStyles;

    public HttpEventDataAccessObject() {
        this.eventColors = new HashMap<>();
        this.eventStyles = new HashMap<>();

        // Read the event-to-wear lookup from embedded resources.
        try (InputStream stream = getClass().getResourceAsStream("/event_wears.json")) {
            if (stream == null) {
                throw new RuntimeException("The resources cannot be loaded.");
            }

            final JSONArray events = new JSONArray(new JSONTokener(stream));
            for (int i = 0; i < events.length(); i++) {
                final JSONObject event = events.getJSONObject(i);
                final String name = event.getString("name");

                final JSONArray colors = event.getJSONArray("colors");
                final List<WearColor> colorsParsed = new ArrayList<>();
                for (int j = 0; j < colors.length(); j++) {
                    colorsParsed.add(WearColor.valueOf(colors.getString(j).toUpperCase()));
                }
                eventColors.put(name, colorsParsed);

                final JSONArray styles = event.getJSONArray("styles");
                final List<WearStyle> stylesParsed = new ArrayList<>();
                for (int j = 0; j < styles.length(); j++) {
                    stylesParsed.add(WearStyle.valueOf(styles.getString(j).toUpperCase()));
                }
                eventStyles.put(name, stylesParsed);
            }
        } catch (IOException ex) {
            throw new RuntimeException("The resources cannot be loaded.");
        }
    }

    private Response fetch(String endpoint) throws IOException {
        final Request request = new Request.Builder()
            .url(String.format("%s/%s", API_BASE_URL, endpoint))
            .build();
        return getClient().newCall(request).execute();
    }

    @Override
    public List<Event> getEvents(String country) {
        final LocalDate now = LocalDate.now();
        try (Response response = fetch(String.format(
            "holidays?api_key=%s&country=%s&year=%d&month=%d&day=%d",
            API_KEY,
            country,
            now.getYear(),
            now.getMonthValue(),
            now.getDayOfMonth()
        ))) {
            if (CODE_OK != response.code() || response.body() == null) {
                throw new RuntimeException(String.format("The holiday API has failed (%d).", response.code()));
            }

            final List<Event> result = new ArrayList<>();
            final JSONObject responseBody = new JSONObject(response.body().string());

            final JSONObject responseObj = responseBody.getJSONObject("response");
            final JSONArray holidaysArray = responseObj.getJSONArray("holidays");
            for (int i = 0; i < holidaysArray.length(); i++) {
                final JSONObject holiday = holidaysArray.getJSONObject(i);
                final String name = holiday.getString("name");
                if (!eventColors.containsKey(name) || !eventStyles.containsKey(name)) {
                    continue;
                }

                final OffsetDateTime dateStart = LocalDate
                    .parse(holiday.getJSONObject("date").getString("iso"))
                    .atStartOfDay().atOffset(ZoneOffset.UTC);
                final OffsetDateTime dateEnd = dateStart.plusMinutes(MINUTES_IN_DAY - 1);

                result.add(new Event(name, dateStart, dateEnd, eventColors.get(name), eventStyles.get(name)));
            }

            return result;
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
