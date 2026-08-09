package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class WearFactoryTest {
    @Test
    public void testConstructInnerTopwear() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("InnerTopwear", uuid);

        assertInstanceOf(InnerTopwear.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructOuterTopwear() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("OuterTopwear", uuid);

        assertInstanceOf(OuterTopwear.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructBottomwear() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("Bottomwear", uuid);

        assertInstanceOf(Bottomwear.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructFootwear() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("Footwear", uuid);

        assertInstanceOf(Footwear.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructHeadwear() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("Headwear", uuid);

        assertInstanceOf(Headwear.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructAccessory() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("Accessory", uuid);

        assertInstanceOf(Accessory.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructWearIsCaseInsensitive() {
        final UUID uuid = UUID.randomUUID();

        final AbstractWear wear = WearFactory.constructWear("footwear", uuid);

        assertInstanceOf(Footwear.class, wear);
        assertEquals(uuid, wear.getUuid());
    }

    @Test
    public void testConstructInvalidTypeThrowsException() {
        final UUID uuid = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> WearFactory.constructWear("InvalidType", uuid));
    }
}
