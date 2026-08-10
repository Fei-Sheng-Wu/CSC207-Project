package use_case.recommendation_tag;

import java.util.ArrayList;
import java.util.List;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Footwear;
import entity.Headwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Outfit;
import entity.WearColor;
import entity.WearStyle;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Generates outfits based on the recommendations of color, style and tags.
 */
public class TagBasedRecommendationInteractor implements TagBasedRecommendationInputBoundary {
    private static final int TAG_MATCH_SCORE = 1;
    private static final int COLOR_MATCH_SCORE = 1;
    private static final int STYLE_MATCH_SCORE = 1;
    // An item with a score below MINIMAL_ITEM_SCORE is considered not adequate.
    private static final int MINIMAL_ITEM_SCORE = 1;
    // Benchmark for including an accessory.
    private static final int MINIMAL_ACCESSORY_SCORE = 3;
    private static final String MISSING_REQUIRED_ITEMS =
        "A recommendation requires at least one inner topwear, bottomwear, and footwear.";
    private static final String NO_SUFFICIENT_MATCH =
        "No complete outfit sufficiently matches the requested preferences.";
    private static final String SEPARATOR = ", ";

    private final WardrobeDataAccessInterface wardrobe;
    private final RecommendationOutputBoundary output;

    public TagBasedRecommendationInteractor(
        WardrobeDataAccessInterface wardrobe,
        RecommendationOutputBoundary output
    ) {
        this.wardrobe = wardrobe;
        this.output = output;
    }

    @Override
    public void recommend(TagBasedRecommendationInputData request) {
        final List<AbstractWear> items = wardrobe.fetchWardrobe().getItems();
        final InnerTopwear innerTopwear = bestOfType(items, InnerTopwear.class, request);
        final Bottomwear bottomwear = bestOfType(items, Bottomwear.class, request);
        final Footwear footwear = bestOfType(items, Footwear.class, request);

        if (innerTopwear == null || bottomwear == null || footwear == null) {
            output.prepareFailView(MISSING_REQUIRED_ITEMS);
            return;
        }

        // An outfit is inadequate if it is too unrelated to the user's desire.
        if (isInadequate(innerTopwear, request)
            || isInadequate(bottomwear, request)
            || isInadequate(footwear, request)) {
            output.prepareFailView(NO_SUFFICIENT_MATCH);
            return;
        }

        final Outfit outfit = new Outfit(
            innerTopwear,
            adequateOrNothing(bestOfType(items, OuterTopwear.class, request), request),
            bottomwear,
            footwear,
            adequateOrNothing(bestOfType(items, Headwear.class, request), request),
            bestAccessories(items, request)
        );

        output.prepareSuccessView(new RecommendationOutputData(outfit, createResponse(request)));
    }

    /**
     * Returns the clothing item of a type that best matches the user's preferences.
     *
     * @param items   the clothing items in the wardrobe
     * @param type    the type of clothing item to choose between
     * @param request the user's preferences
     * @param <T>     the type of clothing item to choose between
     * @return the best clothing item of the type, or null if the wardrobe holds none
     */
    private <T extends AbstractWear> T bestOfType(List<AbstractWear> items,
                                                  Class<T> type,
                                                  TagBasedRecommendationInputData request) {
        T best = null;
        for (AbstractWear item : items) {
            if (type.isInstance(item) && isBetter(type.cast(item), best, request)) {
                best = type.cast(item);
            }
        }

        return best;
    }

    /**
     * Drops an optional clothing item that does not match enough preferences to be worth wearing.
     *
     * @param item    the optional clothing item, possibly null
     * @param request the user's preferences
     * @param <T>     the type of the clothing item
     * @return the clothing item if it is worth wearing; otherwise, null
     */
    private <T extends AbstractWear> T adequateOrNothing(T item, TagBasedRecommendationInputData request) {
        T result = item;
        if (item != null && isInadequate(item, request)) {
            result = null;
        }

        return result;
    }

    /**
     * Returns the accessories that match the user's preferences closely enough to be worn.
     *
     * @param items   the clothing items in the wardrobe
     * @param request the user's preferences
     * @return the accessories worth wearing
     */
    private List<Accessory> bestAccessories(List<AbstractWear> items,
                                            TagBasedRecommendationInputData request) {
        final List<Accessory> bestAccessories = new ArrayList<>();
        for (AbstractWear item : items) {
            if (item instanceof Accessory accessory
                && calculateScore(accessory, request) >= MINIMAL_ACCESSORY_SCORE) {
                bestAccessories.add(accessory);
            }
        }

        return bestAccessories;
    }

    /**
     * Checks whether a clothing item is too unrelated to the user's preferences to be worn.
     *
     * @param item    the clothing item in question
     * @param request the user's preferences
     * @return true if the clothing item is inadequate; otherwise, false
     */
    private boolean isInadequate(AbstractWear item, TagBasedRecommendationInputData request) {
        return calculateScore(item, request) < MINIMAL_ITEM_SCORE;
    }

    /**
     * Creates a response String to accompany the outfit.
     *
     * @param request the user's preferences
     * @return the response
     */
    private String createResponse(TagBasedRecommendationInputData request) {
        final List<String> colors = new ArrayList<>();
        for (WearColor color : request.getPreferredColors()) {
            colors.add(color.getDisplayName());
        }

        final List<String> styles = new ArrayList<>();
        for (WearStyle style : request.getPreferredStyles()) {
            styles.add(style.getDisplayName());
        }

        final StringBuilder response = new StringBuilder("Based on your preferences ");
        appendPreferences(response, "colors", colors);
        appendPreferences(response, "styles", styles);
        appendPreferences(response, "tags", request.getPreferredTags());

        return response.toString();
    }

    /**
     * Appends one kind of preference to the response, if the user expressed any.
     *
     * @param response the response being built
     * @param label    the name of the kind of preference
     * @param values   the preferences the user expressed
     */
    private static void appendPreferences(StringBuilder response, String label, List<String> values) {
        if (!values.isEmpty()) {
            response.append("including ").append(label).append(": ").append(String.join(SEPARATOR, values));
        }
    }

    /**
     * Compares two candidate items and chooses the better one based on user preferences.
     *
     * @param candidate the new candidate clothing
     * @param current   the clothing in question
     * @param request   user preferences
     * @return true if the new candidate is better, false otherwise
     */
    private boolean isBetter(AbstractWear candidate, AbstractWear current, TagBasedRecommendationInputData request) {
        return current == null || calculateScore(candidate, request) > calculateScore(current, request);
    }

    /**
     * Measures how closely a clothing item ties to user preferences.
     *
     * @param item    item in question
     * @param request user preferences
     * @return score
     */
    private int calculateScore(AbstractWear item, TagBasedRecommendationInputData request) {
        int score = 0;
        for (String preferredTag : request.getPreferredTags()) {
            for (String itemTag : item.getTags()) {
                if (preferredTag.equalsIgnoreCase(itemTag)) {
                    score += TAG_MATCH_SCORE;
                }
            }
        }

        if (request.getPreferredColors().contains(item.getColor())) {
            score += COLOR_MATCH_SCORE;
        }

        if (request.getPreferredStyles().contains(item.getStyle())) {
            score += STYLE_MATCH_SCORE;
        }

        return score;
    }
}
