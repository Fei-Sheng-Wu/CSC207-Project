package interface_adapter.wardrobe_analyzer;

import interface_adapter.wardrobe.WardrobeViewModel;
import use_case.wardrobe_analyzer.WardrobeAnalyzerOutputBoundary;
import use_case.wardrobe_analyzer.WardrobeAnalyzerOutputData;

public class WardrobeAnalyzerPresenter implements WardrobeAnalyzerOutputBoundary {

    private final WardrobeViewModel viewModel;

    /**
     * Constructs a new presenter.
     *
     * @param viewModel the view model of the presenter
     */
    public WardrobeAnalyzerPresenter(WardrobeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WardrobeAnalyzerOutputData outputData) {
        viewModel.setAnalyzerStatistics(outputData.getStatistics());
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setError(error);
    }
}
