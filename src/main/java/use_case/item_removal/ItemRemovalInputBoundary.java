package use_case.item_removal;

import use_case.item_action.ItemActionRequest;

/**
 * Input boundary for removing a clothing item.
 */
public interface ItemRemovalInputBoundary {
    void removeItem(ItemActionRequest request);
}
