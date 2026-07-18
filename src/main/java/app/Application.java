package app;

import java.awt.CardLayout;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Represents an application.
 */
public class Application {
    private final Map<Class<?>, Object> registry = new HashMap<>();
    private final CardLayout layout = new CardLayout();
    private final Container panel = new JPanel(layout);

    /**
     * Constructs a new application.
     */
    public Application() {

    }

    /**
     * Register an object.
     *
     * @param objectClass the class of the object
     * @return the current application
     * @throws RuntimeException if the object cannot be registered
     */
    public Application register(Class<?> objectClass) {
        try {
            registry.put(objectClass, objectClass.getDeclaredConstructor(Application.class).newInstance(this));
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return this;
    }

    /**
     * Register a view.
     *
     * @param viewClass the class of the view
     * @return the current application
     */
    public Application registerView(Class<Container> viewClass) {
        register(viewClass);
        panel.add(get(viewClass), viewClass.getName());

        return this;
    }

    /**
     * Returns the registered object by the class.
     *
     * @param objectClass the class of the object
     * @param <T>         the type of the object
     * @return the object
     * @throws RuntimeException if the object is unregistered or invalid
     */
    public <T> T get(Class<T> objectClass) {
        try {
            return objectClass.cast(registry.get(objectClass));
        } catch (ClassCastException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Shows the specified view.
     *
     * @param viewClass the class of the view
     */
    public void showView(Class<Container> viewClass) {
        layout.show(panel, viewClass.getName());
    }

    /**
     * Creates the user interface for the application.
     *
     * @return the user interface
     */
    public JFrame build() {
        final JFrame frame = new JFrame("Suitable: Your Personal Outfit Advisor");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return frame;
    }
}
