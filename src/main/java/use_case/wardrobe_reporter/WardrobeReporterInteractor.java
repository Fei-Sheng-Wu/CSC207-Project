package use_case.wardrobe_reporter;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import entity.AbstractWear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * The Wardrobe Reporter Interactor.
 */
public class WardrobeReporterInteractor implements WardrobeReporterInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeReporterOutputBoundary outputBoundary;

    public WardrobeReporterInteractor(
        WardrobeDataAccessInterface wardrobeReporterDataAccessInterface,
        WardrobeReporterOutputBoundary wardrobeReporterOutputBoundary
    ) {
        this.repository = wardrobeReporterDataAccessInterface;
        this.outputBoundary = wardrobeReporterOutputBoundary;
    }

    /**
     * Executes the Wardrobe Reporter Use Case.
     * Reports all the clothes and their related info that are in this wardrobe.
     */
    @Override
    public void report() {
        final Wardrobe wardrobe = repository.fetchWardrobe();

        final List<AbstractWear> wearsAll = wardrobe.getItems();
        final List<AbstractWear> wearsOld = new ArrayList<>();
        for (AbstractWear wear : wearsAll) {
            final Period age = wear.getAge();
            if (age != null && age.getYears() >= 1) {
                wearsOld.add(wear);
            }
        }

        outputBoundary.prepareSuccessView(new WardrobeReporterOutputData(
            wearsAll,
            wearsOld
        ));
    }
}
