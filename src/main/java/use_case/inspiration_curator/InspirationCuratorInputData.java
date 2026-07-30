package use_case.inspiration_curator;

import org.jetbrains.annotations.Nullable;

import entity.AbstractWear;

/**
 * Represents the input data for curating an inspiration feed.
 */
public class InspirationCuratorInputData {
    private final String query;
    private final AbstractWear wear;

    /**
     * Constructs a new input data.
     *
     * @param query the query for the curation
     * @param wear  the clothing item for the curation
     */
    public InspirationCuratorInputData(String query, @Nullable AbstractWear wear) {
        this.query = query;
        this.wear = wear;
    }

    /**
     * Returns the query for the curation.
     *
     * @return the query for the curation
     */
    public String getQuery() {
        return query;
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
