package interface_adapter.wardrobe_sorter;

import entity.WardrobeSort;
import use_case.wardrobe_sorter.WardrobeSorterInputBoundary;
import use_case.wardrobe_sorter.WardrobeSorterInputData;

public class WardrobeSorterController {
    private final WardrobeSorterInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeSorterController(WardrobeSorterInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the sort items use case.
     *
     * @param sortBy the display name of the sorting by criteria
     */
    public void sortWardrobe(String sortBy) {
        WardrobeSort sort = WardrobeSort.TYPE;
        for (int i = 0; i < WardrobeSort.values().length; i++) {
            if (WardrobeSort.values()[i].getDisplayName() == sortBy) {
                sort = WardrobeSort.values()[i];
            }
        }

        final WardrobeSorterInputData inputData = new WardrobeSorterInputData(sort);
        interactor.sortWardrobe(inputData);
    }
}
