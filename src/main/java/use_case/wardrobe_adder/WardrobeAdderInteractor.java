package use_case.wardrobe_adder;

import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * Use case interactor for adding a clothing item from the wardrobe.
 */
public class WardrobeAdderInteractor implements WardrobeAdderInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeAdderOutputBoundary outputBoundary;

    public WardrobeAdderInteractor(
        WardrobeDataAccessInterface repository,
        WardrobeAdderOutputBoundary outputBoundary
    ) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void addItem(WardrobeAdderInputData request) {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        wardrobe.addItem(request.getItem());

        repository.saveWardrobe(wardrobe);
        outputBoundary.prepareSuccessView(request.getItem());
    }
}
