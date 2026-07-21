package data_access.social_scrawl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import data_access.social_crawl.HttpSocialDataAccessObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import entity.OutfitIdea;

public class HttpSocialDataAccessObjectTest {
    private static List<OutfitIdea> outfitIdeas;

    @BeforeAll
    public static void SocialScrawlApiSetUp() {
        final HttpSocialDataAccessObject httpSocial = new HttpSocialDataAccessObject();
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
