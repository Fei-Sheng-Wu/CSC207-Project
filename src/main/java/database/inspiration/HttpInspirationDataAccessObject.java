package database.inspiration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.Response;

import database.AbstractHttpDataAccessObject;
import entity.OutfitIdea;
import use_case.inspiration_curator.InspirationDataAccessInterface;

/**
 * Represents an implementation of the Social data access interface.
 */
public class HttpInspirationDataAccessObject
    extends AbstractHttpDataAccessObject
    implements InspirationDataAccessInterface {
    private static final String API_BASE_URL = System.getenv("API_BASE_URL_SOCIAL");
    private static final String API_KEY = System.getenv("API_KEY_SOCIAL");

    private static final String DEFAULT_URL = "https://www.pinterest.com/pin/8367697593331739";

    private Response fetch(String endpoint) throws IOException {
        final Request request = new Request.Builder()
            .url(String.format("%s/%s", API_BASE_URL, endpoint))
            .header("Cache-Control", "no-cache")
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .header("x-api-key", API_KEY)
            .build();
        return getClient().newCall(request).execute();
    }

    @Override
    public List<OutfitIdea> getOutfitIdeas(String query) {
        try (Response response = fetch(String.format("/search?query=%s", query))) {
            if (CODE_OK != response.code() || response.body() == null) {
                throw new RuntimeException(String.format("The SocialScrawl API has failed (%d).", response.code()));
            }

            final List<OutfitIdea> result = new ArrayList<>();
            final JSONArray items = new JSONObject(response.body().string())
                .getJSONObject("data").getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                final JSONObject post = items.getJSONObject(i).getJSONObject("post");
                final String url = post.getString("url");
                if (DEFAULT_URL.equals(url)) {
                    continue;
                }

                final String description;
                if (post.getJSONObject("content").isNull("text")) {
                    description = "";
                } else {
                    description = post.getJSONObject("content").getString("text");
                }

                result.add(new OutfitIdea(description, url));
            }

            return result;
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
