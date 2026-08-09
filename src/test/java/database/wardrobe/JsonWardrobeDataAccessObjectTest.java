package database.wardrobe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.AbstractFileDataAccessObject;
import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearColor;
import entity.WearCondition;
import entity.WearFactory;
import entity.WearStyle;

public class JsonWardrobeDataAccessObjectTest
    extends AbstractFileDataAccessObject {
    private static final String TEST_FILE = "wardrobe_dao_test.json";

    private JsonWardrobeDataAccessObject wardrobeDao;

    @BeforeEach
    public void setUp() {
        wardrobeDao = new JsonWardrobeDataAccessObject(TEST_FILE);
        clearTestFileWardrobe();
    }

    private void clearTestFileWardrobe() {
        try (FileWriter writer = new FileWriter(getPath(TEST_FILE).toString(), StandardCharsets.UTF_8)) {
            writer.write("[]");
        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testSaveWardrobeIncreaseSizeByOne() {
        final Wardrobe wardrobe = wardrobeDao.fetchWardrobe();
        final int initialSize = wardrobe.getItems().size();

        final AbstractWear newFootwear = WearFactory.constructWear("headwear", UUID.randomUUID());
        newFootwear.setName("Adidas Go Run 3");
        wardrobe.getItems().add(newFootwear);
        wardrobeDao.saveWardrobe(wardrobe);

        final Wardrobe updatedWardrobe = wardrobeDao.fetchWardrobe();
        assertEquals(initialSize + 1, updatedWardrobe.getItems().size());
    }

    @Test
    public void testFetchFromEmptyOrMissingFileReturnsEmptyWardrobe() {
        final Wardrobe emptyWardrobe = wardrobeDao.fetchWardrobe();

        assertNotNull(emptyWardrobe);
        assertTrue(emptyWardrobe.getItems().isEmpty());
    }

    @Test
    public void testSaveAndFetch() {
        final UUID expectedUuid = UUID.randomUUID();
        final AbstractWear shirt = WearFactory.constructWear("innertopwear", expectedUuid);

        shirt.setName("test_name");
        shirt.setBrand("Nike");
        shirt.setPurchaseDate(LocalDate.of(2026, 6, 25));
        shirt.setFondness(8.5);

        shirt.setColor(WearColor.BLACK);
        shirt.setStyle(WearStyle.CASUAL);
        shirt.setCondition(WearCondition.FAIR);

        shirt.setTags(new ArrayList<>(List.of("test1", "test2", "test3")));

        final Wardrobe originalWardrobe = new Wardrobe(new ArrayList<>(List.of(shirt)));
        wardrobeDao.saveWardrobe(originalWardrobe);

        final Wardrobe fetchedWardrobe = wardrobeDao.fetchWardrobe();
        assertEquals(1, fetchedWardrobe.getItems().size());
        final AbstractWear fetchedShirt = fetchedWardrobe.getItems().get(0);

        assertEquals(expectedUuid, fetchedShirt.getUuid());
        assertEquals("test_name", fetchedShirt.getName());
        assertEquals("Nike", fetchedShirt.getBrand());
        assertEquals(LocalDate.of(2026, 6, 25), fetchedShirt.getPurchaseDate());
        assertEquals(8.5, fetchedShirt.getFondness());

        assertEquals(WearColor.BLACK, fetchedShirt.getColor());
        assertEquals(WearStyle.CASUAL, fetchedShirt.getStyle());
        assertEquals(WearCondition.FAIR, fetchedShirt.getCondition());

        assertEquals(3, fetchedShirt.getTags().size());
        assertTrue(fetchedShirt.getTags().contains("test1"));
    }

    @Test
    public void testSaveAndFetchItemWithOnlyRequiredFields() {
        final UUID expectedUuid = UUID.randomUUID();
        final AbstractWear pants = WearFactory.constructWear("bottomwear", expectedUuid);

        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>(List.of(pants)));

        wardrobeDao.saveWardrobe(wardrobe);
        final Wardrobe fetchedWardrobe = wardrobeDao.fetchWardrobe();

        assertEquals(1, fetchedWardrobe.getItems().size());
        assertEquals(expectedUuid, fetchedWardrobe.getItems().get(0).getUuid());
    }

}
