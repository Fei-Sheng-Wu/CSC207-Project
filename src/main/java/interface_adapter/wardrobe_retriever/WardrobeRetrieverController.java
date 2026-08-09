package interface_adapter.wardrobe_retriever;

import use_case.wardrobe_retriever.WardrobeRetrieverInputBoundary;

/**
 * Controller for retrieving all wardrobe items.
 */
public class WardrobeRetrieverController {
    private final WardrobeRetrieverInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeRetrieverController(WardrobeRetrieverInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the ret wardrobe use case.
     */
    public void retrieveWardrobe() {
        interactor.retrieve();
    }
}
