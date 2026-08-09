package interface_adapter.inspiration;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import entity.AbstractWear;
import entity.OutfitIdea;
import interface_adapter.AbstractViewModel;

/**
 * Represents the inspiration view model.
 */
public class InspirationViewModel extends AbstractViewModel {
    public static final String PROPERTY_ERROR = "error";
    public static final String PROPERTY_CURRENT_ITEM = "currentItem";
    public static final String PROPERTY_IDEAS = "ideas";

    private String error;
    private AbstractWear currentItem;
    private List<OutfitIdea> ideas = new ArrayList<>();

    /**
     * Returns the error.
     *
     * @return the error
     */
    @Nullable
    public String getError() {
        return error;
    }

    /**
     * Updates the error.
     *
     * @param error the error
     */
    public void setError(@Nullable String error) {
        this.error = error;
        firePropertyChange(PROPERTY_ERROR, this.error);
    }

    /**
     * Returns the current item.
     *
     * @return the current item
     */
    @Nullable
    public AbstractWear getCurrentItem() {
        return currentItem;
    }

    /**
     * Updates the current item.
     *
     * @param item the current item
     */
    public void setCurrentItem(@Nullable AbstractWear item) {
        currentItem = item;
        firePropertyChange(PROPERTY_CURRENT_ITEM, currentItem);
    }

    /**
     * Returns the ideas.
     *
     * @return the ideas
     */
    public List<OutfitIdea> getIdeas() {
        return ideas;
    }

    /**
     * Updates the ideas.
     *
     * @param ideas the ideas
     */
    public void setIdeas(List<OutfitIdea> ideas) {
        this.ideas = ideas;
        firePropertyChange(PROPERTY_IDEAS, this.ideas);
    }
}
