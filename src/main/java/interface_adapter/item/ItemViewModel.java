package interface_adapter.item;

import org.jetbrains.annotations.Nullable;

import entity.AbstractWear;
import interface_adapter.AbstractViewModel;

/**
 * Represents the item editing view model.
 */
public class ItemViewModel extends AbstractViewModel {
    public static final String PROPERTY_ERROR = "error";
    public static final String PROPERTY_CURRENT_ITEM = "currentItem";

    private String error;
    private AbstractWear currentItem;

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
}
