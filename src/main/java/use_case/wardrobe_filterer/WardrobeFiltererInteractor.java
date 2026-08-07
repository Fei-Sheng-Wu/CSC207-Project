package use_case.wardrobe_filterer;

import entity.AbstractWear;
import entity.Wardrobe;
import interface_adapter.wardrobe_filterer.WardrobeFilteringModel;
import use_case.wardrobe.WardrobeDataAccessInterface;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

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
     * @param filterCriteria the model containing the user's filter preferences
     */
    public void filterItems(WardrobeFilteringModel filterCriteria) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final List<AbstractWear> allItems = wardrobe.getItems();
        final List<AbstractWear> filteredAll = new ArrayList<>();

        for (AbstractWear wear : allItems) {
            final boolean matchesName = isNameMatch(filterCriteria, wear);
            final boolean matchesCategory = isCategoryMatch(filterCriteria, wear);
            final boolean matchesMonths = isMonthMatch(filterCriteria, wear);
            final boolean matchesCondition = isConditionMatch(filterCriteria, wear);
            final boolean matchesTag = isTagMatch(filterCriteria, wear);
            if (matchesName && matchesCategory
                && matchesMonths && matchesCondition && matchesTag) {
                filteredAll.add(wear);
            }
        }
        outputBoundary.prepareSuccessView(new WardrobeFiltererOutputData(
            filteredAll
        ));
    }

    private static boolean isMonthMatch(WardrobeFilteringModel filterCriteria, AbstractWear wear) {
        boolean matchesMonths = true;
        if (filterCriteria.getPurchaseMonth() > 0) {
            final Period age = wear.getAge();
            if (age != null) {
                final int totalMonthsOld = (age.getYears() * 12) + age.getMonths();
                matchesMonths = totalMonthsOld >= filterCriteria.getPurchaseMonth();
            } else {
                matchesMonths = false;
            }
        }
        return matchesMonths;
    }

    private static boolean isNameMatch(WardrobeFilteringModel filterCriteria, AbstractWear wear) {
        return filterCriteria.getName() == null || filterCriteria.getName().isEmpty()
            || wear.getName().toLowerCase().startsWith(filterCriteria.getName().toLowerCase());
    }

    private static boolean isCategoryMatch(WardrobeFilteringModel filterCriteria, AbstractWear wear) {
        boolean matchesCategory = filterCriteria.getCategory() == null
            || filterCriteria.getCategory().equalsIgnoreCase("All Categories");
        if (!matchesCategory) {
            final String className = wear.getClass().getSimpleName();
            final String selectedCategory = filterCriteria.getCategory();

            if (selectedCategory.equalsIgnoreCase(className)) {
                matchesCategory = true;
            } else {
                matchesCategory = false;
            }
        }
        return matchesCategory;
    }

    private static boolean isConditionMatch(WardrobeFilteringModel filterCriteria, AbstractWear wear) {
        if (filterCriteria.getCondition() == null || filterCriteria.getCondition().isEmpty()
            || filterCriteria.getCondition().equalsIgnoreCase("All Conditions")) {
            return true;
        }

        if (wear.getCondition() == null) {
            return false;
        }

        return wear.getCondition().name().equalsIgnoreCase(filterCriteria.getCondition());
    }

    private static boolean isTagMatch(WardrobeFilteringModel filterCriteria, AbstractWear wear) {
        if (filterCriteria.getTag() == null || filterCriteria.getTag().isEmpty()) {
            return true;
        }

        final List<String> wearTags = wear.getTags();
        if (wearTags == null || wearTags.isEmpty()) {
            return false;
        }

        for (String tag : wearTags) {
            if (tag.toLowerCase().contains(filterCriteria.getTag().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

}
