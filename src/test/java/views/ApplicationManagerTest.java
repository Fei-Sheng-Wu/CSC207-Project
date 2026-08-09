package views;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApplicationManagerTest {
    private ApplicationManager manager;

    @BeforeEach
    public void setUp() {
        manager = new ApplicationManager(new HashMap<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testRegisterAuto() {
        manager.register(Random.class);

        assertEquals(Random.class, manager.get(Random.class).getClass());
    }

    @Test
    public void testRegisterActual() {
        final Random random = new Random();
        manager.register(Random.class, random);

        assertEquals(random, manager.get(Random.class));
    }
}
