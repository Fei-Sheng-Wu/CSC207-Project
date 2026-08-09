package use_case.wardrobe_retriever;

import java.util.List;

import entity.AbstractWear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * The Wardrobe Retriever Interactor.
 */
public class WardrobeRetrieverInteractor implements WardrobeRetrieverInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeRetrieverOutputBoundary outputBoundary;

    public WardrobeRetrieverInteractor(
        WardrobeDataAccessInterface wardrobeReporterDataAccessInterface,
        WardrobeRetrieverOutputBoundary wardrobeRetrieverOutputBoundary
    ) {
        this.repository = wardrobeReporterDataAccessInterface;
        this.outputBoundary = wardrobeRetrieverOutputBoundary;
    }

    /**
     * Executes the Wardrobe Retriever Use Case.
     * Reports all the clothes and their related info that are in this wardrobe.
     */
    @Override
    public void retrieve() {
        final Wardrobe wardrobe = repository.fetchWardrobe();

        final List<AbstractWear> wearsAll = wardrobe.getItems();

        outputBoundary.prepareSuccessView(new WardrobeRetrieverOutputData(
            wearsAll
        ));
    }
}
