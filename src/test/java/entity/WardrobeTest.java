package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class WardrobeTest {
    @Test
    public void testAddItem() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());
        final AbstractWear item = new InnerTopwear(UUID.randomUUID());

        wardrobe.addItem(item);

        assertEquals(1, wardrobe.getItems().size());
        assertTrue(wardrobe.getItems().contains(item));
    }

    @Test
    public void testUpdateExistingItem() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());
        final UUID uuid = UUID.randomUUID();

        final AbstractWear oldItem = new InnerTopwear(uuid);
        oldItem.setName("Old Shirt");

        final AbstractWear updatedItem = new InnerTopwear(uuid);
        updatedItem.setName("Updated Shirt");
        updatedItem.setBrand("Nike");
        updatedItem.setColor(WearColor.BLUE);
        updatedItem.setStyle(WearStyle.CASUAL);
        updatedItem.setCondition(WearCondition.NEW);

        wardrobe.addItem(oldItem);

        final boolean result = wardrobe.updateItem(updatedItem);

        assertTrue(result);
        assertEquals(1, wardrobe.getItems().size());
        assertEquals("Updated Shirt", wardrobe.getItems().get(0).getName());
        assertEquals("Nike", wardrobe.getItems().get(0).getBrand());
        assertEquals(WearColor.BLUE, wardrobe.getItems().get(0).getColor());
        assertEquals(WearStyle.CASUAL, wardrobe.getItems().get(0).getStyle());
        assertEquals(WearCondition.NEW, wardrobe.getItems().get(0).getCondition());
    }

    @Test
    public void testUpdateMissingItemReturnsFalse() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());
        final AbstractWear item = new InnerTopwear(UUID.randomUUID());

        final boolean result = wardrobe.updateItem(item);

        assertFalse(result);
        assertTrue(wardrobe.getItems().isEmpty());
    }

    @Test
    public void testRemoveExistingItem() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());
        final UUID uuid = UUID.randomUUID();

        final AbstractWear item = new Bottomwear(uuid);
        final AbstractWear itemToRemove = new Bottomwear(uuid);

        wardrobe.addItem(item);

        final boolean result = wardrobe.removeItem(itemToRemove);

        assertTrue(result);
        assertTrue(wardrobe.getItems().isEmpty());
    }

    @Test
    public void testRemoveMissingItemReturnsFalse() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());
        final AbstractWear item = new Footwear(UUID.randomUUID());

        final boolean result = wardrobe.removeItem(item);

        assertFalse(result);
        assertTrue(wardrobe.getItems().isEmpty());
    }

    @Test
    public void testRemoveOnlyMatchingUuid() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());

        final AbstractWear firstItem = new InnerTopwear(UUID.randomUUID());
        firstItem.setName("Blue Shirt");

        final AbstractWear secondItem = new Bottomwear(UUID.randomUUID());
        secondItem.setName("Black Jeans");

        wardrobe.addItem(firstItem);
        wardrobe.addItem(secondItem);

        final boolean result = wardrobe.removeItem(firstItem);

        assertTrue(result);
        assertEquals(1, wardrobe.getItems().size());
        assertEquals("Black Jeans", wardrobe.getItems().get(0).getName());
    }

    @Test
    public void testClearItems() {
        final Wardrobe wardrobe = new Wardrobe(new ArrayList<>());

        wardrobe.addItem(new InnerTopwear(UUID.randomUUID()));
        wardrobe.addItem(new Bottomwear(UUID.randomUUID()));

        wardrobe.clearItems();

        assertTrue(wardrobe.getItems().isEmpty());
    }
}
