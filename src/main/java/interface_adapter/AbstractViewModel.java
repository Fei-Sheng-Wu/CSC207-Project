package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents a view model.
 */
public abstract class AbstractViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Adds a property change listener to the view model.
     *
     * @param listener the property change listener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Fires a property change event for the view model.
     *
     * @param propertyName  the name of the property that was changed
     * @param propertyValue the value of the property that was changed
     */
    public void firePropertyChange(String propertyName, Object propertyValue) {
        support.firePropertyChange(propertyName, null, propertyValue);
    }
}
