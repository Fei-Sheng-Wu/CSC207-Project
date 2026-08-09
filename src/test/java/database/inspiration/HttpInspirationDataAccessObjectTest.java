package database.inspiration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import entity.OutfitIdea;

/**
 * Tests for the social API data access object.
 *
 * <p>This is a live integration test: it calls the real API and therefore needs credentials.
 * It runs when {@code API_BASE_URL_SOCIAL} and {@code API_KEY_SOCIAL} are set, and is skipped
 * otherwise so that a developer without credentials still gets a green build.
 */
@EnabledIfEnvironmentVariable(named = "API_BASE_URL_SOCIAL", matches = ".+")
@EnabledIfEnvironmentVariable(named = "API_KEY_SOCIAL", matches = ".+")
public class HttpInspirationDataAccessObjectTest {
    private static List<OutfitIdea> outfitIdeas;

    @BeforeAll
    public static void socialScrawlApiSetUp() {
        final HttpInspirationDataAccessObject httpSocial = new HttpInspirationDataAccessObject();
        outfitIdeas = httpSocial.getOutfitIdeas("Outfit ideas for men today");
    }

    @Test
    public void testGetOutfitIdeasReturnsValidList() {
        assertNotNull(outfitIdeas);
        assertFalse(outfitIdeas.isEmpty());

        final OutfitIdea firstHoliday = outfitIdeas.get(0);
        assertNotNull(firstHoliday.getDescription());
        assertNotNull(firstHoliday.getUrl());
    }

    // I understand using println() in a test file is not a good practice. But for now
    // I will just use it to ensure there is some valid output.
    @Test
    public void testGetHolidaysReturnsValidListElement() {
        for (OutfitIdea outfitIdea : outfitIdeas) {
            System.out.println(outfitIdea.getDescription());
            System.out.println(outfitIdea.getUrl());
            System.out.println("--------------------");
        }
    }
}
