package interface_adapter.wardrobe_analyzer;

import use_case.wardrobe_analyzer.WardrobeAnalyzerInputBoundary;

public class WardrobeAnalyzerController {
    private final WardrobeAnalyzerInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeAnalyzerController(WardrobeAnalyzerInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the wardrobe use case.
     */
    public void analyzeWardrobe() {
        interactor.analyze();
    }
}
