package interface_adapter.wardrobe_sorter;

import interface_adapter.wardrobe.WardrobeViewModel;
import use_case.wardrobe_sorter.WardrobeSorterOutputBoundary;
import use_case.wardrobe_sorter.WardrobeSorterOutputData;

public class WardrobeSorterPresenter implements WardrobeSorterOutputBoundary {
    private final WardrobeViewModel wardrobeViewModel;

    public WardrobeSorterPresenter(WardrobeViewModel wardrobeViewModel) {
        this.wardrobeViewModel = wardrobeViewModel;
    }

    @Override
    public void prepareSuccessView(WardrobeSorterOutputData outputData) {
        wardrobeViewModel.setItems(outputData.getSortedItems());
    }
}
