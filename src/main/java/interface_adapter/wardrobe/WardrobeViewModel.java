package interface_adapter.wardrobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import entity.AbstractWear;
import interface_adapter.AbstractViewModel;

/**
 * Represents the wardrobe view model.
 */
public class WardrobeViewModel extends AbstractViewModel {
    public static final String PROPERTY_ERROR = "error";
    public static final String PROPERTY_ITEMS = "items";
    public static final String PROPERTY_ANALYZER_STATISTICS = "analyzerStatistics";

    private String error;
    private List<AbstractWear> items = new ArrayList<>();
    private Map<String, Object> analyzerStatistics;

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
     * Returns the analyzer statistics.
     *
     * @return the analyzer state
     */
    public Map<String, Object> getAnalyzerStatistics() {
        return analyzerStatistics;
    }

    /**
     * Sets the analyzer statistics and notifies listeners of the change.
     *
     * @param analyzerStatistics the new analyzer state to set
     */
    public void setAnalyzerStatistics(Map<String, Object> analyzerStatistics) {
        this.analyzerStatistics = analyzerStatistics;
        firePropertyChange(PROPERTY_ANALYZER_STATISTICS, this.analyzerStatistics);
    }
}
