package use_case.recommendation_context;

import java.util.EnumSet;
import java.util.Set;

import entity.AbstractWear;
import entity.Event;
import entity.WearColor;
import entity.WearStyle;

/**
 * Scores a garment's attributes against the events happening today.
 */
public final class EventItemAnalyzer implements ItemAnalyzer {
    @Override
    public ItemScore analyze(AbstractWear item, RecommendationContext context) {
        final Set<WearColor> eventColors = EnumSet.noneOf(WearColor.class);
        final Set<WearStyle> eventStyles = EnumSet.noneOf(WearStyle.class);
        for (Event event : context.getEvents()) {
            eventColors.addAll(event.getWearColors());
            eventStyles.addAll(event.getWearStyles());
        }

        int matches = 0;
        if (item.getColor() != null && eventColors.contains(item.getColor())) {
            matches++;
        }
        if (item.getStyle() != null && eventStyles.contains(item.getStyle())) {
            matches++;
        }

        return new ItemScore(matches, 0);
    }
}
