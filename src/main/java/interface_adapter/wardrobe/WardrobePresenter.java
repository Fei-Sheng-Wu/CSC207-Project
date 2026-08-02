package interface_adapter.wardrobe;

import use_case.wardrobe_reporter.WardrobeReporterOutputBoundary;
import use_case.wardrobe_reporter.WardrobeReporterOutputData;
import view.ApplicationManager;

public class WardrobePresenter implements WardrobeReporterOutputBoundary {
    private final WardrobeViewModel wardrobeViewModel;

    public WardrobePresenter(WardrobeViewModel wardrobeViewModel,
                             ApplicationManager applicationManager) {
        this.wardrobeViewModel = wardrobeViewModel;
    }

    @Override
    public void prepareSuccessView(WardrobeReporterOutputData outputData) {

        WardrobeState wardrobeState = new WardrobeState();

        wardrobeState.setTotalClothesCount(outputData.getWearsAll().size());
        wardrobeState.setOldClothesCount(outputData.getWearsOld().size());
        wardrobeState.setLaundryNeededCount(outputData.getWearsLaundryNeeded().size());

        this.wardrobeViewModel.setWardrobeState(wardrobeState);

        this.wardrobeViewModel.firePropertyChange("state", wardrobeState);
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println(">>> There is an ERROR: " + error);

        WardrobeState wardrobeState = new WardrobeState();

        wardrobeState.setReportError(error);

        this.wardrobeViewModel.setWardrobeState(wardrobeState);
        this.wardrobeViewModel.firePropertyChange("state", wardrobeState);
    }
}
