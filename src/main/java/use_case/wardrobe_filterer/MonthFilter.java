package use_case.wardrobe_filterer;

import java.time.Period;

import entity.AbstractWear;

public class MonthFilter implements WardrobeFilter {
    private static final int MONTHS_IN_YEAR = 12;

    @Override
    public boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria) {
        if (criteria.getPurchaseMonth() <= 0) {
            return true;
        }
        final Period age = wear.getAge();
        if (age == null) {
            return false;
        }
        final int totalMonthsOld = (age.getYears() * MONTHS_IN_YEAR) + age.getMonths();
        return totalMonthsOld >= criteria.getPurchaseMonth();
    }
}
