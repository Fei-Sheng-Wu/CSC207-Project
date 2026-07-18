package use_case.wardrobe_remover;

import entity.AbstractWear;

/**
 * Represents the input data for removing an item from the wardrobe.
 */
public class WardrobeRemoverInputData {
    private final AbstractWear item;

    /**
     * Constructs a new input data.
     *
     * @param item the item to remove
     */
    public WardrobeRemoverInputData(AbstractWear item) {
        this.item = item;
    }

    /**
     * Returns the item to remove.
     *
     * @return the item to remove
     */
    public AbstractWear getItem() {
        return item;
    }
}
