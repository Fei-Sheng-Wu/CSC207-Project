package interface_adapter.wardrobe_retriever;

import java.util.ArrayList;

import interface_adapter.wardrobe.WardrobeViewModel;
import use_case.wardrobe_retriever.WardrobeRetrieverOutputBoundary;
import use_case.wardrobe_retriever.WardrobeRetrieverOutputData;

/**
 * Presenter for the wardrobe remover use case.
 */
public class WardrobeRetrieverPresenter implements WardrobeRetrieverOutputBoundary {
    private final WardrobeViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeRetrieverPresenter(WardrobeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WardrobeRetrieverOutputData outputData) {
        viewModel.setItems(outputData.getWearsAll());
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setItems(new ArrayList<>());
    }
}
