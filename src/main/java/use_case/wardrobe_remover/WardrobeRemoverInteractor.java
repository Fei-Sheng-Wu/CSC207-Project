package use_case.wardrobe_remover;

import entity.Wardrobe;
import use_case.wardrobe_actor.WardrobeActorDataAccessInterface;

/**
 * Use case interactor for removing a clothing item from the wardrobe.
 */
public class WardrobeRemoverInteractor implements WardrobeRemoverInputBoundary {
    private final WardrobeActorDataAccessInterface repository;
    private final WardrobeRemoverOutputBoundary outputBoundary;

    public WardrobeRemoverInteractor(WardrobeActorDataAccessInterface repository,
                                     WardrobeRemoverOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void removeItem(WardrobeRemoverInputData request) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final boolean successful = wardrobe.removeItem(request.getItem());

        if (successful) {
            repository.saveWardrobe(wardrobe);
            outputBoundary.prepareSuccessView();
        }
        else {
            outputBoundary.prepareFailView();
        }
    }
}
