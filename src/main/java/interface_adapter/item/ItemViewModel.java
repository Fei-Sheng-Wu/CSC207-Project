package interface_adapter.item;

import org.jetbrains.annotations.Nullable;

import interface_adapter.AbstractViewModel;

/**
 * Represents the item editing view model.
 */
public class ItemViewModel extends AbstractViewModel {
    private String error;

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
}
