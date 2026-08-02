package interface_adapter.recommendation;

import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;

/**
 * Presents the result of a recommendation by updating the view model.
 *
 * <p>This class implements an interface declared in the use case layer, which is what lets the
 * interactor hand its result outward without ever depending on the view. Swapping this presenter
 * for a console or test one requires no change to the interactor at all.
 */
public class RecommendationPresenter implements RecommendationOutputBoundary {
    private final RecommendationViewModel viewModel;

    /**
     * Constructs a new recommendation presenter.
     *
     * @param viewModel the view model to update
     */
    public RecommendationPresenter(RecommendationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RecommendationOutputData outputData) {
        viewModel.setRecommendation(outputData.getOutfit(), outputData.getReason());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}
