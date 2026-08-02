package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_reporter.WardrobeReporterController;

/**
 * Represents the wardrobe view.
 */
public class WardrobeDetailsView extends AbstractView implements PropertyChangeListener {
    private final WardrobeViewModel viewModel;
    private final WardrobeReporterController reporterController;

    /**
     * Constructs a new wardrobe view.
     *
     * @param manager the application manager of the view
     */
    public WardrobeDetailsView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(WardrobeViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.reporterController = manager.get(WardrobeReporterController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));

        this.reporterController.reportWardrobe();

        // @TODO: reporter view for Aiman
    }

    @Override
    public String getTitle() {
        return "My Wardrobe";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "items":
                // @TODO: update content
            case "itemsOld":
                // @TODO: update content
            case "error":
                // @TODO: update content
            default:
                break;
        }
    }
}
