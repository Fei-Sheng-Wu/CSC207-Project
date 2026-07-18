package use_case.item_action;

/**
 * Output boundary for wardrobe item actions.
 */
public interface ItemActionOutputBoundary {
    void prepareSuccessView(ItemActionResponse response);

    void prepareFailView(ItemActionResponse response);
}
