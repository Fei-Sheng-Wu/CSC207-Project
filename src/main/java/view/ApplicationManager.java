package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Represents an application manager.
 */
public class ApplicationManager {
    private static final int WINDOW_WIDTH_MIN = 960;
    private static final int WINDOW_HEIGHT_MIN = 580;

    private final Map<Class<?>, Object> registry;
    private final List<Class<? extends AbstractView>> navigationsTop;
    private final List<Class<? extends AbstractView>> navigationsBottom;

    private final JFrame window;
    private final JPanel navigator;
    private final CardLayout navigatorLayout;

    /**
     * Constructs a new application.
     *
     * @param registry          the registry of the application
     * @param navigationsTop    the top navigations of the application
     * @param navigationsBottom the bottom navigations of the application
     */
    public ApplicationManager(
        Map<Class<?>, Object> registry,
        List<Class<? extends AbstractView>> navigationsTop,
        List<Class<? extends AbstractView>> navigationsBottom
    ) {
        this.registry = registry;
        this.navigationsTop = navigationsTop;
        this.navigationsBottom = navigationsBottom;

        this.window = new JFrame();
        this.navigatorLayout = new CardLayout();
        this.navigator = new JPanel(this.navigatorLayout);
        this.navigator.setOpaque(false);
    }

    /**
     * Registers an object.
     *
     * @param objectClass the class of the object
     * @param <T>         the type of the object
     * @throws RuntimeException if the object cannot be registered
     */
    public <T> void register(Class<T> objectClass) {
        try {
            register(objectClass, objectClass.getDeclaredConstructor().newInstance());
        } catch (NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Registers an object.
     *
     * @param objectClass the class of the object
     * @param object      the instantiated object
     * @param <T>         the type of the object
     * @throws RuntimeException if the object cannot be registered
     */
    public <T> void register(Class<T> objectClass, T object) {
        registry.put(objectClass, object);
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
    public void showView(Class<? extends AbstractView> viewClass) {
        final String identifier = viewClass.getName();
        final AbstractView view = get(viewClass);
        if (!navigator.isAncestorOf(view)) {
            navigator.add(view, identifier);
        }

        navigatorLayout.show(navigator, identifier);
        window.setTitle(String.format("Suitable: Your Personal Outfit Advisor | %s", view.getTitle()));
    }

    /**
     * Builds the window.
     *
     * @return the window
     */
    public JFrame buildWindow() {
        final JPanel navigations = new JPanel(new GridBagLayout());
        navigations.setPreferredSize(new Dimension(AbstractView.SIZE_WIDTH_XXL, 0));
        navigations.setBackground(AbstractView.COLOR_AREA);
        navigations.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AbstractView.COLOR_BORDER));

        final GridBagConstraints navigationsConstraints = new GridBagConstraints();
        navigationsConstraints.gridx = 0;
        navigationsConstraints.weightx = 1.0;
        navigationsConstraints.fill = GridBagConstraints.HORIZONTAL;
        navigationsConstraints.insets = new Insets(
            AbstractView.SIZE_SPACING_XS, AbstractView.SIZE_SPACING_SM,
            AbstractView.SIZE_SPACING_XS, AbstractView.SIZE_SPACING_SM
        );

        final JLabel navigationsHeader = new JLabel("Navigations");
        navigationsHeader.setForeground(AbstractView.COLOR_MUTED);
        navigations.add(navigationsHeader, navigationsConstraints);
        for (Class<? extends AbstractView> navigationClass : navigationsTop) {
            final JButton navigationsItem = new JButton(get(navigationClass).getTitle());
            navigationsItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showView(navigationClass);
                }
            });
            navigations.add(navigationsItem, navigationsConstraints);
        }
        navigationsConstraints.weighty = 1.0;
        navigations.add(Box.createVerticalGlue(), navigationsConstraints);
        navigationsConstraints.weighty = 0.0;
        for (Class<? extends AbstractView> navigationClass : navigationsBottom) {
            final JButton navigationsItem = new JButton(get(navigationClass).getTitle());
            navigationsItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showView(navigationClass);
                }
            });
            navigations.add(navigationsItem, navigationsConstraints);
        }

        window.setLayout(new BorderLayout());
        window.setMinimumSize(new Dimension(WINDOW_WIDTH_MIN, WINDOW_HEIGHT_MIN));
        window.setSize(new Dimension(WINDOW_WIDTH_MIN, WINDOW_HEIGHT_MIN));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(navigations, BorderLayout.LINE_START);
        window.add(navigator, BorderLayout.CENTER);
        window.pack();

        return window;
    }
}
