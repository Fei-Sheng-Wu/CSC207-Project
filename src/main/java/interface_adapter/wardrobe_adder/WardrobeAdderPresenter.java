package interface_adapter.wardrobe_adder;

import interface_adapter.item.ItemViewModel;
import use_case.wardrobe_adder.WardrobeAdderOutputBoundary;

/**
 * Presenter for the wardrobe adder use case.
 */
public class WardrobeAdderPresenter implements WardrobeAdderOutputBoundary {
    private final ItemViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeAdderPresenter(ItemViewModel viewModel) {
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
