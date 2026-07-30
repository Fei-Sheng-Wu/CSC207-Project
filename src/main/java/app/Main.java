package app;

import javax.swing.JFrame;

import data_access.wardrobe.JsonWardrobeDataAccessObject;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_remover.WardrobeRemoverPresenter;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterPresenter;
import use_case.wardrobe.WardrobeDataAccessInterface;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInteractor;
import use_case.wardrobe_updater.WardrobeUpdaterInputBoundary;
import use_case.wardrobe_updater.WardrobeUpdaterInteractor;
import view.WardrobeManagementView;

public class Main {
    /**
     * Run the application.
     *
     * @param args the unused arguments
     */
    public static void main(String[] args) {
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

        final JFrame application = new JFrame("Suitable: Wardrobe Management");
        application.add(viewHolder[0]);
        application.pack();
        application.setLocationRelativeTo(null);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setVisible(true);
    }
}
