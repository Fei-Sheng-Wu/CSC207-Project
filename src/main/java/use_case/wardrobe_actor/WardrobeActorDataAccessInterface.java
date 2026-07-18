package use_case.wardrobe_actor;

import entity.Wardrobe;

/**
 * Data access interface for wardrobe-related actions.
 */
public interface WardrobeActorDataAccessInterface {
    /**
     * Retrieves the saved wardrobe data.
     *
     * @return the Wardrobe entity containing all saved clothing items
     */
    Wardrobe fetchWardrobe();

    /**
     * Saves the specified wardrobe data.
     *
     * @param wardrobe the Wardrobe entity to be saved
     */
    void saveWardrobe(Wardrobe wardrobe);
}
