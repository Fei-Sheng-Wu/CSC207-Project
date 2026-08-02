package data_access.wardrobe;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data_access.AbstractFileDataAccessObject;
import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearFactory;
import entity.WearStyle;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Represents the data access implementation for wardrobe-related actions.
 */
public class JsonWardrobeDataAccessObject
    extends AbstractFileDataAccessObject
    implements WardrobeDataAccessInterface {
    private final String fileName;

    private static final int INDENT_FACTOR = 4;

    private static final String KEY_TYPE = "type";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_COLOR = "color";
    private static final String KEY_STYLE = "style";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_PURCHASE_DATE = "purchaseDate";
    private static final String KEY_FONDNESS = "fondness";
    private static final String KEY_TAGS = "tags";

    public JsonWardrobeDataAccessObject(String fileName) {
        this.fileName = fileName;
    } // depeendency injection ( more flexibility)

    @Override
    public Wardrobe fetchWardrobe() {
        final List<AbstractWear> result = new ArrayList<>();

        try {
            final String jsonContent = Files.readString(getPath(fileName), StandardCharsets.UTF_8);
            final JSONArray jsonItems;
            if (jsonContent.isEmpty()) {
                jsonItems = new JSONArray();
            } else {
                jsonItems = new JSONArray(jsonContent);
            }

            for (int i = 0; i < jsonItems.length(); i++) {
                final JSONObject jsonItem = jsonItems.getJSONObject(i);
                final AbstractWear wear = WearFactory.constructWear(
                    jsonItem.getString(KEY_TYPE),
                    UUID.fromString(jsonItem.getString(KEY_UUID))
                );
                populateBasicAttributes(jsonItem, wear);
                populateEnumAttributes(jsonItem, wear);
                populateTags(jsonItem, wear);

                result.add(wear);
            }
        } catch (IOException | JSONException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }

        return new Wardrobe(result);
    }

    @Override
    public void saveWardrobe(Wardrobe wardrobe) {
        final JSONArray jsonItems = new JSONArray();
        for (AbstractWear wear : wardrobe.getItems()) {
            final JSONObject jsonItem = new JSONObject();
            jsonItem.put(KEY_TYPE, wear.getClass().getSimpleName());
            jsonItem.put(KEY_UUID, wear.getUuid().toString());
            serializeBasicAttributes(jsonItem, wear);
            serializeEnumAttributes(jsonItem, wear);
            serializeTags(jsonItem, wear);

            jsonItems.put(jsonItem);
        }

        try (FileWriter writer = new FileWriter(getPath(fileName).toString(), StandardCharsets.UTF_8)) {
            writer.write(jsonItems.toString(INDENT_FACTOR));
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }


    private static void populateBasicAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has(KEY_NAME)) {
            wear.setName(jsonItem.getString(KEY_NAME));
        }
        if (jsonItem.has(KEY_BRAND)) {
            wear.setBrand(jsonItem.getString(KEY_BRAND));
        }
        if (jsonItem.has(KEY_PURCHASE_DATE)) {
            wear.setPurchaseDate(LocalDate.parse(jsonItem.getString(KEY_PURCHASE_DATE)));
        }
        if (jsonItem.has(KEY_FONDNESS)) {
            wear.setFondness(jsonItem.getDouble(KEY_FONDNESS));
        }
    }

    private static void populateEnumAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has(KEY_COLOR)) {
            wear.setColor(WearColor.valueOf(jsonItem.getString(KEY_COLOR).toUpperCase()));
        }
        if (jsonItem.has(KEY_STYLE)) {
            wear.setStyle(WearStyle.valueOf(jsonItem.getString(KEY_STYLE).toUpperCase()));
        }
        if (jsonItem.has(KEY_CONDITION)) {
            wear.setCondition(WearCondition.valueOf(jsonItem.getString(KEY_CONDITION).toUpperCase()));
        }
    }

    private static void populateTags(JSONObject jsonItem, AbstractWear wear) {
        if (jsonItem.has(KEY_TAGS)) {
            final JSONArray tagsArray = jsonItem.getJSONArray(KEY_TAGS);
            final List<String> tagsList = new ArrayList<>();
            for (int j = 0; j < tagsArray.length(); j++) {
                tagsList.add(tagsArray.getString(j));
            }
            wear.setTags(tagsList);
        }
    }

    // --- saveWardrobe() HELPER METHODS ---
    private static void serializeBasicAttributes(JSONObject jsonItem, AbstractWear wear) {
        jsonItem.put(KEY_NAME, wear.getName());
        jsonItem.put(KEY_BRAND, wear.getBrand());
        if (wear.getPurchaseDate() != null) {
            jsonItem.put(KEY_PURCHASE_DATE, wear.getPurchaseDate().toString());
        }
        jsonItem.put(KEY_FONDNESS, wear.getFondness());
    }

    private static void serializeEnumAttributes(JSONObject jsonItem, AbstractWear wear) {
        if (wear.getColor() != null) {
            jsonItem.put(KEY_COLOR, wear.getColor().name());
        }
        if (wear.getStyle() != null) {
            jsonItem.put(KEY_STYLE, wear.getStyle().name());
        }
        if (wear.getCondition() != null) {
            jsonItem.put(KEY_CONDITION, wear.getCondition().name());
        }
    }

    private static void serializeTags(JSONObject jsonItem, AbstractWear wear) {
        final JSONArray tagsArray = new JSONArray();
        for (String tag : wear.getTags()) {
            tagsArray.put(tag);
        }
        jsonItem.put(KEY_TAGS, tagsArray);
    }
}
