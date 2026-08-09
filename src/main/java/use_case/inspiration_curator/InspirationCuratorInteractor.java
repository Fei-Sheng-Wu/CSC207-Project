package use_case.inspiration_curator;

import entity.AbstractWear;

/**
 * Represents the interactor for curating an inspiration feed.
 */
public class InspirationCuratorInteractor implements InspirationCuratorInputBoundary {
    private final InspirationDataAccessInterface repository;
    private final InspirationCuratorOutputBoundary outputBoundary;

    /**
     * Constructs a new interactor.
     *
     * @param repository     the data access object of the interactor
     * @param outputBoundary the output boundary of the interactor
     */
    public InspirationCuratorInteractor(
        InspirationDataAccessInterface repository,
        InspirationCuratorOutputBoundary outputBoundary
    ) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void curate(InspirationCuratorInputData request) {
        final AbstractWear wear = request.getWear();

        String color = "";
        if (wear.getColor() != null) {
            color = wear.getColor().getDisplayName();
        }

        outputBoundary.prepareSuccessView(
            new InspirationCuratorOutputData(repository.getOutfitIdeas(String.format(
                "outfit inspirations with %s %s from %s",
                color,
                wear.getName(),
                wear.getBrand()
            )))
        );
    }
}
