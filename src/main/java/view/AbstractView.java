package view;

import javax.swing.JPanel;

/**
 * Represents a view of the application.
 */
public abstract class AbstractView extends JPanel {
    private final ApplicationManager manager;

    /**
     * Constructs a new view.
     *
     * @param manager the application manager of the view
     */
    public AbstractView(ApplicationManager manager) {
        this.manager = manager;
    }

    /**
     * Returns the title of the view.
     *
     * @return the title of the view
     */
    public abstract String getTitle();

    /**
     * Returns the application manager of the view.
     *
     * @return the application manager of the view
     */
    public ApplicationManager getApplicationManager() {
        return manager;
    }
}
