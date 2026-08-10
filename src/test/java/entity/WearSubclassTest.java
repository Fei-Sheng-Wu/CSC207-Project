package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class WearSubclassTest {
    @Test
    public void testAllSubclassesStoreUuid() {
        final UUID innerUuid = UUID.randomUUID();
        final UUID outerUuid = UUID.randomUUID();
        final UUID bottomUuid = UUID.randomUUID();
        final UUID footwearUuid = UUID.randomUUID();
        final UUID headwearUuid = UUID.randomUUID();
        final UUID accessoryUuid = UUID.randomUUID();

        final AbstractWear inner = new InnerTopwear(innerUuid);
        final AbstractWear outer = new OuterTopwear(outerUuid);
        final AbstractWear bottom = new Bottomwear(bottomUuid);
        final AbstractWear footwear = new Footwear(footwearUuid);
        final AbstractWear headwear = new Headwear(headwearUuid);
        final AbstractWear accessory = new Accessory(accessoryUuid);

        assertInstanceOf(AbstractWear.class, inner);
        assertInstanceOf(AbstractWear.class, outer);
        assertInstanceOf(AbstractWear.class, bottom);
        assertInstanceOf(AbstractWear.class, footwear);
        assertInstanceOf(AbstractWear.class, headwear);
        assertInstanceOf(AbstractWear.class, accessory);

        assertEquals(innerUuid, inner.getUuid());
        assertEquals(outerUuid, outer.getUuid());
        assertEquals(bottomUuid, bottom.getUuid());
        assertEquals(footwearUuid, footwear.getUuid());
        assertEquals(headwearUuid, headwear.getUuid());
        assertEquals(accessoryUuid, accessory.getUuid());
    }

    @Test
    public void testDefaultFields() {
        final AbstractWear wear = new InnerTopwear(UUID.randomUUID());

        assertEquals("", wear.getName());
        assertEquals("", wear.getBrand());
        assertNull(wear.getColor());
        assertNull(wear.getStyle());
        assertNull(wear.getCondition());
        assertNull(wear.getPurchaseDate());
        assertEquals(1.0, wear.getFondness());
        assertEquals(List.of(), wear.getTags());
    }

    @Test
    public void testSettersAndGetters() {
        final AbstractWear wear = new InnerTopwear(UUID.randomUUID());
        final LocalDate purchaseDate = LocalDate.of(2025, 1, 1);
        final List<String> tags = List.of("summer", "casual");

        wear.setName("Blue Shirt");
        wear.setBrand("Nike");
        wear.setColor(WearColor.BLUE);
        wear.setStyle(WearStyle.CASUAL);
        wear.setCondition(WearCondition.NEW);
        wear.setPurchaseDate(purchaseDate);
        wear.setFondness(0.8);
        wear.setTags(tags);

        assertEquals("Blue Shirt", wear.getName());
        assertEquals("Nike", wear.getBrand());
        assertEquals(WearColor.BLUE, wear.getColor());
        assertEquals(WearStyle.CASUAL, wear.getStyle());
        assertEquals(WearCondition.NEW, wear.getCondition());
        assertEquals(purchaseDate, wear.getPurchaseDate());
        assertEquals(0.8, wear.getFondness());
        assertEquals(tags, wear.getTags());
    }

    @Test
    public void testOuterTopwearIsThickField() {
        final OuterTopwear outerTopwear = new OuterTopwear(UUID.randomUUID());

        assertFalse(outerTopwear.isThick());

        outerTopwear.setIsThick(true);

        assertTrue(outerTopwear.isThick());
    }

    @Test
    public void testBottomwearIsLongField() {
        final Bottomwear bottomwear = new Bottomwear(UUID.randomUUID());

        assertFalse(bottomwear.isLong());

        bottomwear.setIsLong(true);

        assertTrue(bottomwear.isLong());
    }

    @Test
    public void testFootwearIsWaterproofField() {
        final Footwear footwear = new Footwear(UUID.randomUUID());

        assertFalse(footwear.isWaterproof());

        footwear.setIsWaterproof(true);

        assertTrue(footwear.isWaterproof());
    }

    @Test
    public void testDisplayString() {
        final InnerTopwear wear = new InnerTopwear(UUID.randomUUID());

        assertEquals("[unnamed]", wear.getDisplayString());

        wear.setName("Name");

        assertEquals("Name", wear.getDisplayString());

        wear.setBrand("Brand");

        assertEquals("Name (Brand)", wear.getDisplayString());

        wear.setName("");
        wear.setBrand("Brand");

        assertEquals("[unnamed] (Brand)", wear.getDisplayString());
    }
}
