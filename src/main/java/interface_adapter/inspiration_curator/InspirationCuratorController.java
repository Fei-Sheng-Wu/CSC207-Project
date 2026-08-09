package interface_adapter.inspiration_curator;

import entity.AbstractWear;
import use_case.inspiration_curator.InspirationCuratorInputBoundary;
import use_case.inspiration_curator.InspirationCuratorInputData;

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

    /**
     * Curates inspiration.
     *
     * @param wear the clothing item
     */
    public void curate(AbstractWear wear) {
        interactor.curate(new InspirationCuratorInputData(wear));
    }
}
