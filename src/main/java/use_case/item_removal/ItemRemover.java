package use_case.item_removal;

import entity.Wardrobe;
import use_case.item_action.ItemActionOutputBoundary;
import use_case.item_action.ItemActionRequest;
import use_case.item_action.ItemActionResponse;
import use_case.item_action.WardrobeRepository;

/**
 * Use case interactor for removing a clothing item from the wardrobe.
 */
public class ItemRemover implements ItemRemovalInputBoundary {
    private final WardrobeRepository repository;
    private final ItemActionOutputBoundary outputBoundary;

    public ItemRemover(WardrobeRepository repository, ItemActionOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void removeItem(ItemActionRequest request) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final boolean successful = wardrobe.removeItem(request.getItem());

        if (successful) {
            repository.saveWardrobe(wardrobe);
            outputBoundary.prepareSuccessView(
                    new ItemActionResponse(true, "Clothing item removed successfully.")
            );
        }
        else {
            outputBoundary.prepareFailView(
                    new ItemActionResponse(false, "Clothing item could not be found.")
            );
        }
    }
}
