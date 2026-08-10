package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OutfitIdeaTest {
    @Test
    void testDescription() {
        final OutfitIdea idea = new OutfitIdea("Test Description", "");
        assertEquals("Test Description", idea.getDescription());
    }

    @Test
    void testUrl() {
        final OutfitIdea idea = new OutfitIdea("", "www.example.com");
        assertEquals("www.example.com", idea.getUrl());
    }
}
