package interface_adapter.recommendation;

import java.util.List;
import java.util.Random;

import entity.WearColor;
import entity.WearStyle;
import use_case.context_based_recommendation.ContextBasedRecommendationInputBoundary;
import use_case.context_based_recommendation.ContextBasedRecommendationInputData;

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
public class RecommendationController {
    private final ContextBasedRecommendationInputBoundary inputBoundary;
    private final Random random;

    /**
     * Constructs a new recommendation controller.
     *
     * @param inputBoundary the use case to invoke
     * @param random the source of tie-breaking seeds
     */
    public RecommendationController(ContextBasedRecommendationInputBoundary inputBoundary, Random random) {
        this.inputBoundary = inputBoundary;
        this.random = random;
    }

    /**
     * Requests a recommendation for the supplied preferences.
     *
     * @param preferredColors the colors the user prefers, possibly empty
     * @param preferredStyles the styles the user prefers, possibly empty
     */
    public void recommend(List<WearColor> preferredColors, List<WearStyle> preferredStyles) {
        inputBoundary.recommend(new ContextBasedRecommendationInputData(
            random.nextInt(),
            preferredColors,
            preferredStyles
        ));
    }
}
