package use_case.wardrobe_reporter;

import java.util.ArrayList;
import java.util.List;

import entity.AbstractWear;
import entity.Wardrobe;
import entity.WearCondition;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * The Wardrobe Reporter Interactor.
 */
public class WardrobeReporterInteractor implements WardrobeReporterInputBoundary {
    private final WardrobeDataAccessInterface wardrobeReporterDataAccessObject;
    private final WardrobeReporterOutputBoundary wardrobeReporterPresenter;

    public WardrobeReporterInteractor(WardrobeDataAccessInterface wardrobeReporterDataAccessInterface,
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
        try {
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
            final WardrobeReporterOutputData outputData = new WardrobeReporterOutputData(wearsAll, wearsOld,
                wearsLaundryNeeded);
            wardrobeReporterPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            System.out.println("SOMETHING IS WRONG IN INTERACTOR OR DAO!");
            wardrobeReporterPresenter.prepareFailView("Data Error: " + e.getMessage());
        }
    }
}
