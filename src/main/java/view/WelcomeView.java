package view;

import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import interface_adapter.welcome.WelcomeViewModel;

/**
 * Represents the welcome view.
 */
public class WelcomeView extends AbstractView implements PropertyChangeListener {
    private final WelcomeViewModel viewModel;

    /**
     * Constructs a new welcome view.
     *
     * @param manager the application manager of the view
     */
    public WelcomeView(ApplicationManager manager) {
        super(manager);

        this.viewModel = new WelcomeViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(WelcomeViewModel.class, this.viewModel);

        setLayout(new GridBagLayout());

        add(new JLabel("@TODO: welcome view"));
    }

    @Override
    public String getTitle() {
        return "Welcome";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
