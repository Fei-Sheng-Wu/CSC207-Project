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
     * @param sortBy the sorting by criteria
     */
    public void sortWardrobe(String sortBy) {
        WardrobeSort sort = null;
        for (WardrobeSort sortOption : WardrobeSort.values()) {
            if (!sortOption.getDisplayName().equals(sortBy)) {
                continue;
            }

            sort = sortOption;
            break;
        }

        final WardrobeSorterInputData inputData = new WardrobeSorterInputData(sort);
        interactor.sortWardrobe(inputData);
    }
}
