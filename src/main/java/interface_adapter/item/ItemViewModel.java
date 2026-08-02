package interface_adapter.item;

import entity.AbstractWear;
import org.jetbrains.annotations.Nullable;

import interface_adapter.AbstractViewModel;

/**
 * Represents the item editing view model.
 */
public class ItemViewModel extends AbstractViewModel {
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
        firePropertyChange("error", this.error);
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
        firePropertyChange("currentItem", currentItem);
    }
}
