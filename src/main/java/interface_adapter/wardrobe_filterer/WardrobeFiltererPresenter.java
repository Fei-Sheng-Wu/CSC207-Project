package interface_adapter.wardrobe_filterer;

import interface_adapter.wardrobe.WardrobeViewModel;
import use_case.wardrobe_filterer.WardrobeFiltererOutputBoundary;
import use_case.wardrobe_filterer.WardrobeFiltererOutputData;

import java.util.ArrayList;

public class WardrobeFiltererPresenter implements WardrobeFiltererOutputBoundary {
    private final WardrobeViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeFiltererPresenter(WardrobeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WardrobeFiltererOutputData outputData) {
        viewModel.setItems(outputData.getFilteredItems());
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setItems(new ArrayList<>());
    }
}
