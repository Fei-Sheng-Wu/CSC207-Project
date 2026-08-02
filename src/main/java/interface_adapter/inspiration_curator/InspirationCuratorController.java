package interface_adapter.inspiration_curator;

import use_case.inspiration_curator.InspirationCuratorInputBoundary;

/**
 * Controller for adding wardrobe items.
 */
public class InspirationCuratorController {
    private final InspirationCuratorInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public InspirationCuratorController(InspirationCuratorInputBoundary interactor) {
        this.interactor = interactor;
    }
}
