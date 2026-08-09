package use_case.wardrobe_sorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import entity.AbstractWear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class WardrobeSorterInteractor implements WardrobeSorterInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeSorterOutputBoundary outputBoundary;

    public WardrobeSorterInteractor(
        WardrobeDataAccessInterface repository,
        WardrobeSorterOutputBoundary outputBoundary
    ) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void sortWardrobe(WardrobeSorterInputData inputData) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final List<AbstractWear> items = new ArrayList<>(wardrobe.getItems());

        final Comparator<AbstractWear> sortBy = SortingUtility.SORTING_WAYS.get(inputData.getSortBy());
        if (sortBy != null) {
            items.sort(sortBy);
        }

        final WardrobeSorterOutputData outputData = new WardrobeSorterOutputData(items);
        outputBoundary.prepareSuccessView(outputData);
    }
}
