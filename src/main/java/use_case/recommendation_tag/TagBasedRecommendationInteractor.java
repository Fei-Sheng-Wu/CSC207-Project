package use_case.recommendation_tag;

import entity.*;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;
import use_case.wardrobe.WardrobeDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates outfits based on the recommendations of color, style and tags.
 */
public class TagBasedRecommendationInteractor implements TagBasedRecommendationInputBoundary {
    private static final int TAG_MATCH_SCORE = 1;
    private static final int COLOR_MATCH_SCORE = 1;
    private static final int STYLE_MATCH_SCORE = 1;
    // an item with a score below MINIMAL_ITEM_SCORE is considered not adequate
    private static final int MINIMAL_ITEM_SCORE = 1;
    private static final String MISSING_REQUIRED_ITEMS =
        "A recommendation requires at least one inner topwear, bottomwear, and footwear.";
    // benchmark for including an accessory
    private static final int MINIMAL_ACCESSORY_SCORE = 3;

    private final WardrobeDataAccessInterface wardrobe;
    private final RecommendationOutputBoundary output;

    public TagBasedRecommendationInteractor(
        WardrobeDataAccessInterface wardrobe,
        RecommendationOutputBoundary output
    ) {
        this.wardrobe = wardrobe;
        this.output = output;
    }

    /**
     * Creates a recommendation based on how closely it ties to the user's preference
     *
     * @param request the user's preferences
     */
    public void recommend(TagBasedRecommendationInputData request) {
        final List<AbstractWear> items = wardrobe.fetchWardrobe().getItems();

        InnerTopwear currInnerTopwear = null;
        OuterTopwear currOuterTopwear = null;
        Bottomwear currBottomwear = null;
        Footwear currFootwear = null;
        Headwear currHeadwear = null;

        final List<Accessory> accessories = new ArrayList<>();
        // look through wardrobe, and for each item in the wardrobe, use compareTo
        for (AbstractWear item : items) {
            if (item instanceof Accessory accessory) {
                accessories.add(accessory);
            } else if (item instanceof InnerTopwear innerTopwear) {
                if (isBetter(innerTopwear, currInnerTopwear, request)) {
                    currInnerTopwear = innerTopwear;
                }
            } else if (item instanceof OuterTopwear outerTopwear) {
                if (isBetter(outerTopwear, currOuterTopwear, request)) {
                    currOuterTopwear = outerTopwear;
                }
            } else if (item instanceof Bottomwear bottomwear) {
                if (isBetter(bottomwear, currBottomwear, request)) {
                    currBottomwear = bottomwear;
                }
            } else if (item instanceof Footwear footwear) {
                if (isBetter(footwear, currFootwear, request)) {
                    currFootwear = footwear;
                }
            } else if (item instanceof Headwear headwear) {
                if (isBetter(headwear, currHeadwear, request)) {
                    currHeadwear = headwear;
                }
            }
        }

        final boolean missingOutfit =
            currInnerTopwear == null
                || currBottomwear == null
                || currFootwear == null;

        if (missingOutfit) {
            output.prepareFailView(MISSING_REQUIRED_ITEMS);
            return;
        }

        // an outfit is inadequate if it's too unrelated to the user's desire.
        final boolean inadequateOutfit =
            calculateScore(currInnerTopwear, request) < MINIMAL_ITEM_SCORE
                || calculateScore(currBottomwear, request) < MINIMAL_ITEM_SCORE
                || calculateScore(currFootwear, request) < MINIMAL_ITEM_SCORE;

        if (inadequateOutfit) {
            output.prepareFailView(
                "No complete outfit sufficiently matches the requested preferences."
            );
            return;
        }

        // Optional items are excluded when they do not match enough preferences.
        if (currOuterTopwear != null
            && calculateScore(currOuterTopwear, request) < MINIMAL_ITEM_SCORE) {
            currOuterTopwear = null;
        }

        if (currHeadwear != null
            && calculateScore(currHeadwear, request) < MINIMAL_ITEM_SCORE) {
            currHeadwear = null;
        }

        final List<Accessory> bestAccessories = new ArrayList<>();
        for (Accessory accessory : accessories) {
            if (calculateScore(accessory, request) >= MINIMAL_ACCESSORY_SCORE) {
                bestAccessories.add(accessory);
            }
        }

        final Outfit outfit = new Outfit(
            currInnerTopwear,
            currOuterTopwear,
            currBottomwear,
            currFootwear,
            currHeadwear,
            bestAccessories
        );

        String response = createResponse(request);

        output.prepareSuccessView(new RecommendationOutputData(outfit, response));
    }

    /**
     * Creates a response String to accompany the outfit
     *
     * @param request the user's preferences
     * @return response the response
     */
    private String createResponse(TagBasedRecommendationInputData request) {
        String response = "";
        response += "Based on your preferences ";

        if (!request.getPreferredColors().isEmpty()) {
            response += "including colors: ";
            for (WearColor color : request.getPreferredColors()) {
                response += (color.getDisplayName() + ", ");
            }
        }

        if (!request.getPreferredStyles().isEmpty()) {
            response += "including styles: ";
            for (WearStyle style : request.getPreferredStyles()) {
                response += (style.getDisplayName() + ", ");
            }

        }
        if (!request.getPreferredTags().isEmpty()) {
            response += "including tags: ";
            for (String tag : request.getPreferredTags()) {
                response += (tag + ", ");
            }
        }

        return response;
    }

    /**
     * Compare two candidate items and choose the better one based on user preferences
     *
     * @param candidate the new candidate clothing
     * @param current   the clothing in question
     * @param request   user preferences
     * @return true if the new candidate is better, false otherwise
     */
    private boolean isBetter(AbstractWear candidate, AbstractWear current, TagBasedRecommendationInputData request) {
        if (current == null) {
            return true;
        } else {
            return calculateScore(candidate, request) > calculateScore(current, request);
        }
    }

    /**
     * benchmark to measure how closely a clothing item ties to user preferences
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
