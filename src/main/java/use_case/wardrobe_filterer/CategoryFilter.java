package use_case.wardrobe_filterer;

import entity.AbstractWear;

public class CategoryFilter implements WardrobeFilter {
    @Override
    public boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria) {
        final String selectedCategory = criteria.getCategory();
        if (selectedCategory == null || "All Categories".equalsIgnoreCase(selectedCategory)) {
            return true;
        }
        return selectedCategory.equalsIgnoreCase(wear.getClass().getSimpleName());
    }
}
