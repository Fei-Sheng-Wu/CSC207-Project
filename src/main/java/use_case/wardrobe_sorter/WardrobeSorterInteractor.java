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

    public WardrobeSorterInteractor(WardrobeDataAccessInterface repository,
                                    WardrobeSorterOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void sortWardrobe(WardrobeSorterInputData inputData) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final List<AbstractWear> items = new ArrayList<>(wardrobe.getItems());

        switch (inputData.getSortBy()) {
            case NAME_ASC:
                items.sort(Comparator.comparing(AbstractWear::getName, String.CASE_INSENSITIVE_ORDER));
                break;
            case NAME_DESC:
                items.sort(Comparator.comparing(AbstractWear::getName, String.CASE_INSENSITIVE_ORDER).reversed());
                break;
            case BRAND_ASC:
                items.sort(Comparator.comparing(item -> {
                    final String brand = item.getBrand();
                    if (brand != null) {
                        return brand;
                    }
                    return "";
                }, String.CASE_INSENSITIVE_ORDER));
                break;
            case BRAND_DESC:
                items.sort(Comparator.comparing((AbstractWear item) -> {
                    final String brand = item.getBrand();
                    if (brand != null) {
                        return brand;
                    }
                    return "";
                }, String.CASE_INSENSITIVE_ORDER).reversed());
                break;
            case TYPE:
                items.sort(Comparator.comparing(item -> item.getClass().getSimpleName()));
                break;
            default:
                // Default: sort by name
                items.sort(Comparator.comparing(AbstractWear::getName, String.CASE_INSENSITIVE_ORDER));
                break;
        }

        final WardrobeSorterOutputData outputData = new WardrobeSorterOutputData(items);
        outputBoundary.prepareSuccessView(outputData);
    }
}
