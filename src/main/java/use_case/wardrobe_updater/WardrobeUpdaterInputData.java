package use_case.wardrobe_updater;

import entity.AbstractWear;

/**
 * Represents the input data for updating an item in the wardrobe.
 */
public class WardrobeUpdaterInputData {
    private final AbstractWear item;

    /**
     * Constructs a new input data.
     *
     * @param item the item to update
     */
    public WardrobeUpdaterInputData(AbstractWear item) {
        this.item = item;
    }

    /**
     * Returns the item to update.
     *
     * @return the item to update
     */
    public AbstractWear getItem() {
        return item;
    }
}
