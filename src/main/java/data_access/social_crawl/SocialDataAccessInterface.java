package data_access.social_crawl;

import java.util.List;

import entity.OutfitIdea;

/**
 * Defines the interface of a holiday repository that provides holiday data.
 */
public interface SocialDataAccessInterface {
    /**
     * Returns a list of outfit ideas for the specified query.
     *
     * @param query the search query to find outfit ideas
     * @return a list of outfit ideas
     */
    List<OutfitIdea> getOutfitIdeas(String query);
}
