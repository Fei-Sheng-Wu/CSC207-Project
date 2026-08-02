package interface_adapter.recommendation;

import entity.Outfit;
import interface_adapter.AbstractViewModel;

/**
 * Represents the recommendation view model.
 *
 * <p>Holds the state a recommendation screen renders, and notifies its listeners when that state
 * changes. A recommendation and an error are mutually exclusive: presenting one clears the other,
 * so the view never has to decide which of two populated fields to trust.
 */
public class RecommendationViewModel extends AbstractViewModel {
    /**
     * The property fired when a recommendation is presented.
     */
    public static final String PROPERTY_RECOMMENDATION = "recommendation";

    /**
     * The property fired when a failure is presented.
     */
    public static final String PROPERTY_ERROR_MESSAGE = "errorMessage";

    private Outfit outfit;
    private String reason = "";
    private String errorMessage = "";

    /**
     * Returns the recommended outfit, or null when no recommendation has been presented.
     *
     * @return the recommended outfit
     */
    public Outfit getOutfit() {
        return outfit;
    }

    /**
     * Returns the explanation of the recommendation.
     *
     * @return the explanation of the recommendation
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the explanation of the most recent failure.
     *
     * @return the explanation of the most recent failure
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Updates the view model with a successful recommendation, clearing any previous error.
     *
     * @param recommendedOutfit the recommended outfit
     * @param recommendationReason the explanation of the recommendation
     */
    public void setRecommendation(Outfit recommendedOutfit, String recommendationReason) {
        outfit = recommendedOutfit;
        reason = recommendationReason;
        errorMessage = "";
        firePropertyChange(PROPERTY_RECOMMENDATION, recommendedOutfit);
    }

    /**
     * Updates the view model with a failure, clearing any previous recommendation.
     *
     * @param message the explanation of the failure
     */
    public void setErrorMessage(String message) {
        outfit = null;
        reason = "";
        errorMessage = message;
        firePropertyChange(PROPERTY_ERROR_MESSAGE, message);
    }
}
