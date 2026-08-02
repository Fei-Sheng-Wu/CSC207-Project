package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import data_access.wardrobe.JsonWardrobeDataAccessObject;
import interface_adapter.item_editing.ItemEditingViewModel;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_remover.WardrobeRemoverPresenter;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterPresenter;
import use_case.wardrobe.WardrobeDataAccessInterface;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInteractor;
import use_case.wardrobe_updater.WardrobeUpdaterInputBoundary;
import use_case.wardrobe_updater.WardrobeUpdaterInteractor;

/**
 * Represents the item editing view.
 */
public class ItemEditingView extends AbstractView implements PropertyChangeListener {
    private final ItemEditingViewModel viewModel;

    /**
     * Constructs a new item editing view.
     *
     * @param manager the application manager of the view
     */
    public ItemEditingView(ApplicationManager manager) {
        super(manager);
        setLayout(new BorderLayout());

        this.viewModel = new ItemEditingViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(ItemEditingViewModel.class, this.viewModel);

        final WardrobeDataAccessInterface wardrobeDataAccessObject = new JsonWardrobeDataAccessObject();

        final WardrobeManagementView[] viewHolder = new WardrobeManagementView[1];

        final WardrobeUpdaterPresenter updaterPresenter = new WardrobeUpdaterPresenter(
            message -> viewHolder[0].showMessage(message)
        );
        final WardrobeRemoverPresenter removerPresenter = new WardrobeRemoverPresenter(
            message -> viewHolder[0].showMessage(message)
        );

        final WardrobeUpdaterInputBoundary updaterInteractor = new WardrobeUpdaterInteractor(
            wardrobeDataAccessObject,
            updaterPresenter
        );
        final WardrobeRemoverInputBoundary removerInteractor = new WardrobeRemoverInteractor(
            wardrobeDataAccessObject,
            removerPresenter
        );

        final WardrobeUpdaterController updaterController = new WardrobeUpdaterController(updaterInteractor);
        final WardrobeRemoverController removerController = new WardrobeRemoverController(removerInteractor);

        viewHolder[0] = new WardrobeManagementView(
            updaterController,
            removerController,
            wardrobeDataAccessObject
        );

        add(viewHolder[0], BorderLayout.CENTER);
    }

    @Override
    public String getTitle() {
        return "Manage Clothing Items";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
