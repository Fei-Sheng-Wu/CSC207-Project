package view;

import javax.swing.JLabel;

import interface_adapter.inspiration.InspirationViewModel;

/**
 * Represents the inspiration view.
 */
public class InspirationView extends AbstractView {
    /**
     * Constructs a new inspiration view.
     *
     * @param manager the application manager of the view
     */
    public InspirationView(ApplicationManager manager) {
        super(manager);

        manager.register(InspirationViewModel.class);

        add(new JLabel("@TODO: inspiration view"));
    }

    @Override
    public String getTitle() {
        return "Inspiration";
    }
}
