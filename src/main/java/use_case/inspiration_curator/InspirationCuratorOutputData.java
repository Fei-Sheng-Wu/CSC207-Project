package use_case.inspiration_curator;

import java.util.List;

import entity.OutfitIdea;

/**
 * Represents the output data for curating an inspiration feed.
 */
public class InspirationCuratorOutputData {
    private final List<OutfitIdea> outfitIdeas;

    /**
     * Constructs a new output data.
     *
     * @param outfitIdeas the collection of outfit ideas from the curation
     */
    public InspirationCuratorOutputData(List<OutfitIdea> outfitIdeas) {
        this.outfitIdeas = outfitIdeas;
    }

    /**
     * Returns the collection of outfit ideas from the curation.
     *
     * @return the collection of outfit ideas from the curation
     */
    public List<OutfitIdea> getOutfitIdeas() {
        return outfitIdeas;
    }
}
