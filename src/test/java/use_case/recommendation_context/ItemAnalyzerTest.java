package use_case.recommendation_context;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import entity.Accessory;
import entity.Event;
import entity.InnerTopwear;
import entity.WearColor;
import entity.WearCondition;
import entity.WearStyle;
import entity.Weather;

/**
 * Tests for the criteria that score a single garment.
 *
 * <p>These are the criteria the interactor narrows each wardrobe slot with, so what they count
 * decides which garments are ever combined into an outfit at all.
 */
class ItemAnalyzerTest {
    @Test
    void preferenceAnalyzerCountsColorAndStyleSeparately() {
        final InnerTopwear shirt = shirt(WearColor.RED, WearStyle.CASUAL);

        final ItemScore both = new PreferenceItemAnalyzer().analyze(
                shirt, context(List.of(), List.of(WearColor.RED), List.of(WearStyle.CASUAL)));
        final ItemScore colorOnly = new PreferenceItemAnalyzer().analyze(
                shirt, context(List.of(), List.of(WearColor.RED), List.of(WearStyle.FORMAL)));
        final ItemScore neither = new PreferenceItemAnalyzer().analyze(
                shirt, context(List.of(), List.of(WearColor.BLUE), List.of(WearStyle.FORMAL)));

        assertEquals(2, both.getPreferenceMatches());
        assertEquals(1, colorOnly.getPreferenceMatches());
        assertEquals(0, neither.getPreferenceMatches());
        assertEquals(0, both.getEventMatches());
    }

    @Test
    void eventAnalyzerCountsAttributesOfEveryEventAtOnce() {
        final InnerTopwear shirt = shirt(WearColor.RED, WearStyle.FORMAL);
        final Event parade = event("Parade", WearColor.RED, WearStyle.CASUAL);
        final Event gala = event("Gala", WearColor.BLACK, WearStyle.FORMAL);

        final ItemScore score = new EventItemAnalyzer().analyze(
                shirt, context(List.of(parade, gala), List.of(), List.of()));

        assertEquals(2, score.getEventMatches());
        assertEquals(0, score.getPreferenceMatches());
    }

    @Test
    void analyzersTolerateGarmentsWithoutColorOrStyle() {
        // A garment loaded from storage may carry neither, and the preference lists are immutable,
        // which means a null lookup is rejected rather than answered.
        final InnerTopwear unlabelled = new InnerTopwear(UUID.randomUUID());
        unlabelled.setCondition(WearCondition.NEW);
        final RecommendationContext context = context(
                List.of(event("Parade", WearColor.RED, WearStyle.CASUAL)),
                List.of(WearColor.RED),
                List.of(WearStyle.CASUAL)
        );

        assertDoesNotThrow(() -> new PreferenceItemAnalyzer().analyze(unlabelled, context));
        assertDoesNotThrow(() -> new EventItemAnalyzer().analyze(unlabelled, context));
        assertEquals(0, new PreferenceItemAnalyzer().analyze(unlabelled, context).getPreferenceMatches());
        assertEquals(0, new EventItemAnalyzer().analyze(unlabelled, context).getEventMatches());
    }

    @Test
    void scoresAddUpAndReportWhetherTheyContribute() {
        final Accessory watch = new Accessory(UUID.randomUUID());
        watch.setColor(WearColor.BLACK);
        watch.setStyle(WearStyle.FORMAL);
        watch.setCondition(WearCondition.NEW);
        final RecommendationContext context = context(
                List.of(event("Gala", WearColor.BLACK, WearStyle.CASUAL)),
                List.of(WearColor.BLACK),
                List.of()
        );

        final ItemScore combined = new EventItemAnalyzer().analyze(watch, context)
                .plus(new PreferenceItemAnalyzer().analyze(watch, context));

        assertEquals(1, combined.getEventMatches());
        assertEquals(1, combined.getPreferenceMatches());
        assertTrue(combined.contributes());
        assertFalse(ItemScore.none().contributes());
    }

    private static InnerTopwear shirt(WearColor color, WearStyle style) {
        final InnerTopwear shirt = new InnerTopwear(UUID.randomUUID());
        shirt.setColor(color);
        shirt.setStyle(style);
        shirt.setCondition(WearCondition.NEW);
        return shirt;
    }

    private static Event event(String name, WearColor color, WearStyle style) {
        return new Event(
                name,
                OffsetDateTime.parse("2026-07-01T00:00:00-04:00"),
                OffsetDateTime.parse("2026-07-01T23:59:59-04:00"),
                List.of(color),
                List.of(style)
        );
    }

    private static RecommendationContext context(List<Event> events,
                                                 List<WearColor> colors,
                                                 List<WearStyle> styles) {
        return new RecommendationContext(
                new Weather(LocalDate.of(2026, 7, 1), "Clear", 20.0, 0.0, 0.0, 0.0, 0),
                events,
                colors,
                styles
        );
    }
}
