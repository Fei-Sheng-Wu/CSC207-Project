package interface_adapter.wardrobe_reporter;

import java.util.ArrayList;

import interface_adapter.wardrobe.WardrobeViewModel;
import use_case.wardrobe_reporter.WardrobeReporterOutputBoundary;
import use_case.wardrobe_reporter.WardrobeReporterOutputData;

/**
 * Presenter for the wardrobe remover use case.
 */
public class WardrobeReporterPresenter implements WardrobeReporterOutputBoundary {
    private final WardrobeViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeReporterPresenter(WardrobeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WardrobeReporterOutputData outputData) {
        viewModel.setItems(outputData.getWearsAll());
        viewModel.setOldItems(outputData.getWearsOld());
        viewModel.setError(null);
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setItems(new ArrayList<>());
        viewModel.setOldItems(new ArrayList<>());
        viewModel.setError(message);
    }
}
