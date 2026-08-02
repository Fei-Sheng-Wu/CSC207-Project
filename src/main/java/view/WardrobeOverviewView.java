package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.border.EmptyBorder;

import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_adder.WardrobeAdderController;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_reporter.WardrobeReporterController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;

/**
 * Represents the wardrobe view.
 */
public class WardrobeOverviewView extends AbstractView implements PropertyChangeListener {
    private final WardrobeViewModel viewModel;
    private final WardrobeReporterController reporterController;
    private final WardrobeAdderController adderController;
    private final WardrobeUpdaterController updaterController;
    private final WardrobeRemoverController removerController;

    /**
     * Constructs a new wardrobe view.
     *
     * @param manager the application manager of the view
     */
    public WardrobeOverviewView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(WardrobeViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.reporterController = manager.get(WardrobeReporterController.class);
        this.adderController = manager.get(WardrobeAdderController.class);
        this.updaterController = manager.get(WardrobeUpdaterController.class);
        this.removerController = manager.get(WardrobeRemoverController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));
        setBorder(new EmptyBorder(SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD));

        this.reporterController.reportWardrobe();

        // @TODO: migrate original code from Edison
    }

    @Override
    public String getTitle() {
        return "My Wardrobe";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "error":
                // @TODO: update error
            case "items":
                // @TODO: update list collection
            default:
                break;
        }
    }
}
