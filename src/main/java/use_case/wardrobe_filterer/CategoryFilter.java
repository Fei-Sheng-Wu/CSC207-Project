package use_case.wardrobe_filterer;

import entity.AbstractWear;
import entity.WearFactory;

public class CategoryFilter implements WardrobeFilter {
    @Override
    public boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria) {
        final String selectedCategory = criteria.getCategory();
        if (selectedCategory == null || selectedCategory.isBlank()) {
            return true;
        }
        return selectedCategory.equalsIgnoreCase(WearFactory.getDisplayName(wear.getClass()));
    }
}
