package use_case.inspiration_curator;

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
        // @TODO
    }
}
