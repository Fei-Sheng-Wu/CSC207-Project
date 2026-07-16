package use_case.item_action;

import entity.AbstractWear;

/**
 * Input data for actions performed on a wardrobe item.
 */
public class ItemActionRequest {
    private final AbstractWear item;

    public ItemActionRequest(AbstractWear item) {
        this.item = item;
    }

    public AbstractWear getItem() {
        return item;
    }
}
