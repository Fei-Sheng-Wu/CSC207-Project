package view;

import javax.swing.JPanel;

/**
 * Represents a view of the application.
 */
public abstract class AbstractView extends JPanel {
    protected static final int SIZE_SPACING_XS = 4;
    protected static final int SIZE_SPACING_SM = 8;
    protected static final int SIZE_SPACING_MD = 12;
    protected static final int SIZE_SPACING_LG = 24;
    protected static final int SIZE_SPACING_XL = 32;
    protected static final int SIZE_WIDTH_SM = 24;
    protected static final int SIZE_WIDTH_MD = 48;
    protected static final int SIZE_WIDTH_LG = 96;
    protected static final int SIZE_WIDTH_XL = 192;
    protected static final int SIZE_HEIGHT_SM = 12;
    protected static final int SIZE_HEIGHT_MD = 24;
    protected static final int SIZE_HEIGHT_LG = 36;
    protected static final int SIZE_HEIGHT_XL = 48;

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
