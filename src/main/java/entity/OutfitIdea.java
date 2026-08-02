package entity;

/**
 * Represents an outfit. Instances of this class are created using data from the SocialScrawl api.
 */
public class OutfitIdea {
    private final String description;
    private final String url;

    /**
     * Constructs a new outfit idea.
     *
     * @param description the description of the outfit idea
     * @param url         the url of this outfit idea
     */
    public OutfitIdea(String description, String url) {
        this.description = description;
        this.url = url;
    }

    /**
     * Returns the description of the outfit idea.
     *
     * @return the description of the outfit idea
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the url of this outfit idea.
     *
     * @return the url of this outfit idea
     */
    public String getUrl() {
        return url;
    }
}
