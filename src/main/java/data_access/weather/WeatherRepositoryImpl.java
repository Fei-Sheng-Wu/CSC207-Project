package data_access.weather;

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
import entity.Weather;

/**
 * Represents an implementation of the weather repository interface.
 */
public class WeatherRepositoryImpl extends AbstractHttpRepositoryImpl implements WeatherRepository {
    private static final String API_BASE_URL = System.getenv("API_BASE_URL_WEATHER");
    private static final String API_KEY = System.getenv("API_KEY_WEATHER");

    private Response fetch(String endpoint) throws IOException {
        final Request request = new Request.Builder()
            .url(String.format("%s/%s", API_BASE_URL, endpoint))
            .build();
        return getClient().newCall(request).execute();
    }

    @Override
    public Weather getCurrentByLocation(String location) {
        try (Response response = fetch(String.format("current.json?key=%s&q=%s", API_KEY, location))) {
            if (CODE_OK != response.code() || response.body() == null) {
                throw new RuntimeException(String.format("The weather API has failed (%d).", response.code()));
            }

            final JSONObject current = new JSONObject(response.body().string())
                .getJSONObject("current");
            return new Weather(
                LocalDate.now(),
                current.getJSONObject("condition").getString("text"),
                current.getDouble("temp_c"),
                current.getDouble("precip_mm"),
                current.getDouble("wind_kph"),
                current.getDouble("humidity"),
                current.getInt("uv")
            );
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Weather> getForecastByLocation(String location) {
        try (Response response = fetch(String.format("forecast.json?key=%s&q=%s&days=7", API_KEY, location))) {
            if (CODE_OK != response.code() || response.body() == null) {
                throw new RuntimeException(String.format("The weather API has failed (%d).", response.code()));
            }

            final List<Weather> result = new ArrayList<>();
            final JSONArray forecasts = new JSONObject(response.body().string())
                .getJSONObject("forecast").getJSONArray("forecastday");
            final LocalDate now = LocalDate.now();
            for (int i = 0; i < forecasts.length(); i++) {
                final JSONObject forecast = forecasts.getJSONObject(i).getJSONObject("day");
                result.add(new Weather(
                    now.plusDays(i),
                    forecast.getJSONObject("condition").getString("text"),
                    forecast.getDouble("avgtemp_c"),
                    forecast.getDouble("totalprecip_mm"),
                    forecast.getDouble("maxwind_kph"),
                    forecast.getDouble("avghumidity"),
                    forecast.getInt("uv")
                ));
            }

            return result;
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
