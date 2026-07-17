package data_access.wardrobe;

import java.io.FileWriter;
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
    private static final String BRAND_KEY = "brand";
    private static final String NAME_KEY = "name";
    private static final String PURCHASE_DATE_KEY = "purchaseData";
    private static final String FONDNESS_KEY = "fondness";
    private static final String COLOR_KEY = "color";
    private static final String STYLE_KEY = "style";
    private static final String CONDITION_KEY = "condition";
    private static final String TAGS_KEY = "tags";
    private static final int INDENT_FACTOR = 4;
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
        final JSONObject parentJSONObject = new JSONObject();
        final JSONArray itemsJSONArray = new JSONArray();

        for (AbstractWear wear: wardrobe.getItems()) {
            final JSONObject jsonItem = new JSONObject();

            jsonItem.put("uuid", wear.getUuid().toString());
            // Hopefully this returns the name of the class.
            jsonItem.put("type", wear.getClass().getSimpleName());

            // Not sure if we need precondition checks. Or we could assume the given arguments are valid.
            getPutBasicAttributes(jsonItem, wear);
            getPutEnumAttributed(jsonItem, wear);
            getPutTags(jsonItem, wear);

            itemsJSONArray.put(jsonItem);
        }
        parentJSONObject.put("items", itemsJSONArray);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(parentJSONObject.toString(INDENT_FACTOR));
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    // --- fetchWardrobe() HELPER METHODS ---
    private static void populateBasicAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has(NAME_KEY)) {
            wear.setName(jsonItem.getString(NAME_KEY));
        }
        if (jsonItem.has(BRAND_KEY)) {
            wear.setBrand(jsonItem.getString(BRAND_KEY));
        }
        if (jsonItem.has(PURCHASE_DATE_KEY)) {
            wear.setPurchaseDate(LocalDate.parse(jsonItem.getString(PURCHASE_DATE_KEY)));
        }
        if (jsonItem.has(FONDNESS_KEY)) {
            wear.setFondness(jsonItem.getDouble(FONDNESS_KEY));
        }
    }

    private static void populateEnumAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has(COLOR_KEY)) {
            wear.setColor(WearColor.valueOf(jsonItem.getString(COLOR_KEY).toUpperCase()));
        }
        if (jsonItem.has(STYLE_KEY)) {
            wear.setStyle(WearStyle.valueOf(jsonItem.getString(STYLE_KEY).toUpperCase()));
        }
        if (jsonItem.has(CONDITION_KEY)) {
            wear.setCondition(WearCondition.valueOf(jsonItem.getString(CONDITION_KEY).toUpperCase()));
        }
    }

    private static void populateTags(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has(TAGS_KEY)) {
            final JSONArray tagsArray = jsonItem.getJSONArray("tags");
            final List<String> tagsList = new ArrayList<>();
            for (int j = 0; j < tagsArray.length(); j++) {
                tagsList.add(tagsArray.getString(j));
            }
            wear.setTags(tagsList);
        }
    }

    // --- saveWardrobe() HELPER METHODS ---
    private static void getPutBasicAttributes(JSONObject jsonItem, AbstractWear wear) {
        jsonItem.put(NAME_KEY, wear.getName());

        jsonItem.put(BRAND_KEY, wear.getBrand());

        jsonItem.put(PURCHASE_DATE_KEY, wear.getPurchaseDate().toString());

        jsonItem.put(FONDNESS_KEY, wear.getFondness());
    }

    private static void getPutEnumAttributed(JSONObject jsonItem, AbstractWear wear) {
        jsonItem.put(COLOR_KEY, wear.getColor().name());

        jsonItem.put(STYLE_KEY, wear.getStyle().name());

        jsonItem.put(CONDITION_KEY, wear.getCondition().name());
    }

    private static void getPutTags(JSONObject jsonItem, AbstractWear wear) {
        final JSONArray tagsArray = new JSONArray();
        for (String tag : wear.getTags()) {
            tagsArray.put(tag);
        }
        jsonItem.put(TAGS_KEY, tagsArray);
    }
}
