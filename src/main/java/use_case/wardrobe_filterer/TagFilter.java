package use_case.wardrobe_filterer;

import java.util.List;

import entity.AbstractWear;

public class TagFilter implements WardrobeFilter {
    @Override
    public boolean matches(AbstractWear wear, WardrobeFiltererInputData criteria) {
        final String filterTag = criteria.getTag();
        if (filterTag == null || filterTag.isBlank()) {
            return true;
        }
        final List<String> wearTags = wear.getTags();
        if (wearTags == null || wearTags.isEmpty()) {
            return false;
        }
        for (String tag : wearTags) {
            if (tag.toLowerCase().contains(filterTag.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
