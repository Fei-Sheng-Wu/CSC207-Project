package use_case.wardrobe_reporter;

import java.util.List;

import entity.AbstractWear;

/**
 * Output Data for the Wardrobe Reporter Use Case.
 */
public class WardrobeReporterOutputData {
    private final List<AbstractWear> wearsAll;
    private final List<AbstractWear> wearsOld;

    /**
     * Constructs a new wardrobe reporter output data.
     *
     * @param wearsAll a list of all clothing items in the wardrobe.
     * @param wearsOld a list of clothing items categorized as old.
     */
    public WardrobeReporterOutputData(
        List<AbstractWear> wearsAll,
        List<AbstractWear> wearsOld
    ) {
        this.wearsAll = wearsAll;
        this.wearsOld = wearsOld;
    }

    /**
     * Returns the list of all clothing items.
     *
     * @return a list of all items in the wardrobe
     */
    public List<AbstractWear> getWearsAll() {
        return wearsAll;
    }

    /**
     * Returns the list of old clothing items.
     *
     * @return a list of items categorized as old
     */
    public List<AbstractWear> getWearsOld() {
        return wearsOld;
    }
}
