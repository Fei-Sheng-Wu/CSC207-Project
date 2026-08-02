package use_case.recommendation_context;

import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

import entity.AbstractWear;
import entity.Event;
import entity.Outfit;
import entity.WearColor;
import entity.WearStyle;

/**
 * Scores outfit attributes that match current events.
 */
public final class EventOutfitAnalyzer implements OutfitAnalyzer {
    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        final Set<WearColor> eventColors = EnumSet.noneOf(WearColor.class);
        final Set<WearStyle> eventStyles = EnumSet.noneOf(WearStyle.class);
        final Set<String> eventNames = new TreeSet<>();
        for (Event event : context.getEvents()) {
            eventColors.addAll(event.getWearColors());
            eventStyles.addAll(event.getWearStyles());
            eventNames.add(event.getName());
        }

        int matches = 0;
        for (AbstractWear item : outfit.toList()) {
            if (eventColors.contains(item.getColor())) {
                matches++;
            }
            if (eventStyles.contains(item.getStyle())) {
                matches++;
            }
        }

        if (matches == 0) {
            return OutfitAnalysis.neutral();
        }
        final String reason = String.format(
                "%d clothing attributes match the current event context: %s.",
                matches,
                String.join(", ", eventNames)
        );
        return new OutfitAnalysis(true, matches, 0, 0.0, java.util.List.of(reason));
    }
}
