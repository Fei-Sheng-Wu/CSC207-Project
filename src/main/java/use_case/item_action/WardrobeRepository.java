package use_case.item_action;

import entity.Wardrobe;

/**
 * Data access interface for the wardrobe.
 */
public interface WardrobeRepository {
    Wardrobe fetchWardrobe();

    void saveWardrobe(Wardrobe wardrobe);
}
