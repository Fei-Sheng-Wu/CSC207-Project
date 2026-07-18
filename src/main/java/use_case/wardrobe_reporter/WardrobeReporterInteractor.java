package use_case.wardrobe_reporter;

import java.util.ArrayList;
import java.util.List;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearCondition;

/**
 * The Wardrobe Reporter Interactor.
 */
public class WardrobeReporterInteractor implements WardrobeReporterInputBoundary {
    private final WardrobeReporterDataAccessInterface wardrobeReporterDataAccessObject;
    private final WardrobeReporterOutputBoundary wardrobeReporterPresenter;

    public WardrobeReporterInteractor(WardrobeReporterDataAccessInterface wardrobeReporterDataAccessInterface,
                                      WardrobeReporterOutputBoundary wardrobeReporterOutputBoundary) {
        this.wardrobeReporterDataAccessObject = wardrobeReporterDataAccessInterface;
        this.wardrobeReporterPresenter = wardrobeReporterOutputBoundary;
    }

    /**
     * Executes the Wardrobe Reporter Use Case.
     * Reports all the clothes and their related info that are in this wardrobe.
     */
    @Override
    public void report() {
        final Wardrobe wardrobe = wardrobeReporterDataAccessObject.fetchWardrobe();

        final List<AbstractWear> wearsAll = wardrobe.getItems();
        final List<AbstractWear> wearsOld = new ArrayList<>();
        final List<AbstractWear> wearsLaundryNeeded = new ArrayList<>();

        for (AbstractWear wear : wearsAll) {

            if (wear.getPurchaseDate() != null && wear.getAge().getYears() >= 1) {
                wearsOld.add(wear);
            }

            if (wear.getCondition() != WearCondition.NEW) {
                wearsLaundryNeeded.add(wear); // Later on we need to update WearCondition to add DIRTY
            }
        }

        final WardrobeReporterOutputData outputData = new WardrobeReporterOutputData(
            wearsAll, wearsOld, wearsLaundryNeeded);
        // wardrobeReporterPresenter.prepareSuccessView(outputData);
        // Later on we need also to add prepare fail view. Java nw is preventing me from placing it inside a catch block

        // TEMPORARY: Print results to test functionality
        System.out.println("--- WARDROBE REPORT ---");
        System.out.println("Total clothes: " + outputData.getWearsAll().size());
        System.out.println("Old clothes (>= 1 year): " + outputData.getWearsOld().size());
        System.out.println("Clothes needing laundry: " + outputData.getWearsLaundryNeeded().size());
        System.out.println("-----------------------");
    }
}
