package use_case.wardrobe_filterer;

import entity.AbstractWear;

public interface WardrobeFilter {
    /**
     * Determines whether the given clothing item matches the filtering criteria.
     *
     * @param wear     the clothing item to evaluate
     * @param criteria the filter preferences provided by the user
     * @return true if the item matches the filter condition, false otherwise
     */
    boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria);
}
