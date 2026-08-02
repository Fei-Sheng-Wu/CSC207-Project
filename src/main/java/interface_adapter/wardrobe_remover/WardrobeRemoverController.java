package interface_adapter.wardrobe_remover;

import java.util.UUID;

import entity.AbstractWear;
import entity.WearFactory;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInputData;

/**
 * Controller for removing wardrobe items.
 */
public class WardrobeRemoverController {
    private final WardrobeRemoverInputBoundary interactor;

    /**
     * Constructs a new controller.
     *
     * @param interactor the interactor of the controller
     */
    public WardrobeRemoverController(WardrobeRemoverInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the remove item use case.
     *
     * @param uuid the UUID
     * @param type the clothing item type
     */
    public void removeItem(UUID uuid, String type) {
        final AbstractWear item = WearFactory.constructWear(type, uuid);

        interactor.removeItem(new WardrobeRemoverInputData(item));
    }
}
