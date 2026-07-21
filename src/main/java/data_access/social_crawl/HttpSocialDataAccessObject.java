package data_access.social_crawl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.OutfitIdea;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.Response;

import data_access.AbstractHttpDataAccessObject;

/**
 * Represents an implementation of the Social data access interface.
 */
public class HttpSocialDataAccessObject
    extends AbstractHttpDataAccessObject
    implements SocialDataAccessInterface {
    private static final String API_BASE_URL = System.getenv("API_BASE_URL_SOCIAL");
    private static final String API_KEY = System.getenv("API_KEY_SOCIAL");
    private static final String IDEMPOTENCY_KEY = "7a5e1b4c-2d8f-4a3b-9c1e-6e8b4d2a1f3c";

    private Response fetch(String endpoint) throws IOException {
        final Request request = new Request.Builder()
            .url(String.format("%s/%s", API_BASE_URL, endpoint))
            .header("Cache-Control", "no-cache")
            .header("Idempotency-Key", IDEMPOTENCY_KEY)
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
            final JSONArray outfitIdeasArray = new JSONObject(response.body().string())
                                                    .getJSONObject("data")
                                                    .getJSONArray("items");

            for (int i = 0; i < outfitIdeasArray.length(); i++) {
                final JSONObject post = outfitIdeasArray.getJSONObject(i).getJSONObject("post");

                final String pinterestUrl = post.getString("url");
                final String description = post.getJSONObject("content").isNull("text")
                    ? "No description"
                    : post.getJSONObject("content").getString("text");

                if (!pinterestUrl.equals("https://www.pinterest.com/pin/8367697593331739")
                    && !description.equals("No description")) {
                    result.add(new OutfitIdea(description, pinterestUrl));
                }
            }
            return result;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
