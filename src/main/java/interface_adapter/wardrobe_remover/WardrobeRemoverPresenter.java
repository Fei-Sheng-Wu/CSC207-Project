package interface_adapter.wardrobe_remover;

import interface_adapter.item.ItemViewModel;
import use_case.wardrobe_remover.WardrobeRemoverOutputBoundary;

/**
 * Presenter for the wardrobe remover use case.
 */
public class WardrobeRemoverPresenter implements WardrobeRemoverOutputBoundary {
    private final ItemViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeRemoverPresenter(ItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView() {
        viewModel.setError(null);
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setError(message);
    }
}
