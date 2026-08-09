package interface_adapter.inspiration_curator;

import java.util.ArrayList;

import interface_adapter.inspiration.InspirationViewModel;
import use_case.inspiration_curator.InspirationCuratorOutputBoundary;
import use_case.inspiration_curator.InspirationCuratorOutputData;

/**
 * Presenter for the wardrobe remover use case.
 */
public class InspirationCuratorPresenter implements InspirationCuratorOutputBoundary {
    private final InspirationViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public InspirationCuratorPresenter(InspirationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(InspirationCuratorOutputData output) {
        viewModel.setIdeas(output.getOutfitIdeas());
        viewModel.setError(null);
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setIdeas(new ArrayList<>());
        viewModel.setError(error);
    }
}
