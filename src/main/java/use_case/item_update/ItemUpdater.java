package use_case.item_update;

import entity.Wardrobe;
import use_case.item_action.ItemActionOutputBoundary;
import use_case.item_action.ItemActionRequest;
import use_case.item_action.ItemActionResponse;
import use_case.item_action.WardrobeRepository;

/**
 * Use case interactor for updating a clothing item in the wardrobe.
 */
public class ItemUpdater implements ItemUpdateInputBoundary {
    private final WardrobeRepository repository;
    private final ItemActionOutputBoundary outputBoundary;

    public ItemUpdater(WardrobeRepository repository, ItemActionOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void updateItem(ItemActionRequest request) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final boolean successful = wardrobe.updateItem(request.getItem());

        if (successful) {
            repository.saveWardrobe(wardrobe);
            outputBoundary.prepareSuccessView(
                    new ItemActionResponse(true, "Clothing item updated successfully.")
            );
        }
        else {
            outputBoundary.prepareFailView(
                    new ItemActionResponse(false, "Clothing item could not be found.")
            );
        }
    }
}
