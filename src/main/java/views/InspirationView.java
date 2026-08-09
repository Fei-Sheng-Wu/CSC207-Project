package views;

import javax.swing.JLabel;

import interface_adapter.inspiration.InspirationViewModel;

/**
 * Represents the inspiration view.
 */
public class InspirationView extends AbstractView {
    private final InspirationViewModel viewModel;

    /**
     * Constructs a new inspiration view.
     *
     * @param manager the application manager of the view
     */
    public InspirationView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(InspirationViewModel.class);
        // this.viewModel.addPropertyChangeListener(this);

        add(new JLabel("@TODO: inspiration view (WIP by Jet)"));
    }

    @Override
    public String getTitle() {
        return "Inspiration";
    }
}
