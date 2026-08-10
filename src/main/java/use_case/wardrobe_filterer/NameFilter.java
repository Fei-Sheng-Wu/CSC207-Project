package use_case.wardrobe_filterer;

import entity.AbstractWear;

public class NameFilter implements WardrobeFilter {
    @Override
    public boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria) {
        final String criteriaName = criteria.getName();
        if (criteriaName == null || criteriaName.isEmpty()) {
            return true;
        }
        return wear.getName().toLowerCase().contains(criteriaName.toLowerCase());
    }
}
