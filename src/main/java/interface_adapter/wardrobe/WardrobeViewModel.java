package interface_adapter.wardrobe;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import entity.AbstractWear;
import interface_adapter.AbstractViewModel;

/**
 * Represents the wardrobe view model.
 */
public class WardrobeViewModel extends AbstractViewModel {
    private String error;
    private List<AbstractWear> items = new ArrayList<>();
    private List<AbstractWear> itemsOld = new ArrayList<>();

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
     * Returns the items.
     *
     * @return the items
     */
    public List<AbstractWear> getItems() {
        return items;
    }

    /**
     * Updates the items.
     *
     * @param items the items
     */
    public void setItems(List<AbstractWear> items) {
        this.items = items;
        firePropertyChange("items", this.items);
    }

    /**
     * Returns the old items.
     *
     * @return the old items
     */
    public List<AbstractWear> getOldItems() {
        return itemsOld;
    }

    /**
     * Updates the old items.
     *
     * @param oldItems the old items
     */
    public void setOldItems(List<AbstractWear> oldItems) {
        this.itemsOld = oldItems;
        firePropertyChange("itemsOld", this.itemsOld);
    }
}
