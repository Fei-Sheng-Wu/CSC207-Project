package use_case.inspiration_curator;

/**
 * Defines the input boundary for curating an inspiration feed.
 */
public interface InspirationCuratorInputBoundary {
    /**
     * Curates an inspiration feed.
     *
     * @param request the input data
     */
    void curate(InspirationCuratorInputData request);
}
