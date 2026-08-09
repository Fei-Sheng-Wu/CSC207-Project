package use_case.wardrobe_sorter;

import entity.AbstractWear;

import java.util.Comparator;
import java.util.Map;

public final class SortingUtility {

    public static final Map<String, Comparator<AbstractWear>> SORTING_WAYS = Map.of(
        "NAME_ASC", Comparator.comparing(
            AbstractWear::getName,
            String.CASE_INSENSITIVE_ORDER
        ),
        "NAME_DESC", Comparator.comparing(
            AbstractWear::getName,
            String.CASE_INSENSITIVE_ORDER
        ).reversed(),
        "BRAND_ASC", Comparator.comparing(
            item ->
                item.getBrand() != null ? item.getBrand() : "",
            String.CASE_INSENSITIVE_ORDER
        ),
        "BRAND_DESC", Comparator.comparing(
            (AbstractWear item) ->
                item.getBrand() != null ? item.getBrand() : "",
            String.CASE_INSENSITIVE_ORDER
        ).reversed(),
        "TYPE", Comparator.comparing(
            item -> item.getClass().getSimpleName()
        )
    );
}

