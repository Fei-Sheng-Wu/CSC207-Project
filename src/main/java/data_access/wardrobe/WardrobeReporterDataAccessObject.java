package data_access.wardrobe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearFactory;
import entity.WearStyle;
import use_case.wardrobe_reporter.WardrobeReporterDataAccessInterface;

/**
 * The DAO for the Wardrobe Reporter.
 */
public class WardrobeReporterDataAccessObject implements WardrobeReporterDataAccessInterface {
    // Should we rename this to WardrobeRepository... since it will be used by other use_cases as well?
    private static final String FILE_PATH = "src/main/resources/wardrobe.json";
    private final WearFactory wearFactory;

    public WardrobeReporterDataAccessObject(WearFactory wearFactory) {
        this.wearFactory = wearFactory;
    }

    @Override
    public Wardrobe fetchWardrobe() {
        final List<AbstractWear> savedClothes = new ArrayList<>();

        try {
            final String jsonContent = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            final JSONObject jsonObject = new JSONObject(jsonContent);
            final JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                final JSONObject jsonItem = itemsArray.getJSONObject(i);

                final String type = jsonItem.optString("type", "Unknown");
                final UUID uuid = UUID.fromString(jsonItem.getString("uuid"));

                final AbstractWear wear = wearFactory.constructWear(type, uuid);

                populateBasicAttributes(jsonItem, wear);
                populateEnumAttributes(jsonItem, wear);
                populateTags(jsonItem, wear);

                savedClothes.add(wear);
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
        return new Wardrobe(savedClothes);
    }

    @Override
    public void saveWardrobe(Wardrobe wardrobe) {
        // @TODO: Implement this method. This should be straightforward, as it is similar to fetchWardrobe.
        // The main difference is that this method writes to wardrobe.json.
    }


    private static void populateBasicAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has("name")) {
            wear.setName(jsonItem.getString("name"));
        }
        if (jsonItem.has("brand")) {
            wear.setBrand(jsonItem.getString("brand"));
        }
        if (jsonItem.has("purchaseDate")) {
            wear.setPurchaseDate(LocalDate.parse(jsonItem.getString("purchaseDate")));
        }
        if (jsonItem.has("fondness")) {
            wear.setFondness(jsonItem.getDouble("fondness"));
        }
    }

    private static void populateEnumAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has("color")) {
            wear.setColor(WearColor.valueOf(jsonItem.getString("color").toUpperCase()));
        }
        if (jsonItem.has("style")) {
            wear.setStyle(WearStyle.valueOf(jsonItem.getString("style").toUpperCase()));
        }
        if (jsonItem.has("condition")) {
            wear.setCondition(WearCondition.valueOf(jsonItem.getString("condition").toUpperCase()));
        }
    }

    private static void populateTags(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has("tags")) {
            final JSONArray tagsArray = jsonItem.getJSONArray("tags");
            final List<String> tagsList = new ArrayList<>();
            for (int j = 0; j < tagsArray.length(); j++) {
                tagsList.add(tagsArray.getString(j));
            }
            wear.setTags(tagsList);
        }
    }
}
