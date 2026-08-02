package interface_adapter.wardrobe_reporter;

import use_case.wardrobe_reporter.WardrobeReporterInputBoundary;

/**
 * Controller for removing wardrobe items.
 */
public class WardrobeReporterController {
    private final WardrobeReporterInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeReporterController(WardrobeReporterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the report wardrobe use case.
     */
    public void reportWardrobe() {
        interactor.report();
    }
}
