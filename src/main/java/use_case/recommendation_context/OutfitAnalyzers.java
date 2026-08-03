package use_case.recommendation_context;

import java.util.List;

/**
 * Supplies the set of analyzers the recommendation use case scores outfits with.
 *
 * <p>Collecting the default set here keeps the interactor from naming the concrete analyzers it
 * happens to run with. Adding a new criterion means writing another {@link OutfitAnalyzer} and
 * listing it below; neither the interactor nor any existing analyzer changes.
 */
public final class OutfitAnalyzers {
    private OutfitAnalyzers() {
    }

    /**
     * Returns the analyzers the application scores outfits with by default.
     *
     * @return the default analyzers, in evaluation order
     */
    public static List<OutfitAnalyzer> standard() {
        return List.of(
            new PreferenceOutfitAnalyzer(),
            new FondnessOutfitAnalyzer(),
            new WeatherOutfitAnalyzer(),
            new EventOutfitAnalyzer()
        );
    }

    /**
     * Returns the analyzers the application narrows each wardrobe slot with by default.
     *
     * <p>These are the criteria a single garment can be judged on in isolation, which is what
     * makes narrowing possible at all. A criterion that only makes sense for a finished outfit,
     * such as an average or a rule about how two garments look together, belongs in
     * {@link #standard()} instead.
     *
     * @return the default item analyzers
     */
    public static List<ItemAnalyzer> standardItems() {
        return List.of(
            new EventItemAnalyzer(),
            new PreferenceItemAnalyzer()
        );
    }
}
