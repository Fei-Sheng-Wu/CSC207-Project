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
        final WardrobeAnalyzerState state = new WardrobeAnalyzerState();

        final String formattedFondness = String.format("%.2f", outputData.getAverageFondness());
        state.setTotalItemsCount(outputData.getTotalItems());
        state.setAverageFondnessString(formattedFondness + "/100");
        state.setDonationCandidateCount(outputData.getDonationCandidateCount());
        state.setOldestItemAge(outputData.getOldestItemAge() + " months");
        state.setNewestItemAge(outputData.getNewestItemAge() + " months");
        state.setCategoryDistribution(outputData.getCategoryCounts());
        state.setConditionDistribution(outputData.getConditionCounts());

        viewModel.setAnalyzerState(state);
    }

    @Override
    public void prepareFailView(String error) {
        final WardrobeAnalyzerState state = new WardrobeAnalyzerState();
        state.setError(error);
        viewModel.setAnalyzerState(state);
    }
}
