package use_case.wardrobe_actor;

import entity.Wardrobe;

/**
 * Data access interface for the wardrobe.
 */
public interface WardrobeActorDataAccessInterface {
    /**
     * Retrieves the current wardrobe data from the database.
     *
     * @return the Wardrobe entity containing all saved clothing items
     */
    Wardrobe fetchWardrobe();

    /**
     * Saves the given wardrobe data to the database.
     *
     * @param wardrobe the Wardrobe entity to be saved
     */
    void saveWardrobe(Wardrobe wardrobe);
}
