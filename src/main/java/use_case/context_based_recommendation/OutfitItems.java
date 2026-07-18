package use_case.context_based_recommendation;

import java.util.ArrayList;
import java.util.List;

import entity.AbstractWear;
import entity.Outfit;

/**
 * Collects the clothing items contained in an outfit.
 */
final class OutfitItems {
    private OutfitItems() {
    }

    static List<AbstractWear> collect(Outfit outfit) {
        final List<AbstractWear> items = new ArrayList<>();
        items.add(outfit.getTopwearInner());
        if (outfit.getTopwearOuter() != null) {
            items.add(outfit.getTopwearOuter());
        }
        items.add(outfit.getBottomwear());
        items.add(outfit.getFootwear());
        if (outfit.getHeadwear() != null) {
            items.add(outfit.getHeadwear());
        }
        items.addAll(outfit.getAccessories());
        return items;
    }
}
