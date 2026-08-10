package interface_adapter.wardrobe_filterer;

import use_case.wardrobe_filterer.WardrobeFiltererInputBoundary;
import use_case.wardrobe_filterer.WardrobeFiltererInputData;

/**
 * Controller for removing wardrobe items.
 */
public class WardrobeFiltererController {
    private final WardrobeFiltererInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeFiltererController(WardrobeFiltererInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the filter wardrobe use case.
     *
     * @param category     the category by which to filter the wardrobe
     * @param condition    the condition by which to filter the wardrobe
     * @param name         the name by which to filter the wardrobe
     * @param purchaseDate the purchase date/months limit by which to filter the wardrobe
     * @param tag          the tag/occasion by which to filter the wardrobe
     */
    public void filterWardrobe(
        String name,
        String category,
        String condition,
        int purchaseDate,
        String tag
    ) {
        final WardrobeFiltererInputData filteringModel = new WardrobeFiltererInputData(
            name,
            category,
            condition,
            purchaseDate,
            tag
        );
        interactor.filterItems(filteringModel);
    }
}
