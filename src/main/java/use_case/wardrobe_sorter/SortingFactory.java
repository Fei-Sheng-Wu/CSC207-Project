package use_case.wardrobe_sorter;

import java.util.Comparator;

import entity.AbstractWear;
import entity.WardrobeSort;

public final class SortingFactory {

    /**
     * Creates and returns a Comparator based on the provided sortType.
     *
     * @param sortType the wardrobe sort option
     * @return a Comparator for the given sort type
     * @throws IllegalArgumentException if the sort type is unsupported
     */
    public static Comparator<AbstractWear> createComparator(WardrobeSort sortType) {
        switch (sortType) {
            case NAME_ASC:
                return Comparator.comparing(
                    AbstractWear::getName,
                    String.CASE_INSENSITIVE_ORDER
                );
            case NAME_DESC:
                return Comparator.comparing(
                    AbstractWear::getName,
                    String.CASE_INSENSITIVE_ORDER
                ).reversed();
            case BRAND_ASC:
                return Comparator.comparing(
                    AbstractWear::getBrand,
                    String.CASE_INSENSITIVE_ORDER
                );
            case BRAND_DESC:
                return Comparator.comparing(
                    AbstractWear::getBrand,
                    String.CASE_INSENSITIVE_ORDER
                ).reversed();
            case TYPE:
                return Comparator.comparing(
                    item -> item.getClass().getSimpleName()
                );
            default:
                throw new IllegalArgumentException("Unsupported sort type: " + sortType);
        }
    }
}

