package use_case.wardrobe_reporter;

import java.util.List;

import entity.AbstractWear;

/**
 * Output Data for the Wardrobe Reporter Use Case.
 */
public class WardrobeReporterOutputData {
    private final List<AbstractWear> wearsAll;
    private final List<AbstractWear> wearsOld;
    private final List<AbstractWear> wearsLaundryNeeded;

    /**
     * Constructs a new wardrobe reporter output data.
     *
     * @param wearsAll a list of all clothing items in the wardrobe.
     * @param wearsOld a list of clothing items categorized as old.
     * @param wearsLaundryNeeded a list of clothing items that require laundry.
     */
    public WardrobeReporterOutputData(List<AbstractWear> wearsAll,
                                      List<AbstractWear> wearsOld,
                                      List<AbstractWear> wearsLaundryNeeded) {
        this.wearsAll = wearsAll;
        this.wearsOld = wearsOld;
        this.wearsLaundryNeeded = wearsLaundryNeeded;
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

    /**
     * Returns the list of clothing items that need laundry.
     *
     * @return a list of items that require laundry
     */
    public List<AbstractWear> getWearsLaundryNeeded() {
        return wearsLaundryNeeded;
    }

}
