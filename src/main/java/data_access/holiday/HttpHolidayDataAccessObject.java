package data_access.holiday;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.Response;

import data_access.AbstractHttpRepositoryImpl;
import entity.Holiday;

/**
 * Represents an implementation of the holiday repository interface.
 */
public class HttpHolidayDataAccessObject
    extends AbstractHttpRepositoryImpl
    implements HolidayDataAccessInterface {
    private static final String API_BASE_URL = System.getenv("API_BASE_URL_HOLIDAY");
    private static final String API_KEY = System.getenv("API_KEY_HOLIDAY");

    private Response fetch(String endpoint) throws IOException {
        final Request request = new Request.Builder()
            .url(String.format("%s/%s", API_BASE_URL, endpoint))
            .build();
        return getClient().newCall(request).execute();
    }

    @Override
    public List<Holiday> getHolidays(String country, int year) {
        try (Response response = fetch(String.format(
            "holidays?api_key=%s&country=%s&year=%d", API_KEY, country, year))) {
            if (CODE_OK != response.code() || response.body() == null) {
                throw new RuntimeException(String.format("The holiday API has failed (%d).", response.code()));
            }

            final List<Holiday> result = new ArrayList<>();
            final JSONObject responseBody = new JSONObject(response.body().string());

            final JSONObject responseObj = responseBody.getJSONObject("response");
            final JSONArray holidaysArray = responseObj.getJSONArray("holidays");

            for (int i = 0; i < holidaysArray.length(); i++) {
                final JSONObject holiday = holidaysArray.getJSONObject(i);

                final String isoDateStr = holiday.getJSONObject("date").getString("iso");

                final LocalDate date = LocalDate.parse(isoDateStr.substring(0, 10));

                final String name = holiday.getString("name");
                final String type = holiday.getString("primary_type");

                // I will leave this as it is for now, and then you can decide whether the Holiday entity should be removed
                // or if there is a better way to integrate the results into the project. What I did here is similar
                // to what we did for the Weather API, where we constructed new Weather instances.
                result.add(new Holiday(date, name, type));
            }
            return result;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
