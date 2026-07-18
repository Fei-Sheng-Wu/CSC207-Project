package use_case.wardrobe_adder;

import entity.AbstractWear;

/**
 * Represents the input data for adding an item to the wardrobe.
 */
public class WardrobeAdderInputData {
    private final AbstractWear item;

    /**
     * Constructs a new input data.
     *
     * @param item the item to add
     */
    public WardrobeAdderInputData(AbstractWear item) {
        this.item = item;
    }

    /**
     * Returns the item to add.
     *
     * @return the item to add
     */
    public AbstractWear getItem() {
        return item;
    }
}
