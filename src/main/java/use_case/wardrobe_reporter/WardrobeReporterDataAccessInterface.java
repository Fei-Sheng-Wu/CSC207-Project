package use_case.wardrobe_reporter;

import entity.Wardrobe;

/**
 * DAO for the Wardrobe Reporter Use Case.
 */
public interface WardrobeReporterDataAccessInterface {
    // Renamed from WardrobeRepositroyDataAccessInterface to WardrobeReporterDataAccessInterface
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
