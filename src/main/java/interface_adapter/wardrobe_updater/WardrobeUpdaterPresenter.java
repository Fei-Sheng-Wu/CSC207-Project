package interface_adapter.wardrobe_updater;

import interface_adapter.item.ItemViewModel;
import use_case.wardrobe_updater.WardrobeUpdaterOutputBoundary;

/**
 * Presenter for the wardrobe updater use case.
 */
public class WardrobeUpdaterPresenter implements WardrobeUpdaterOutputBoundary {
    private final ItemViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeUpdaterPresenter(ItemViewModel viewModel) {
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
