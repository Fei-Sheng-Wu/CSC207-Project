package interface_adapter.recommendation_context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.WearColor;
import entity.WearStyle;
import use_case.recommendation_context.ContextBasedRecommendationInputBoundary;
import use_case.recommendation_context.ContextBasedRecommendationInputData;

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
public class ContextBasedRecommendationController {
    private final ContextBasedRecommendationInputBoundary interactor;
    private final Random random;

    /**
     * Constructs a new recommendation controller.
     *
     * @param interactor the use case to invoke
     * @param random     the source of tie-breaking seeds
     */
    public ContextBasedRecommendationController(ContextBasedRecommendationInputBoundary interactor, Random random) {
        this.interactor = interactor;
        this.random = random;
    }

    /**
     * Requests a recommendation for the supplied preferences.
     *
     * @param preferredColors the colors the user prefers, possibly empty
     * @param preferredStyles the styles the user prefers, possibly empty
     */
    public void recommend(List<String> preferredColors, List<String> preferredStyles) {
        final List<WearColor> colors = new ArrayList<>();
        for (String text : preferredColors) {
            colors.add(WearColor.valueOf(text.strip().toUpperCase()));
        }

        final List<WearStyle> styles = new ArrayList<>();
        for (String text : preferredStyles) {
            styles.add(WearStyle.valueOf(text.strip().toUpperCase()));
        }

        interactor.recommend(new ContextBasedRecommendationInputData(random.nextInt(), colors, styles));
    }
}
