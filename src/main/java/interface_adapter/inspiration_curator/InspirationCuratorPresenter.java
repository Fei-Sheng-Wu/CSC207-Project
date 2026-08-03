package interface_adapter.inspiration_curator;

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
        // @TODO
    }

    @Override
    public void prepareFailView() {
        // @TODO
    }
}
