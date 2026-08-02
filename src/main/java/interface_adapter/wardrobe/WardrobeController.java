package interface_adapter.wardrobe;

import use_case.wardrobe_reporter.WardrobeReporterInputBoundary;

public class WardrobeController {

    private final WardrobeReporterInputBoundary wardrobeReporterInteractor;

    public WardrobeController(WardrobeReporterInputBoundary wardrobeReporterInteractor) {
        this.wardrobeReporterInteractor = wardrobeReporterInteractor;
    }

    /**
     * Executes the Wardrobe Use Case.
     */
    public void execute() {
        wardrobeReporterInteractor.report();
    }
}
