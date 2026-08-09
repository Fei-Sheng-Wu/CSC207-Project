package use_case.inspiration_curator;

import entity.AbstractWear;

/**
 * Represents the input data for curating an inspiration feed.
 */
public class InspirationCuratorInputData {
    private final AbstractWear wear;

    /**
     * Constructs a new input data.
     *
     * @param wear  the clothing item for the curation
     */
    public InspirationCuratorInputData(AbstractWear wear) {
        this.wear = wear;
    }

    /**
     * Returns the clothing item for the curation.
     *
     * @return the clothing item for the curation
     */
    public AbstractWear getWear() {
        return wear;
    }
}
