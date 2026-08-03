package use_case.recommendation_context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import entity.AbstractWear;

/**
 * Reduces a wardrobe slot to the few garments that best suit the context.
 *
 * <p>Ranking a slot on its own is sound only because an {@link ItemAnalyzer} scores a garment
 * without reference to the rest of the outfit: a garment that scores higher here raises the total
 * of every outfit it could appear in, so the garments left behind cannot have led anywhere better
 * on those criteria. Keeping this apart from the interactor lets the search be tuned, or replaced
 * outright, without disturbing the use case that drives it.
 */
public final class SlotNarrower {
    private final List<ItemAnalyzer> itemAnalyzers;
    private final int limit;

    /**
     * Constructs a narrower.
     *
     * @param itemAnalyzers the criteria to rank garments by
     * @param limit         the most garments to leave in any one slot
     */
    public SlotNarrower(List<ItemAnalyzer> itemAnalyzers, int limit) {
        this.itemAnalyzers = List.copyOf(itemAnalyzers);
        this.limit = limit;
    }

    /**
     * Keeps only the garments in a slot that best suit the context.
     *
     * <p>Ties fall back to fondness and then to the identifier, so the outcome never depends on
     * the order the wardrobe happened to be stored in.
     *
     * @param items   the eligible garments for one slot
     * @param context the current recommendation context
     * @param <T>     the type of the garments
     * @return the garments worth combining into outfits
     */
    public <T extends AbstractWear> List<T> narrow(List<T> items, RecommendationContext context) {
        if (items.size() <= limit) {
            return items;
        }

        final Map<UUID, ItemScore> scores = new HashMap<>();
        for (T item : items) {
            scores.put(item.getUuid(), score(item, context));
        }

        final List<T> ranked = new ArrayList<>(items);
        ranked.sort(Comparator
                .comparingInt((T item) -> scores.get(item.getUuid()).getEventMatches())
                .thenComparingInt(item -> scores.get(item.getUuid()).getPreferenceMatches())
                .thenComparingDouble(AbstractWear::getFondness)
                .reversed()
                .thenComparing(item -> item.getUuid().toString()));

        return List.copyOf(ranked.subList(0, limit));
    }

    /**
     * Checks whether a garment suits the context on any criterion at all.
     *
     * @param item    the garment
     * @param context the current recommendation context
     * @return true if the garment contributes to any criterion; otherwise, false
     */
    public boolean contributes(AbstractWear item, RecommendationContext context) {
        return score(item, context).contributes();
    }

    private ItemScore score(AbstractWear item, RecommendationContext context) {
        ItemScore total = ItemScore.none();
        for (ItemAnalyzer itemAnalyzer : itemAnalyzers) {
            total = total.plus(itemAnalyzer.analyze(item, context));
        }
        return total;
    }
}
