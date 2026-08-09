package use_case.wardrobe_sorter;

import java.util.Comparator;
import java.util.Map;

import entity.AbstractWear;
import entity.WardrobeSort;

public final class SortingUtility {

    public static final Map<WardrobeSort, Comparator<AbstractWear>> SORTING_WAYS = Map.of(
        WardrobeSort.NAME_ASC, Comparator.comparing(
            AbstractWear::getName,
            String.CASE_INSENSITIVE_ORDER
        ),
        WardrobeSort.NAME_DESC, Comparator.comparing(
            AbstractWear::getName,
            String.CASE_INSENSITIVE_ORDER
        ).reversed(),
        WardrobeSort.BRAND_ASC, Comparator.comparing(
            item -> {
                if (item.getBrand() == null) {
                    return "";
                }
                return item.getBrand();
            },
            String.CASE_INSENSITIVE_ORDER
        ),
        WardrobeSort.BRAND_DESC, Comparator.comparing(
            (AbstractWear item) -> {
                if (item.getBrand() == null) {
                    return "";
                }
                return item.getBrand();
            },
            String.CASE_INSENSITIVE_ORDER
        ).reversed(),
        WardrobeSort.TYPE, Comparator.comparing(
            item -> item.getClass().getSimpleName()
        )
    );
}

