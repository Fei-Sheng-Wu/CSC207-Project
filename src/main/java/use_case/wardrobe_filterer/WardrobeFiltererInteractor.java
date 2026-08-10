package use_case.wardrobe_filterer;

import java.util.ArrayList;
import java.util.List;

import entity.AbstractWear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class WardrobeFiltererInteractor implements WardrobeFiltererInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeFiltererOutputBoundary outputBoundary;

    public WardrobeFiltererInteractor(
        WardrobeDataAccessInterface wardrobeDataAccessInterface,
        WardrobeFiltererOutputBoundary wardrobeFiltererOutputBoundary
    ) {
        this.repository = wardrobeDataAccessInterface;
        this.outputBoundary = wardrobeFiltererOutputBoundary;
    }

    /**
     * Filters the items in the wardrobe based on the provided filtering criteria.
     *
     * @param filteringCriteria the criteria containing the user's filter preferences
     */
    public void filterItems(WardrobeFiltererInputData filteringCriteria) {
        final Wardrobe wardrobe = repository.fetchWardrobe();

        if (wardrobe == null || wardrobe.getItems() == null) {
            outputBoundary.prepareFailView("Failed to load wardrobe data for filtering.");
            return;
        }

        final List<AbstractWear> allItems = wardrobe.getItems();
        final List<AbstractWear> filteredAll = new ArrayList<>();
        final List<WardrobeFilter> filters = FilterFactory.createFilters();

        for (AbstractWear wear : allItems) {
            boolean matchesAll = true;
            for (WardrobeFilter filter : filters) {
                if (!filter.matches(wear, filteringCriteria)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                filteredAll.add(wear);
            }
        }

        outputBoundary.prepareSuccessView(new WardrobeFiltererOutputData(
            filteredAll
        ));
    }

}
