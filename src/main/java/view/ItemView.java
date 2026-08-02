package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import interface_adapter.item.ItemViewModel;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;

/**
 * Represents the item editing view.
 */
public class ItemView extends AbstractView implements PropertyChangeListener {
    private final ItemViewModel viewModel;
    private final WardrobeUpdaterController updaterController;
    private final WardrobeRemoverController removerController;

    /**
     * Constructs a new item editing view.
     *
     * @param manager the application manager of the view
     */
    public ItemView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(ItemViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.updaterController = manager.get(WardrobeUpdaterController.class);
        this.removerController = manager.get(WardrobeRemoverController.class);

        // Initialize the layout.
        setLayout(new BorderLayout());

        // @TODO: migrate original code from Edison
    }

    @Override
    public String getTitle() {
        return "My Clothing Item";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
