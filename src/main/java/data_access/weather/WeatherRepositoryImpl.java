package data_access.weather;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import entity.Weather;

public class WeatherRepositoryImpl implements WeatherRepository {
    private static final String API_URL = System.getenv("API_BASE_URL_WEA");
    private static final String API_KEY = System.getenv("API_KEY");
    private static final int SUCCESS_CODE = 200;

    private Response fetchJsonFromApi(String endpoint) {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(String.format("%s/%s", API_URL, endpoint))
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Weather getByLocation(String location) {

        try {
            final Response response = fetchJsonFromApi("current.json?key=" + API_KEY + "&q=" + location);
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (response.code() == SUCCESS_CODE) {
                final JSONObject current = responseBody.getJSONObject("current");
                System.out.println(current.getDouble("temp_c"));
                return new Weather(
                        LocalDate.now(),
                        current.getJSONObject("condition").getString("text"),
                        current.getDouble("temp_c"),
                        current.getDouble("precip_mm"),
                        current.getDouble("wind_kph"),
                        current.getDouble("humidity"),
                        current.getInt("uv")
                );
            }
            else {
                throw new RuntimeException("API error: " + response.code());
            }
        } catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public List<Weather> getForecastByLocation(String location) throws JSONException {
        try {
            final Response response = fetchJsonFromApi("forecast.json?key=" + API_KEY + "&q=" + location + "&days=7");
            final JSONObject responseBody = new JSONObject(response.body().string());
            final JSONArray forecastArray = responseBody.getJSONObject("forecast").getJSONArray("forecastday");
            List<Weather> forecast = new ArrayList<>();

            if (response.code() == SUCCESS_CODE) {
                for (int i = 0; i < forecastArray.length(); i++) {
                    JSONObject dayData = forecastArray.getJSONObject(i);
                    JSONObject dayMetrics = dayData.getJSONObject("day");
                    System.out.println(dayMetrics.getDouble("maxtemp_c"));
                    forecast.add(new Weather(
                            LocalDate.now().plusDays(i),
                            dayMetrics.getJSONObject("condition").getString("text"),
                            dayMetrics.getDouble("maxtemp_c"),
                            dayMetrics.getDouble("totalprecip_mm"),
                            dayMetrics.getDouble("maxwind_kph"),
                            dayMetrics.getDouble("avghumidity"),
                            dayMetrics.getInt("uv")
                    ));
                }
                return forecast;
            } else {
                throw new RuntimeException("API error: " + response.code());
            }
        } catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }
}
