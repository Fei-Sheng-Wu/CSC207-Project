package use_case.recommendation_context;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import entity.AbstractWear;
import entity.Event;
import entity.Outfit;

/**
 * Scores outfit attributes that match current events.
 *
 * <p>The rule itself lives in {@link EventItemAnalyzer}; this class only sums that rule over the
 * outfit's garments and names the events responsible. Narrowing and final scoring therefore ask
 * the same object what an event match is, and cannot drift apart.
 */
public final class EventOutfitAnalyzer implements OutfitAnalyzer {
    private final ItemAnalyzer itemAnalyzer = new EventItemAnalyzer();

    @Override
    public OutfitAnalysis analyze(Outfit outfit, RecommendationContext context) {
        int matches = 0;
        for (AbstractWear item : outfit.toList()) {
            matches += itemAnalyzer.analyze(item, context).getEventMatches();
        }

        if (matches == 0) {
            return OutfitAnalysis.neutral();
        }

        final Set<String> eventNames = new TreeSet<>();
        for (Event event : context.getEvents()) {
            eventNames.add(event.getName());
        }
        final String reason = String.format(
                "%d clothing attributes match the current event context: %s.",
                matches,
                String.join(", ", eventNames)
        );
        return new OutfitAnalysis(true, matches, 0, 0.0, List.of(reason));
    }
}
