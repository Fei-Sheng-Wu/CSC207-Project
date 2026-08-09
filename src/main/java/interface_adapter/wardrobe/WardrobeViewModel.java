package interface_adapter.wardrobe;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import entity.AbstractWear;
import interface_adapter.AbstractViewModel;
import interface_adapter.wardrobe_analyzer.WardrobeAnalyzerState;

/**
 * Represents the wardrobe view model.
 */
public class WardrobeViewModel extends AbstractViewModel {
    public static final String PROPERTY_ERROR = "error";
    public static final String PROPERTY_ITEMS = "items";
    public static final String PROPERTY_ITEMS_OLD = "itemsOld";
    public static final String PROPERTY_ANALYZER_STATE = "analyzerState";

    private String error;
    private List<AbstractWear> items = new ArrayList<>();
    private List<AbstractWear> itemsOld = new ArrayList<>();
    private WardrobeAnalyzerState analyzerState;

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
        firePropertyChange(PROPERTY_ITEMS, this.items);
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
        firePropertyChange(PROPERTY_ITEMS_OLD, this.itemsOld);
    }

    /**
     * Returns the analyzer state.
     *
     * @return the analyzer state
     */
    public WardrobeAnalyzerState getAnalyzerState() {
        return analyzerState;
    }

    /**
     * Sets the analyzer state and notifies listeners of the change.
     *
     * @param analyzerState the new analyzer state to set
     */
    public void setAnalyzerState(WardrobeAnalyzerState analyzerState) {
        this.analyzerState = analyzerState;
        firePropertyChange(PROPERTY_ANALYZER_STATE, this.analyzerState);
    }
}
