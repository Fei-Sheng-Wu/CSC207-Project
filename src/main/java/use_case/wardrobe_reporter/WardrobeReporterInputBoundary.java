package use_case.wardrobe_reporter;

/**
 * Input Boundary for actions which are related to using Wardrobe Reporter.
 */
public interface WardrobeReporterInputBoundary {
    /**
     * Executes the Wardrobe Reporter Use Case.
     * Reports all the clothes and their related info that are in this wardrobe.
     */
    void report();

    // Other possible methods: washed, add clothes, remove clothes, laundry.
}
