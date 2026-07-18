package use_case.wardrobe_updater;

import entity.Wardrobe;
import use_case.wardrobe_actor.WardrobeActorDataAccessInterface;

/**
 * Use case interactor for updating a clothing item in the wardrobe.
 */
public class WardrobeUpdaterInteractor implements WardrobeUpdaterInputBoundary {
    private final WardrobeActorDataAccessInterface repository;
    private final WardrobeUpdaterOutputBoundary outputBoundary;

    public WardrobeUpdaterInteractor(WardrobeActorDataAccessInterface repository,
                                     WardrobeUpdaterOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void updateItem(WardrobeUpdaterInputData request) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final boolean successful = wardrobe.updateItem(request.getItem());

        if (successful) {
            repository.saveWardrobe(wardrobe);
            outputBoundary.prepareSuccessView();
        }
        else {
            outputBoundary.prepareFailView();
        }
    }
}
