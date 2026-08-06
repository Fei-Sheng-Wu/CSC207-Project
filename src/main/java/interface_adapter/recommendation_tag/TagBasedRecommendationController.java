package interface_adapter.recommendation_tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.WearColor;
import entity.WearStyle;
import use_case.recommendation_tag.TagBasedRecommendationInputBoundary;
import use_case.recommendation_tag.TagBasedRecommendationInputData;

/**
 * Converts a request for a recommendation from the view into use case input data.
 *
 * <p>The field is typed as the input boundary rather than as the interactor, so this controller
 * can call exactly one method and knows nothing about how a recommendation is produced.
 *
 * <p>The interactor breaks ties between equally ranked outfits using a seed. That is an
 * application concern rather than something a user chooses, so the seed is drawn here from an
 * injected {@link Random}: the view never learns that seeds exist, and a test can pass a seeded
 * instance to make the whole chain reproducible.
 */
public class TagBasedRecommendationController {
    private final TagBasedRecommendationInputBoundary interactor;
    private final Random random;

    /**
     * Constructs a new recommendation controller.
     *
     * @param interactor the use case to invoke
     * @param random     the source of tie-breaking seeds
     */
    public TagBasedRecommendationController(TagBasedRecommendationInputBoundary interactor, Random random) {
        this.interactor = interactor;
        this.random = random;
    }

    /**
     * Requests a recommendation for the supplied preferences.
     *
     * @param preferredColors the colors the user prefers, possibly empty
     * @param preferredStyles the styles the user prefers, possibly empty
     * @param preferredTags   the styles the user prefers, possibly empty
     */
    public void recommend(List<String> preferredColors, List<String> preferredStyles, List<String> preferredTags) {
        final List<WearColor> colors = new ArrayList<>();
        for (String text : preferredColors) {
            colors.add(WearColor.valueOf(text.strip().toUpperCase()));
        }

        final List<WearStyle> styles = new ArrayList<>();
        for (String text : preferredStyles) {
            styles.add(WearStyle.valueOf(text.strip().toUpperCase()));
        }

        final List<String> tags = new ArrayList<>();
        for (String text : preferredTags) {
            tags.add(text.strip());
        }

        interactor.recommend(new TagBasedRecommendationInputData(random.nextInt(), colors, styles, tags));
    }
}
