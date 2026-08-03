package use_case.recommendation_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.InnerTopwear;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import entity.Weather;

/**
 * Tests for the narrowing that bounds the recommendation search.
 */
class SlotNarrowerTest {
    private static final int LIMIT = 3;

    @Test
    void keepsEverythingWhenTheSlotAlreadyFitsWithinTheLimit() {
        final List<InnerTopwear> items = List.of(
                shirt(1, WearColor.RED, WearStyle.CASUAL, 0.5),
                shirt(2, WearColor.BLUE, WearStyle.CASUAL, 0.5)
        );

        final List<InnerTopwear> narrowed = narrower().narrow(items, context(List.of(), List.of()));

        assertEquals(items, narrowed);
    }

    @Test
    void keepsTheGarmentsThatMatchThePreferences() {
        final InnerTopwear matching = shirt(1, WearColor.RED, WearStyle.CASUAL, 0.1);
        final InnerTopwear alsoMatching = shirt(2, WearColor.RED, WearStyle.CASUAL, 0.1);
        final InnerTopwear colorOnly = shirt(3, WearColor.RED, WearStyle.FORMAL, 0.1);
        final InnerTopwear unrelated = shirt(4, WearColor.BLUE, WearStyle.FORMAL, 0.9);
        final InnerTopwear alsoUnrelated = shirt(5, WearColor.BLUE, WearStyle.FORMAL, 0.9);

        final List<InnerTopwear> narrowed = narrower().narrow(
                List.of(unrelated, colorOnly, alsoUnrelated, matching, alsoMatching),
                context(List.of(WearColor.RED), List.of(WearStyle.CASUAL))
        );

        assertEquals(LIMIT, narrowed.size());
        assertTrue(narrowed.contains(matching));
        assertTrue(narrowed.contains(alsoMatching));
        assertTrue(narrowed.contains(colorOnly));
        // A high fondness never outranks matching the context the user actually asked for.
        assertFalse(narrowed.contains(unrelated));
    }

    @Test
    void breaksTiesOnFondnessBeforeAnythingElse() {
        final InnerTopwear liked = shirt(1, WearColor.BLUE, WearStyle.FORMAL, 0.9);
        final InnerTopwear disliked = shirt(2, WearColor.BLUE, WearStyle.FORMAL, 0.1);
        final InnerTopwear middling = shirt(3, WearColor.BLUE, WearStyle.FORMAL, 0.5);
        final InnerTopwear leastLiked = shirt(4, WearColor.BLUE, WearStyle.FORMAL, 0.0);

        final List<InnerTopwear> narrowed = narrower().narrow(
                List.of(leastLiked, disliked, middling, liked),
                context(List.of(), List.of())
        );

        assertEquals(List.of(liked, middling, disliked), narrowed);
    }

    @Test
    void resultDoesNotDependOnTheOrderTheWardrobeWasStoredIn() {
        final List<InnerTopwear> items = new ArrayList<>(List.of(
                shirt(1, WearColor.BLUE, WearStyle.FORMAL, 0.5),
                shirt(2, WearColor.BLUE, WearStyle.FORMAL, 0.5),
                shirt(3, WearColor.BLUE, WearStyle.FORMAL, 0.5),
                shirt(4, WearColor.BLUE, WearStyle.FORMAL, 0.5),
                shirt(5, WearColor.BLUE, WearStyle.FORMAL, 0.5)
        ));
        final RecommendationContext context = context(List.of(), List.of());

        final List<InnerTopwear> fromOneOrder = narrower().narrow(items, context);
        Collections.reverse(items);
        final List<InnerTopwear> fromAnother = narrower().narrow(items, context);

        assertEquals(fromOneOrder, fromAnother);
    }

    @Test
    void reportsWhetherGarmentSuitsTheContextAtAll() {
        final InnerTopwear matching = shirt(1, WearColor.RED, WearStyle.CASUAL, 0.5);
        final InnerTopwear unrelated = shirt(2, WearColor.BLUE, WearStyle.FORMAL, 1.0);
        final RecommendationContext context = context(List.of(WearColor.RED), List.of());

        assertTrue(narrower().contributes(matching, context));
        // Fondness alone is not something a garment can be judged to suit the context by.
        assertFalse(narrower().contributes(unrelated, context));
    }

    @Test
    void narrowingIsDrivenOnlyByTheSuppliedCriteria() {
        final InnerTopwear first = shirt(1, WearColor.RED, WearStyle.CASUAL, 0.1);
        final InnerTopwear second = shirt(2, WearColor.BLUE, WearStyle.FORMAL, 0.9);
        final SlotNarrower indifferent = new SlotNarrower(List.of(), 1);

        final List<InnerTopwear> narrowed = indifferent.narrow(
                List.of(first, second), context(List.of(WearColor.RED), List.of(WearStyle.CASUAL)));

        assertEquals(1, narrowed.size());
        // With no criteria to score by, only fondness can separate the two.
        assertSame(second, narrowed.get(0));
    }

    private static SlotNarrower narrower() {
        return new SlotNarrower(OutfitAnalyzers.standardItems(), LIMIT);
    }

    private static InnerTopwear shirt(int id, WearColor color, WearStyle style, double fondness) {
        final InnerTopwear shirt = new InnerTopwear(new UUID(0L, id));
        shirt.setColor(color);
        shirt.setStyle(style);
        shirt.setCondition(WearCondition.NEW);
        shirt.setFondness(fondness);
        return shirt;
    }

    private static RecommendationContext context(List<WearColor> colors, List<WearStyle> styles) {
        return new RecommendationContext(
                new Weather(LocalDate.of(2026, 7, 1), "Clear", 20.0, 0.0, 0.0, 0.0, 0),
                List.of(),
                colors,
                styles
        );
    }
}
