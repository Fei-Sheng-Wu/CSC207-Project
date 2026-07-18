package use_case.item_update;

import use_case.item_action.ItemActionRequest;

/**
 * Input boundary for updating a clothing item.
 */
public interface ItemUpdateInputBoundary {
    void updateItem(ItemActionRequest request);
}
