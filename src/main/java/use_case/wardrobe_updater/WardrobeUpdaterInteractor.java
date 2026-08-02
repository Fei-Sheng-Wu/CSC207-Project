package use_case.wardrobe_updater;

import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Use case interactor for updating a clothing item in the wardrobe.
 */
public class WardrobeUpdaterInteractor implements WardrobeUpdaterInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeUpdaterOutputBoundary outputBoundary;

    public WardrobeUpdaterInteractor(
        WardrobeDataAccessInterface repository,
        WardrobeUpdaterOutputBoundary outputBoundary
    ) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void updateItem(WardrobeUpdaterInputData request) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final boolean isSuccessful = wardrobe.updateItem(request.getItem());

        if (isSuccessful) {
            repository.saveWardrobe(wardrobe);
            outputBoundary.prepareSuccessView();
        } else {
            outputBoundary.prepareFailView("The item cannot be updated.");
        }
    }
}
