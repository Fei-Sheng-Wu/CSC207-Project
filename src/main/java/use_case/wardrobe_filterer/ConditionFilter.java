package use_case.wardrobe_filterer;

import entity.AbstractWear;

public class ConditionFilter implements WardrobeFilter {
    @Override
    public boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria) {
        final String filterCondition = criteria.getCondition();
        if (filterCondition == null || filterCondition.isEmpty()
            || "All Conditions".equalsIgnoreCase(filterCondition)) {
            return true;
        }
        if (wear.getCondition() == null) {
            return false;
        }
        return wear.getCondition().name().equalsIgnoreCase(filterCondition);
    }
}
