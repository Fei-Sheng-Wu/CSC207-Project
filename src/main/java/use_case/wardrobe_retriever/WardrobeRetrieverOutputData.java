package use_case.wardrobe_retriever;

import java.util.List;

import entity.AbstractWear;

/**
 * Output Data for the Wardrobe Reporter Use Case.
 */
public class WardrobeRetrieverOutputData {
    private final List<AbstractWear> wearsAll;

    /**
     * Constructs a new wardrobe reporter output data.
     *
     * @param wearsAll a list of all clothing items in the wardrobe.
     */
    public WardrobeRetrieverOutputData(
        List<AbstractWear> wearsAll
    ) {
        this.wearsAll = wearsAll;
    }

    /**
     * Returns the list of all clothing items.
     *
     * @return a list of all items in the wardrobe
     */
    public List<AbstractWear> getWearsAll() {
        return wearsAll;
    }
}
